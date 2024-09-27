package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.CreateSlideRequest;
import org.example.exercise_shop.dto.response.SlideResponse;
import org.example.exercise_shop.entity.Slide;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlideMapper {

    Slide toSlide(CreateSlideRequest createSlideRequest);
    SlideResponse toSlideResponse(Slide slide);
}
