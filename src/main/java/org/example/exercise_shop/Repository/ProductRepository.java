package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product> findProductsByDeleteAtIsNullAndShopId(String shopId, Pageable pageable);
    @Query("SELECT p from Product p where lower(p.name) like lower(concat('%', :name,'%')) and p.deleteAt is null ")
    Page<Product> findAllByNameIsLikeIgnoreCaseAndDeleteAtIsNull(@Param(value = "name") String name, Pageable pageable);


}
