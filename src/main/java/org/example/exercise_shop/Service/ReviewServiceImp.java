package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.OrderRepository;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Repository.ReviewRepository;
import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewReponse;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.Review;
import org.example.exercise_shop.entity.StatusOrder;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.ReviewMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImp implements  ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Page<ReviewReponse> getReviewByProduct(String productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("Audit.createdAt"));
        Page<Review> reviews = reviewRepository.findAllByProductId(productId, pageable);

        return reviews.map(review -> {
            ReviewReponse reviewReponse = new ReviewReponse();
            reviewReponse = reviewMapper.toReviewResponse(review);
            reviewReponse.setProductId(review.getProduct().getId());
            reviewReponse.setUserId(review.getUser().getId());
            reviewReponse.setCreatedAt(review.getAudit().getCreatedAt());
            reviewReponse.setUpdatedAt(review.getAudit().getUpdatedAt());
            return reviewReponse;
        });
    }

    @Override
    public Review addReviewForProduct(AddReviewRequest addReviewRequest) {
        if (!orderRepository.existsByOrOrderItems_ProductIdAndStatus(addReviewRequest.getProductId(), StatusOrder.COMPLETED)){
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_PURCHASED);
        }
        Product product = productRepository.findById(addReviewRequest.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Review review = reviewMapper.toReview(addReviewRequest);
        review.setProduct(product);
        review.setUser(user);
        return reviewRepository.save(review);
    }
}
