package org.example.exercise_shop.authentication;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.entity.Role;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ApplicationInitConfig implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value(value = "${init.admin-password}")
    private String ADMIN_PASSWORD;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User adminAccount = userRepository.findByRole(Role.ADMIN);
        if (adminAccount == null){
            User user = User.builder()
                    .lastname("ADMIN")
                    .role(Role.ADMIN)
                    .username("Admin123")
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .build();
            userRepository.save(user);
        }
    }
}
