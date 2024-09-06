package org.example.exercise_shop.authentication;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.CartService;
import org.example.exercise_shop.Service.UserService;
import org.example.exercise_shop.config.JwtTokenService;
import org.example.exercise_shop.dto.request.RegisterRequest;
import org.example.exercise_shop.dto.request.Verification2FARequest;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CartService cartService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;


    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        String cartData = cartService.getCartDataFromCookies(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User)authentication.getPrincipal();

        if (cartData != null && !cartData.isEmpty()){
            cartService.syncCartFromCookies(cartData, user.getId());
            cartService.clearCartCookie(response);
        }

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .message("Authenticated")
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Valid RegisterRequest registerRequest){
        return  ApiResponse.<User>builder()
                .data(authenticationService.register(registerRequest))
                .message("Register Successfully")
                .build();
    }


    @PostMapping("/verify-otp")
    public ApiResponse<AuthenticationResponse> verifyOtp(@RequestBody Verification2FARequest verification2FARequest){
        AuthenticationResponse authenticationResponse = authenticationService.verifyOtp(verification2FARequest.getUsername(), verification2FARequest.getOtp());
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .message("OTP verified and authenticated")
                .build();

    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refresh(refreshTokenRequest))
                .message("Refresh Token Successfully")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestBody LogoutRequest request){
        String username = jwtTokenService.extractUsername(request.getToken());
        userService.logout2FAAuthentication(username);
        authenticationService.logout(request);

        return  ApiResponse.builder()
                .message("Logout Successfully")
                .build();
    }
}
