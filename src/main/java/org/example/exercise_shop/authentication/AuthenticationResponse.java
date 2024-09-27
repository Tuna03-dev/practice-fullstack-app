package org.example.exercise_shop.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.exercise_shop.entity.Role;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    @Builder.Default
    private boolean twoFactorRequired = false;
    private String accessToken;
    private Role role;
}
