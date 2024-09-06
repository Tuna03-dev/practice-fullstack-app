package org.example.exercise_shop.Repository;


import org.example.exercise_shop.entity.Role;
import org.example.exercise_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
    boolean existsUserByUsername(String username);
    User findByRole(Role role);

    @Modifying
    @Query("UPDATE User u SET u.is2FAAuthenticated = :is2FA WHERE u.username = :username")
    void updateUser(@Param("username") String username, @Param("is2FA") boolean is2FA);

}
