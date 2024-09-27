package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.SlideService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.CreateSlideRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/managements/slides")
@RequiredArgsConstructor
public class ManagementSlideController {

    private final SlideService slideService;

    @PostMapping("/create")
    public ApiResponse<Object> createSlide(@RequestBody CreateSlideRequest createSlideRequest){
        try{
            slideService.createSlide(createSlideRequest);
        }catch (Exception e){
            return ApiResponse.builder()
                    .message("Create slide failed")
                    .build();
        }

        return ApiResponse.builder()
                .message("Create slide success")
                .build();
    }
}
