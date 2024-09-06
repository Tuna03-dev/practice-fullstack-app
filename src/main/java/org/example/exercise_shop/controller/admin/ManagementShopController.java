package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.request.ShopCreationRequest;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.StatusShop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/shops")
public class ManagementShopController {
    private final ShopService shopService;
    private final AuthenticationManager authenticationManager;

    @GetMapping()
    public ApiResponse<List<Shop>> showAllShops(){
        List<Shop> shops = shopService.getShops();
        return ApiResponse.<List<Shop>>builder()
                .data(shops).build();
    }



    @GetMapping("/{id}/details")
    public ApiResponse<Shop> getShopDetails(@PathVariable String id){
        Shop shop = shopService.getShopById(id);
        return ApiResponse.<Shop>builder()
                .data(shop)
                .build();
    }

    @PostMapping("/re-authenticate")
    public ApiResponse<String> reAuthentication(@RequestBody Map<String, String> request){
        String password = request.get("password");
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        String username = current.getName();
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ApiResponse.<String>builder()
                    .data("Re-authenticate successful.")
                    .build();
        }catch (AuthenticationException e){
            return ApiResponse.<String>builder()
                    .data("Authenticate failed.")
                    .code(403)
                    .build();
        }
    }


    @PutMapping("/close")
    public ApiResponse<String> closeShop(@RequestBody Map<String,String> request){
        String shopId = request.get("id");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            return ApiResponse.<String>builder()
                    .data("Please re-authenticate")
                    .code(403)
                    .build();
        }

        shopService.updateShopStatus(shopId, StatusShop.CANCELLED);
        return ApiResponse.<String>builder()
                .data("Shop closed successfully")
                .build();
    }

    @PutMapping("/open")
    public ApiResponse<String> openShop(@RequestBody Map<String,String> request){
        String shopId = request.get("id");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            return ApiResponse.<String>builder()
                    .data("Please re-authenticate")
                    .code(403)
                    .build();
        }

        shopService.updateShopStatus(shopId, StatusShop.ACTIVE);
        return ApiResponse.<String>builder()
                .data("Shop opened successfully")
                .build();
    }



}
