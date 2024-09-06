package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {


    @Query("SELECT r FROM Report r WHERE r.shopId = :shopId AND ( (r.year = : startYear AND r.year = :endYear AND r.month >= :startMonth AND r.month <= : endMonth ) OR (r.year = :startYear AND r.month >= :startMonth AND r.year < :endYear) OR (r.year > :startYear AND r.year = :endYear AND r.month <= :endMonth) OR (r.year > :startYear AND r.year <: endYear))")
    List<Report> findByShopIdAndYearAndMonthBetween( @Param("shopId") String shopId,
                                                     @Param("startMonth") int startMonth,
                                                     @Param("startYear") int startYear,
                                                     @Param("endMonth") int endMonth,
                                                     @Param("endYear") int endYear);

}

