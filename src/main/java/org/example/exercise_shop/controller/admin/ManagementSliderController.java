package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.SliderService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.CreateSliderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/managements/sliders")
@RequiredArgsConstructor
public class ManagementSliderController {

    private final SliderService sliderService;

    @PostMapping("/create")
    public ApiResponse<Object> createSlider(@RequestBody CreateSliderRequest createSliderRequest){

        try {
            sliderService.createSlider(createSliderRequest);
        }catch (Exception e){
            return ApiResponse.builder()
                    .message("Create slider failed")
                    .build();
        }

        return ApiResponse.builder()
                .message("Create slider success")
                .build();
    }

}
