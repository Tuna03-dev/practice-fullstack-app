package org.example.exercise_shop.controller.common;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ReviewService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ReviewReponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rate-products")
public class RateProductsController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ApiResponse<Page<ReviewReponse>> getAllReviewByProduct(@PathVariable String id,
                                                                  @RequestParam(value = "page", defaultValue = "0")int page,
                                                                  @RequestParam(value = "size", defaultValue = "10")int size){
        Page<ReviewReponse> reviews = reviewService.getReviewByProduct(id,page,size);


        return ApiResponse.<Page<ReviewReponse>>builder()
                .data(reviews)
                .build();
    }
}
