package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.CategoryRepository;
import org.example.exercise_shop.dto.request.ProductCreationRequest;
import org.example.exercise_shop.dto.request.ProductUpdateRequest;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.ProductMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getProducts(String name, int page, int size, ProductSortType productSortType, String categoryId) {
        Pageable pageable;
        switch (productSortType) {
            case HOT -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("soldQuantity"))
                    .and(Sort.by(Sort.Order.desc("averageRate"))));
            case NEW -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("audit.createdAt")));
            case PRICE_ASC -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("price")));
            case PRICE_DESC -> pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("price")));
            default -> pageable = PageRequest.of(page, size);
        }


        Page<Product> productPage = productRepository.findAllByNameIsLikeIgnoreCaseAndCategory_IdAndDeleteAtIsNull(name, categoryId, pageable);
        List<ProductResponse> productResponses = mapToProductResponseHaveDiscount(productPage.getContent());


        return new PageImpl<>(productResponses, pageable, productPage.getTotalElements());
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
                .image(productCreationRequest.getImage())
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

    @Override
    public List<ProductResponse> getBestSellers(int size) {
        List<Product> products = productRepository.findBestSellers(size);
        return mapToProductResponseHaveDiscount(products);
    }

    @Override
    public List<ProductResponse> getNewArrival(int size) {
        LocalDateTime date = LocalDateTime.now().minusDays(15);
        List<Product> products = productRepository.findNewArrival(size, date);
        if (products.size() < size){
            products.addAll(productRepository.findNewArrivalIfNull(size));
        }
        return mapToProductResponseHaveDiscount(products);
    }

    @Override
    public List<ProductResponse> getTopRates(int size) {
        List<Product> products = productRepository.findTopRates(size);
        return mapToProductResponseHaveDiscount(products);
    }


    private Double getDiscountPercentage(Product product){
        return (product.getDiscounts() != null && !product.getDiscounts().isEmpty())
                ? product.getDiscounts().stream()
                .filter(discount -> LocalDateTime.now().isAfter(discount.getStartDate()) &&
                        LocalDateTime.now().isBefore(discount.getEndDate()))
                .map(Discount::getPercentage)
                .findFirst()
                .orElse(0.0)
                : 0.0;
    }

    private List<ProductResponse> mapToProductResponseHaveDiscount(List<Product> products){
        return products.stream().map(
                product -> {
                    Double discountPercentage = getDiscountPercentage(product);
                    ProductResponse productResponse = productMapper.toProductResponse(product);
                    productResponse.setPriceWithDiscount(product.getPrice().subtract(BigDecimal.valueOf(discountPercentage).multiply(product.getPrice())));
                    productResponse.setCategoryName(product.getCategory().getName());
                    return productResponse;
                }

        ).toList();
    }
}
