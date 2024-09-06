package org.example.exercise_shop.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "two_factor_authentication")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class TwoFactorAuthentication {
    @Id
    @UuidGenerator
    @Column(name = "two_factor_id", updatable = false, unique = true)
    String id;
    String twoFactorCode;
    @Embedded
    @Builder.Default
    Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime expiredDate;
}
