package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findByDefaultAddressIsTrueAndUser_Username(String username);
    List<Address> findAllByUser_Username(String username);

}
