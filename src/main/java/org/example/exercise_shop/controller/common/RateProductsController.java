package org.example.exercise_shop.controller.common;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ReviewService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rate-products")
public class RateProductsController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ApiResponse<Page<ReviewResponse>> getAllReviewByProduct(@PathVariable String id,
                                                                   @RequestParam(value = "page", defaultValue = "0")int page,
                                                                   @RequestParam(value = "size", defaultValue = "10")int size,
                                                                   @RequestParam(value = "sort", defaultValue = "Newest")String sort){
        Page<ReviewResponse> reviews = reviewService.getReviewByProduct(id,page,size,sort);


        return ApiResponse.<Page<ReviewResponse>>builder()
                .data(reviews)
                .build();
    }
}
