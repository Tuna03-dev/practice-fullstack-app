package org.example.exercise_shop.Service;

public interface TwoFactorAuthenticationService {

    void sendOtpToUser(String username);
    boolean validateOtp(String username, String otp);
}
