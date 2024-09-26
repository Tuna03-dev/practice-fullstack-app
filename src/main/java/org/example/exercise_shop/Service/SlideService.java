package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.CreateSlideRequest;
import org.example.exercise_shop.dto.response.SlideResponse;
import org.example.exercise_shop.entity.Slide;
import org.example.exercise_shop.entity.SliderName;

import java.util.List;

public interface SlideService {
    void createSlide(CreateSlideRequest createSlideRequest);
    List<SlideResponse> getSlidesBySliderName(SliderName sliderName);
    Slide getSlide(String id);

}
