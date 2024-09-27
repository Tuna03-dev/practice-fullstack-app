package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Slide;
import org.example.exercise_shop.entity.Slider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SliderRepository  extends JpaRepository<Slider, String> {
}
