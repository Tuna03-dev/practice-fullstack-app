package org.example.exercise_shop.Service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.exercise_shop.dto.response.UserProfileResponse;

public interface UserRedisService {
    void clear(String userId);
    UserProfileResponse getUserProfile(String username) throws JsonProcessingException;
    void cacheUserProfile(UserProfileResponse userProfileResponse) throws JsonProcessingException;

}
