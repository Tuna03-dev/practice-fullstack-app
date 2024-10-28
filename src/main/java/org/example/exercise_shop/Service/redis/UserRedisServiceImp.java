package org.example.exercise_shop.Service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRedisServiceImp implements UserRedisService{

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
    private static final String USER_PROFILE_CACHE_KEY_PREFIX = "userprofile:";

    private String generateKey(String userId){
        return USER_PROFILE_CACHE_KEY_PREFIX+ " " + userId;
    }

    @Override
    public void clear(String username) {
        redisTemplate.delete(generateKey(username));
    }

    @Override
    public UserProfileResponse getUserProfile(String username) throws JsonProcessingException {
        String key = generateKey(username);
        String json = (String) redisTemplate.opsForValue().get(key);

        return json != null ? redisObjectMapper.readValue(json, new TypeReference<UserProfileResponse>(){}) : null;
    }

    @Override
    public void cacheUserProfile(UserProfileResponse userProfileResponse) throws JsonProcessingException {
        String key = generateKey(userProfileResponse.getId());
        String json = redisObjectMapper.writeValueAsString(userProfileResponse);

        long ttl = 10*60 + (long) (Math.random() *30);
        redisTemplate.opsForValue().set(key, json,ttl, TimeUnit.SECONDS);
    }
}
