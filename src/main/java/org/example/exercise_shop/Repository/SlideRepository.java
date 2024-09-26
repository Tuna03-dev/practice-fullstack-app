package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Slide;
import org.example.exercise_shop.entity.SliderName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, String> {
    List<Slide> getAllBySlider_Name(SliderName sliderName);

}
