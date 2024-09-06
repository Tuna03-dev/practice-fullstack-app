package org.example.exercise_shop.authentication;

import lombok.*;
import org.example.exercise_shop.entity.Role;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private Role role;
}
