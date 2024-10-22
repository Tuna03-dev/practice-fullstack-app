package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.ShopCreationRequest;
import org.example.exercise_shop.dto.request.ShopUpdateRequest;
import org.example.exercise_shop.dto.response.ShopInformationResponse;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.StatusShop;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShopService {
    Shop findById(String shopId);
    Shop requestShop(ShopCreationRequest shop);
    Shop approveShop(String shopId, boolean accepted);
    List<Shop> getShops();
    Shop getShopById(String id);
    Shop updateShop(ShopUpdateRequest shop);
    void updateShopStatus(String shopId, StatusShop statusShop);
    Shop findByCurrentUser();
    Page<Shop> getAllShopsActive(int page, int size,String name, String categoryId, String address);
    ShopInformationResponse getShopDetailByProductId(String productId);
    ShopInformationResponse getShopDetailByShopId(String shopId);
    ShopInformationResponse getShopByUsername(String username);
}
