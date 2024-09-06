package org.example.exercise_shop.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.dto.response.CartItemResponse;
import org.example.exercise_shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    AddToCartRequest cartItemToAddToCartRequest(CartItem cartItem);
    CartItem addToCartRequestToCartItem(AddToCartRequest addToCartRequest);
    CartItemResponse cartItemsToCartItemResponse(CartItem cartItem);
    CartItemResponse addToCartRequestsToCartItemResponse(AddToCartRequest addToCartRequest);


    default String cartItemsToJson(List<AddToCartRequest> cartItems){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(cartItems);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert cart items to json",e);
        }
    }

    default List<AddToCartRequest> jsonToCartItems(String cartData) {
        if (cartData == null) {
            return new ArrayList<>();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return new ArrayList<>(Arrays.asList(mapper.readValue(cartData, AddToCartRequest[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to cart items", e);
        }
    }

}
