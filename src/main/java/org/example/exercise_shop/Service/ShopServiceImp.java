package org.example.exercise_shop.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.CategoryRepository;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Repository.ShopRepository;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.dto.request.ShopCreationRequest;
import org.example.exercise_shop.dto.request.ShopUpdateRequest;
import org.example.exercise_shop.dto.response.ShopInformationResponse;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.ShopMapper;
import org.example.exercise_shop.utils.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements ShopService{
    private final DateTimeFormatter dateTimeFormatter;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ShopMapper shopMapper;
    private final ProductRepository productRepository;

    @Override
    public Shop findById(String shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND));

        return shop;
    }

    @Override
    public Shop requestShop(ShopCreationRequest shop) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        if (shopRepository.countByUserIdAndStatus(user.getId(), StatusShop.ACTIVE) > 0){
            throw new ApplicationException(ErrorCode.EXCEED_NUMBER_SHOP);
        }
        Set<Category> categories = shop.getCategories().stream()
                .map(category -> categoryRepository.findById(category.getId()).orElse(null)).collect(Collectors.toSet());

        Shop createShop = Shop.builder()
                .name(shop.getName())
                .address(shop.getAddress())
                .requestDate(LocalDateTime.now())
                .categories(categories)
                .status(StatusShop.PENDING)
                .userId(user.getId())
                .audit(new Audit())
                .build();
        return shopRepository.save(createShop);
    }

    @Override
    public Shop approveShop(String shopId, boolean accepted) {
        Shop shop = getShopById(shopId);
        if (accepted){
            if (shopRepository.countByUserIdAndStatus(shop.getUserId(), StatusShop.ACTIVE) > 0){
                throw new ApplicationException(ErrorCode.EXCEED_NUMBER_SHOP);
            }
            shop.setStatus(StatusShop.ACTIVE);
            shopRepository.rejectOtherRequestByUserId(shop.getUserId(), shopId, StatusShop.REJECTED);

        }else {
            shop.setStatus(StatusShop.REJECTED);

        }
        return shopRepository.save(shop);
    }

    @Override
    public List<Shop> getShops() {
        return shopRepository.findAll();
    }

    @Override
    public Shop getShopById(String id) {
        return shopRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND));
    }

    @Override
    public Shop updateShop(ShopUpdateRequest shopUpdateRequest) {
        Shop shop = getShopById(shopUpdateRequest.getId());
        shop.setName(shopUpdateRequest.getName());
        shop.setAddress(shopUpdateRequest.getAddress());
        Set<Category> categories = shopUpdateRequest.getCategories().stream().map(category -> categoryRepository.findById(category.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND))).collect(Collectors.toSet());
        shop.setCategories(categories);
        return shopRepository.save(shop);
    }

    @Override
    public void updateShopStatus(String shopId,StatusShop statusShop) {
        Shop shop = this.getShopById(shopId);
        shop.setStatus(statusShop);
        shopRepository.save(shop);
    }

    @Override
    public Shop findByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user == null) throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        try{
            Shop shop = shopRepository.findByUserIdAndStatus(user.getId(), StatusShop.ACTIVE);
            if (shop == null) throw new ApplicationException(ErrorCode.SHOP_NOT_FOUND);
            return shop;
        }catch (ApplicationException e){
            throw new ApplicationException(ErrorCode.SHOP_NOT_FOUND);
        }

    }

    @Override
    public Page<Shop>   getAllShopsActive(int page, int size,String name, String categoryId, String address) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("rate")));


        return shopRepository.findAllShopsByFilters(
                StatusShop.ACTIVE,
                categoryId,
                name != null ? name.toLowerCase() : null,
                address != null ?address.toLowerCase() : null,
                pageable);


    }

    @Override
    public ShopInformationResponse getShopDetailByProductId(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        Shop shop = product.getShop();
        ShopInformationResponse shopInformationResponse = shopMapper.toShopInformationResponse(shop);
        shopInformationResponse.setNumberOfProducts(shop.getProducts().size());
        shopInformationResponse.setCreatedAt(shop.getAudit().getCreatedAt());
        shopInformationResponse.setUpdatedAt(shop.getAudit().getUpdatedAt());
        shopInformationResponse.setJoinedDate(dateTimeFormatter.format(shop.getAudit().getCreatedAt()));
        return shopInformationResponse;
    }

    @Override
    public ShopInformationResponse getShopDetailByShopId(String id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND));
        ShopInformationResponse shopInformationResponse = shopMapper.toShopInformationResponse(shop);
        shopInformationResponse.setNumberOfProducts(shop.getProducts().size());
        shopInformationResponse.setCreatedAt(shop.getAudit().getCreatedAt());
        shopInformationResponse.setUpdatedAt(shop.getAudit().getUpdatedAt());
        shopInformationResponse.setJoinedDate(dateTimeFormatter.format(shop.getAudit().getCreatedAt()));
        return shopInformationResponse;
    }

    @Override
    public ShopInformationResponse getShopByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Shop shop =  shopRepository.findByUserIdAndStatus(user.getId(), StatusShop.ACTIVE);
        ShopInformationResponse shopInformationResponse = shopMapper.toShopInformationResponse(shop);
        shopInformationResponse.setNumberOfProducts(shop.getProducts().size());
        shopInformationResponse.setCreatedAt(shop.getAudit().getCreatedAt());
        shopInformationResponse.setUpdatedAt(shop.getAudit().getUpdatedAt());
        shopInformationResponse.setJoinedDate(dateTimeFormatter.format(shop.getAudit().getCreatedAt()));
        return shopInformationResponse;
    }


}
