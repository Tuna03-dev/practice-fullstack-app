package org.example.exercise_shop.controller.common;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.SlideService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.SlideResponse;
import org.example.exercise_shop.entity.Slide;
import org.example.exercise_shop.entity.SliderName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sliders")
public class SliderController {

    private final SlideService slideService;

    @GetMapping("/get-slides/{sliderName}")
    public ApiResponse<List<SlideResponse>> getSlidesBySliderId(@PathVariable SliderName sliderName) {
        return ApiResponse.<List<SlideResponse>>builder()
                .data(slideService.getSlidesBySliderName(sliderName))
                .build();
    }

}
