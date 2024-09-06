package org.example.exercise_shop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User Not Found"),
    INVALID_TOKEN(1002, "Invalid token"),
    UNCATEGORIZED(9999, "Uncategorized error"),
    USER_ALREADY_EXISTS(1003, "User Already Exists"),
    USERNAME_INVALID(1004, "Username must be at least 6 characters and maximum 20 characters"),
    PASSWORD_INVALID(1005, "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character"),
    PASSWORD_TOO_SHORT(1006, "Password must be at least 8 characters"),
    SHOP_EXISTED(1007, "Shop Already Exists"),
    CATEGORY_EXISTED(1008, "Category Already Exists"),
    SHOP_NOT_FOUND(1009, "Shop Not Found"),
    SHOP_FEE_EXISTED(1010,"Shop fee already exists"),
    EXCEED_NUMBER_SHOP(1011, "Each user is only allowed one shop"),
    CATEGORY_NOT_FOUND(1012,"Category not found"),
    PRODUCT_NOT_FOUND(1013,"Product does not exists"),
    DONT_HAVE_PERMISSION(1014,"Don't have permission"),
    OUT_OF_STOCK(1015,"Not enough stock available"),
    INVALID_USER(1016,"Invalid username or password"),
    PRODUCT_NOT_PURCHASED(1017, "Product have not been purchased"),
    ORDER_NOT_FOUND(1018, "Order not found"),
    ADMIN_AUTHENTICATE(200, "Continue 2FA authentication"),
    OTP_FAILED(1019, "OTP Invalid")
    ;

    private int code;
    private String message;
}
