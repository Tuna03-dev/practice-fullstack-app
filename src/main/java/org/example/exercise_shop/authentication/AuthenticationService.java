package org.example.exercise_shop.authentication;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Service.TwoFactorAuthenticationService;
import org.example.exercise_shop.Service.UserService;
import org.example.exercise_shop.config.JwtTokenService;
import org.example.exercise_shop.dto.request.RegisterRequest;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.exception.InvalidTokenException;
import org.example.exercise_shop.Repository.InvalidatedTokenRepository;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserMapper userMapper;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;

    public User register(RegisterRequest registerRequest){
        if (userRepository.existsUserByUsername(registerRequest.getUsername())){
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAudit(new Audit())  ;
        user.setStatus(true);
        return userRepository.save(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_USER));
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))){
            twoFactorAuthenticationService.sendOtpToUser(user.getUsername());
            throw new ApplicationException(ErrorCode.ADMIN_AUTHENTICATE);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenService.generateToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse verifyOtp(String username, String otp){
        if (twoFactorAuthenticationService.validateOtp(username, otp)){
            var user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_USER));
            user.set2FAAuthenticated(true);
            userRepository.save(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtTokenService.generateToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .role(user.getRole())
                    .build();
        }else {
            throw new ApplicationException(ErrorCode.OTP_FAILED);
        }
    }


    public AuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest){
        String refreshToken = refreshTokenRequest.getToken();
        String username = jwtTokenService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (jwtTokenService.validateToken(refreshToken, user)){
            String newAccessToken = jwtTokenService.generateToken(user);
            String newRefreshToken = jwtTokenService.generateRefreshToken(user);
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtTokenService.extractClaim(refreshToken, Claims::getId))
                    .expiryDate(jwtTokenService.extractClaim(refreshToken, Claims::getExpiration))
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .role(user.getRole())
                    .build();
        }else {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Transactional
    public void logout(LogoutRequest request){
        String username = jwtTokenService.extractUsername(request.getToken());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (jwtTokenService.validateToken(request.getToken(), user)){

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtTokenService.extractClaim(request.getToken(), Claims::getId))
                    .expiryDate(jwtTokenService.extractClaim(request.getToken(), Claims::getExpiration))
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);

            log.info("success");
        }else{
            throw new InvalidTokenException("Token is invalid or expired");
        }


    }

}
