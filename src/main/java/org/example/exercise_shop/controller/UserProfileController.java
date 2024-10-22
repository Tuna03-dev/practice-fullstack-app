package org.example.exercise_shop.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.redis.UserRedisService;
import org.example.exercise_shop.Service.UserService;
import org.example.exercise_shop.dto.ApiResponse;

import org.example.exercise_shop.dto.request.UpdateUserProfileRequest;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PutMapping("/update")
    public ApiResponse<UserProfileResponse> updateUserProfile(@RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(updateUserProfileRequest.getUsername())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        userService.updateUserProfile(updateUserProfileRequest);
        userRedisService.clear(updateUserProfileRequest.getUsername());

        return ApiResponse.<UserProfileResponse>builder()
                .message("User profile updated successfully")
                .build();
    }

    @PutMapping("/save-avatar-url")
    public ApiResponse<Object> saveAvatarUrl(@RequestBody Map<String, String> avatarUrlMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.saveAvatarUrl(authentication.getName(), avatarUrlMap.get("url"));
        userRedisService.clear(authentication.getName());

        return ApiResponse.<Object>builder()
                .message("Update avatar successfully")
                .build();
    }

}
