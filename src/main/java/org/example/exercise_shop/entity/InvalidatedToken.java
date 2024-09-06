package org.example.exercise_shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {

    @Id
    @Column(name = "invalidated_token_id", unique = true,updatable = false)
    String id;
    Date expiryDate;

}
