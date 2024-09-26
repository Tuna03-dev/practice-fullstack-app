package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.CreateSliderRequest;
import org.example.exercise_shop.entity.Slider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SliderMapper {

    Slider toSlider(CreateSliderRequest createSliderRequest);

}
