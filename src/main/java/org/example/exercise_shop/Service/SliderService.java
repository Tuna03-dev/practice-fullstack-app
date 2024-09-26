package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.CreateSliderRequest;
import org.example.exercise_shop.entity.Slider;

import java.util.List;

public interface SliderService {
    void createSlider(CreateSliderRequest createSliderRequest);
    List<Slider> getSliders();
    Slider getSlider(String id);
}
