package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.CategoryRepository;
import org.example.exercise_shop.dto.request.ProductCreationRequest;
import org.example.exercise_shop.dto.request.ProductUpdateRequest;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Product> getProducts(String name, int page, int size, ProductSortType productSortType) {
        Pageable pageable;
        switch (productSortType){
            case HOT -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("soldQuantity"))
                    .and(Sort.by(Sort.Order.desc("averageRate"))));
            case NEW -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("audit.createdAt")));
            default -> pageable = PageRequest.of(page, size);
        }

        return productRepository.findAllByNameIsLikeIgnoreCaseAndDeleteAtIsNull(name,pageable);
    }

    @Override
    public Page<Product> getProductsByDeletedAtAndShopId(String shopId, int page, int size) {
        return productRepository.findProductsByDeleteAtIsNullAndShopId(shopId, PageRequest.of(page, size, Sort.by(Sort.Order.desc("soldQuantity")).and(Sort.by(Sort.Order.desc("averageRate")))));
    }

    @Override
    public Product getProduct(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product does not exist"));
    }

    @Override
    public Product addProduct(ProductCreationRequest productCreationRequest, Shop shop) {

        Product product = Product.builder()
                .name(productCreationRequest.getName())
                .price(productCreationRequest.getPrice())
                .category(productCreationRequest.getCategory())
                .stockQuantity(productCreationRequest.getSockQuantity())
                .shop(shop)
                .audit(new Audit())
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest productUpdateRequest) {
        Category category = categoryRepository.findById(productUpdateRequest.getCategoryId()).orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = productRepository.findById(productUpdateRequest.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setName(productUpdateRequest.getName());
        product.setPrice(productUpdateRequest.getPrice());
        product.setStockQuantity(productUpdateRequest.getSockQuantity());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productId, Shop shop) {

        Product product = productRepository.findById(productId).orElseThrow(()-> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        if (shop.getId().equals(product.getShop().getId())){
            product.setDeleteAt(LocalDateTime.now());
            productRepository.save(product);
        }else {
            throw new ApplicationException(ErrorCode.DONT_HAVE_PERMISSION);
        }

    }
}
