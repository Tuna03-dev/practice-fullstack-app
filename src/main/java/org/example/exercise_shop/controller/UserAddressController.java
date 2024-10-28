package org.example.exercise_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.AddressService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.AddressCreationRequest;
import org.example.exercise_shop.entity.Address;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user/address")
@RequiredArgsConstructor
@RestController
public class UserAddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ApiResponse<Object> addAddress(@RequestBody AddressCreationRequest addressCreationRequest) {
        try{
            addressService.addAddress(addressCreationRequest);
        }catch (Exception e){
            return ApiResponse.builder()
                    .code(500)
                    .message("Failed to add address")
                    .build();
        }

        return ApiResponse.builder()
                .message("Address added successfully")
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<List<Address>> getAllAddressByUsername(@RequestParam String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(username)) {
            return ApiResponse.<List<Address>>builder()
                    .message("Unauthorized")
                    .build();
        }
        List<Address> addresses = addressService.getAllAddressByUsername(username);
        return ApiResponse.<List<Address>>builder()
                .data(addresses)
                .build();
    }

    @DeleteMapping("/delete/{addressId}")
    public ApiResponse<Object> deleteAddress(@PathVariable String addressId) {
        try{
            addressService.deleteAddress(addressId);
        }catch (Exception e){
            return ApiResponse.builder()
                    .code(500)
                    .message("Failed to delete address")
                    .build();
        }

        return ApiResponse.builder()
                .message("Address deleted successfully")
                .build();
    }

    @PutMapping("/set-default/{addressId}")
    public ApiResponse<Object> setDefaultAddress(@PathVariable String addressId) {
        try{
            addressService.setDefaultAddress(addressId);
        }catch (Exception e){
            return ApiResponse.builder()
                    .code(500)
                    .message("Failed to set default address")
                    .build();
        }

        return ApiResponse.builder()
                .message("Default address set successfully")
                .build();
    }
}
