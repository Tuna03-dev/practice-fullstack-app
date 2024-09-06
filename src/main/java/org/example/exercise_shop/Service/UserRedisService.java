package org.example.exercise_shop.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.example.exercise_shop.entity.User;

public interface UserRedisService {
    void clear(String userId);
    UserProfileResponse getUserProfile(String userId) throws JsonProcessingException;
    void cacheUserProfile(UserProfileResponse userProfileResponse) throws JsonProcessingException;

}
