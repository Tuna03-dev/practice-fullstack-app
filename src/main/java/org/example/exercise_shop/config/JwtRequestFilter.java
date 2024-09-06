package org.example.exercise_shop.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.Service.TwoFactorAuthenticationService;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username =null;
        String jwt =null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try{
                username = jwtTokenService.extractUsername(jwt);
            }catch (IllegalArgumentException e){
                log.info("Unable to get JWT Token");
            }catch (ExpiredJwtException e){

                log.info("JWT Token has expired");
            }
        }else{
            log.warn("JWT token does not begin with Bearer string");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenService.validateToken(jwt, userDetails)){
                if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))){

                    User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_USER));
                    if (!user.is2FAAuthenticated()) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "2FA failed");
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or has been blacklisted.");
                return;
            }
        }
        filterChain.doFilter(request, response) ;
    }
}
