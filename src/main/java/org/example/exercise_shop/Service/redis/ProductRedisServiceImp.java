package org.example.exercise_shop.Service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.ProductSortType;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductRedisServiceImp implements ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String PRODUCT_CACHE_KEY_PREFIX = "productPages:";

    @Override
    public void clear(String name, int page, int size, ProductSortType sort, String categoryId) {
        redisTemplate.delete(generateCacheKey(name, page, size, sort, categoryId));
    }

    @Override
    public Page<ProductResponse> getProduct(String name, int page, int size, ProductSortType sort, String categoryId) throws JsonProcessingException {
        String key = generateCacheKey(name, page, size, sort, categoryId);
        String json = (String) redisTemplate.opsForValue().get(key);
        return json != null ? objectMapper.readValue(json, new TypeReference<Page<ProductResponse>>() {
        }) : null;
    }

    @Override
    public void cacheProductList(String name, int page, int size,ProductSortType sort, String categoryId, Page<ProductResponse> productPage) {
        String key = generateCacheKey(name, page, size, sort, categoryId);
        try {
            String json = objectMapper.writeValueAsString(productPage);
            long ttl = 10 * 60 + (long) (Math.random() * 30);
            redisTemplate.opsForValue().set(key, json, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ErrorCode.ERROR_CACHE);
        }
    }

    private String generateCacheKey(String name, int page, int size,ProductSortType sort, String categoryId) {
        return PRODUCT_CACHE_KEY_PREFIX+ "products:" + name + ":" + page + ":" + size + ":" + sort + ":" + categoryId;
    }
}
