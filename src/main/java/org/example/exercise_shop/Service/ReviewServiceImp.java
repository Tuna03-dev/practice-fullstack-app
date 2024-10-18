package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.OrderRepository;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Repository.ReviewRepository;
import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewResponse;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.Review;
import org.example.exercise_shop.entity.StatusOrder;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.ReviewMapper;
import org.example.exercise_shop.utils.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewServiceImp implements  ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DateTimeFormatter dateTimeFormatter;
    
    @Override
    public Page<ReviewResponse> getReviewByProduct(String productId, int page, int size, String sort) {
        Pageable pageable = switch (sort) {
            case "Newest" -> PageRequest.of(page, size, Sort.by("audit.updatedAt").descending());
            case "Oldest" -> PageRequest.of(page, size, Sort.by("audit.updatedAt").ascending());
            default -> throw new IllegalStateException("Unexpected value: " + sort);
        };
        Page<Review> reviews = reviewRepository.findAllByProductId(productId, pageable);

        return reviews.map(review -> {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse = reviewMapper.toReviewResponse(review);
            reviewResponse.setProductId(review.getProduct().getId());
            reviewResponse.setUserId(review.getUser().getId());
            reviewResponse.setUserName(review.getUser().getUsername());
            reviewResponse.setUserAvatar(review.getUser().getImageUrl());
            reviewResponse.setCreatedAt(dateTimeFormatter.format(review.getAudit().getCreatedAt()));
            reviewResponse.setUpdatedAt(dateTimeFormatter.format(review.getAudit().getUpdatedAt()));
            return reviewResponse;
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
