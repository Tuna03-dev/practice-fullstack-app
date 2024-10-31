package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.AddressCreationRequest;
import org.example.exercise_shop.entity.Address;

import java.util.List;

public interface AddressService {
    void deleteAddress(String addressId);
    void updateAddress(String addressId, String address);
    Address addAddress(AddressCreationRequest addressCreationRequest);
    void getAddress(String addressId);
    List<Address> getAllAddressByUsername(String username);
    void setDefaultAddress(String addressId);
}
