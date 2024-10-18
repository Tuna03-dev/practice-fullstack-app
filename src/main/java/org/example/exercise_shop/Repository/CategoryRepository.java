package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {

    @Query("SELECT c FROM Category c JOIN c.shops s WHERE s.id = :shopId")
    List<Category> findAllByShopId(@Param("shopId") String shopId);

}
