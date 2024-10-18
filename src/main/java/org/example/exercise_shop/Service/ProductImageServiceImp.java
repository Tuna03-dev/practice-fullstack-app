package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.dto.request.CreateProductImageRequest;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.ProductImage;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImp implements ProductImageService {
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;
    @Override
    public void addImage(String productId, List<CreateProductImageRequest> createProductImageRequests) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        List<ProductImage> productImages = productImageMapper.toListProductImage(createProductImageRequests);
        productImages.forEach(productImage -> productImage.setProduct(product));
        product.setProductImages(productImages);

        productRepository.save(product);

    }
}
