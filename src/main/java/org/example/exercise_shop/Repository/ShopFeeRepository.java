package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.ShopFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ShopFeeRepository extends JpaRepository<ShopFee, String> {

    @Modifying
    @Query("UPDATE ShopFee sf set sf.effectiveDate = :effectiveDate, sf.percentage = :percentage")
    void updateAllByPercentageAndEffectiveDate(@Param(value = "percentage") double percentage,@Param(value = "effectiveDate") LocalDate effectiveDate);

}
