package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {

}
