package org.example.exercise_shop.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.redis.UserRedisService;
import org.example.exercise_shop.Service.UserService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final UserRedisService userRedisService;


    @GetMapping("/{username}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable String username) {
        try{
            UserProfileResponse cacheProfile = userRedisService.getUserProfile(username);
            if (cacheProfile != null){
                return ApiResponse.<UserProfileResponse>builder()
                        .data(cacheProfile)
                        .build();
            }

            UserProfileResponse profileResponse = userService.getUserProfileByUsername(username);
            userRedisService.cacheUserProfile(profileResponse);

            return ApiResponse.<UserProfileResponse>builder()
                    .data(profileResponse)
                    .build();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
