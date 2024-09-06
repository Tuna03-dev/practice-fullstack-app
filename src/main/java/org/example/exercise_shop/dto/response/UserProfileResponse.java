package org.example.exercise_shop.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Audit;
import org.example.exercise_shop.entity.Role;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String username;
    String password;
    String firstname;
    String lastname;
    String phone;
    String email;
    boolean gender;
    LocalDate birthDate;
    boolean status;
    Role role;
    Audit audit;
}
