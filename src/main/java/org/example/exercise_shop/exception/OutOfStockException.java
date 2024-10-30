package org.example.exercise_shop.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OutOfStockException extends RuntimeException{

    private Map<String, String> outOfStockProductsMessage;

    public OutOfStockException(Map<String, String> outOfStockProductsMessage) {
        super("Out of stock");
        this.outOfStockProductsMessage = outOfStockProductsMessage;
    }
}
