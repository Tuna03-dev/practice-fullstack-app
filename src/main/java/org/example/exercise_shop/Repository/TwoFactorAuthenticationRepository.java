package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.TwoFactorAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorAuthenticationRepository extends JpaRepository<TwoFactorAuthentication, String> {
    TwoFactorAuthentication findByUser_UsernameAndTwoFactorCode(String username, String twoFactorCode);

}
