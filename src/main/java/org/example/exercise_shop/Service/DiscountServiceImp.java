package org.example.exercise_shop.Service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.DiscountRepository;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Repository.ShopRepository;
import org.example.exercise_shop.dto.request.CreateDiscountRequest;
import org.example.exercise_shop.entity.Discount;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.DiscountMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountServiceImp implements DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Transactional
    @Override
    public void createDiscount(CreateDiscountRequest createDiscountRequest) {
        Discount discount = discountMapper.toDiscount(createDiscountRequest);
        List<Product> products = productRepository.findAllByIdIn(createDiscountRequest.getProductIds());
        Shop shop = shopRepository.findById(createDiscountRequest.getShopId()).orElseThrow(() -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND));
        for (Product product : products) {
            product.getDiscounts().add(discount);
        }

        discount.setShop(shop);
        discount.setProducts(products);

        discountRepository.save(discount);
    }
}
