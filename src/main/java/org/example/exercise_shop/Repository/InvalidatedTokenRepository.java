package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsById(String id);
}
