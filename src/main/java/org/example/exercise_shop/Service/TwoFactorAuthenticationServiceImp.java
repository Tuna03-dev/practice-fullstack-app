package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.TwoFactorAuthenticationRepository;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.dto.request.EmailRequest;
import org.example.exercise_shop.dto.request.Recipient;
import org.example.exercise_shop.dto.request.Sender;
import org.example.exercise_shop.entity.TwoFactorAuthentication;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthenticationServiceImp implements TwoFactorAuthenticationService{

    private final TwoFactorAuthenticationRepository twoFactorAuthenticationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public void sendOtpToUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        String otpCode = generateOtp();
        saveOtp(user, otpCode);
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Tuna DotCom")
                        .email("ngminhtuan1128@gmail.com")
                        .build())
                .to(Collections.singletonList(Recipient.builder()
                        .name(user.getUsername())
                        .email(user.getEmail())
                        .build()))
                .subject("Your 2FA OTP code")
                .htmlContent("<p>Your Otp code is: <strong>" + otpCode + "</strong></p>")
                .build();
        emailService.sendEmail(emailRequest);
    }

    @Override
    public boolean validateOtp(String username, String otp) {
        TwoFactorAuthentication twoFactorAuthentication = twoFactorAuthenticationRepository.findByUser_UsernameAndTwoFactorCode(username, otp);
        if (twoFactorAuthentication != null && twoFactorAuthentication.getExpiredDate().isAfter(LocalDateTime.now())) {
            twoFactorAuthenticationRepository.delete(twoFactorAuthentication);
            return true;
        }
        return false;
    }



    private void saveOtp(User user, String otp) {
        TwoFactorAuthentication twoFactorAuthentication = TwoFactorAuthentication.builder()
                .user(user)
                .twoFactorCode(otp)
                .expiredDate(LocalDateTime.now().plusMinutes(5))
                .build();
        twoFactorAuthenticationRepository.save(twoFactorAuthentication);
    }

    private String generateOtp(){
        return String.format("%04d", new Random().nextInt(9999));
    }


}
