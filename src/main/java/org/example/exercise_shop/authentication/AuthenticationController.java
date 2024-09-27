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
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CartService cartService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest, response);

        if (authenticationResponse.isTwoFactorRequired()) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .data(authenticationResponse)
                    .message("Two-factor authentication required")
                    .build();
        }

        String cartData = cartService.getCartDataFromCookies(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var user = (User) authentication.getPrincipal();
            if (cartData != null && !cartData.isEmpty()) {
                cartService.syncCartFromCookies(cartData, user.getId());
                cartService.clearCartCookie(response);
            }
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
        userService.login2FAAuthentication(verification2FARequest.getUsername());
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .message("OTP verified and authenticated")
                .build();

    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response){

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refresh(request, response))
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

    @PostMapping("/check-username/{username}")
    public ApiResponse<Boolean> checkUsername(@PathVariable String username){
        return ApiResponse.<Boolean>builder()
                .data(authenticationService.checkUsername(username))
                .message("Username is available")
                .build();
    }
}
