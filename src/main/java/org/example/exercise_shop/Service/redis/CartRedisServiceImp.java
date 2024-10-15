package org.example.exercise_shop.Service.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartRedisServiceImp implements CartRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_CACHE_KEY_PREFIX = "cart:";


    @Override
    public void clear(String sessionId) {
        redisTemplate.delete(generateCacheKey(sessionId));
    }

    @Override
    public List<AddToCartRequest> getCart(String sessionId) {
        String cartJson = (String) redisTemplate.opsForValue().get(generateCacheKey(sessionId));
        if (cartJson == null) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(cartJson, new TypeReference<List<AddToCartRequest>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void cacheCart(String sessionId, List<AddToCartRequest> cartRequestList) {
        try{
            String cartJson = objectMapper.writeValueAsString(cartRequestList);
            redisTemplate.opsForValue().set(generateCacheKey(sessionId), cartJson, 3600, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.ERROR_CACHE);
        }
    }


    private String generateCacheKey(String sessionId) {
        return CART_CACHE_KEY_PREFIX + sessionId;
    }
}
