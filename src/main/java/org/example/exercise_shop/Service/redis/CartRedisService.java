package org.example.exercise_shop.Service.redis;

import org.example.exercise_shop.dto.request.AddToCartRequest;

import java.util.List;

public interface CartRedisService {
    void clear(String sessionId);
    List<AddToCartRequest> getCart(String sessionId);
    void cacheCart(String sessionId, List<AddToCartRequest> cartRequestList);

}
