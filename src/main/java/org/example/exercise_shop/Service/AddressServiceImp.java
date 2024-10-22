package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.AddressRepository;
import org.example.exercise_shop.dto.request.AddressCreationRequest;
import org.example.exercise_shop.entity.Address;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.AddressMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImp implements AddressService{

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public void deleteAddress(String addressId) {
//        List<Address> addresses = addressRepository.findAllByUser_UsernameOrderByDefaultAddress(addressId);
//        if (addresses.size() == 1) {
//            throw new ApplicationException(ErrorCode.LIMIT_ADDRESS);
//        }
        addressRepository.deleteById(addressId);
    }

    @Override
    public void updateAddress(String addressId, String address) {

    }

    @Override
    @Transactional
    public void addAddress(AddressCreationRequest addressCreationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        User user = (User) authentication.getPrincipal();
        List<Address> addresses = addressRepository.findAllByUser_Username(user.getUsername());
        if (addresses.size() == 3) {
            throw new ApplicationException(ErrorCode.LIMIT_ADDRESS);
        }
        Address address = addressMapper.toAdress(addressCreationRequest);
        Address defaultAddress = addressRepository.findByDefaultAddressIsTrueAndUser_Username(user.getUsername()).orElse(null);
        address.setUser(user);
        if (defaultAddress == null) {
            address.setDefaultAddress(true);
        }
        addressRepository.save(address);
    }

    @Override
    public void getAddress(String addressId) {

    }

    @Override
    public List<Address> getAllAddressByUsername(String username) {
        return addressRepository.findAllByUser_Username(username);
    }

    @Override
    public void setDefaultAddress(String addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        User user = (User) authentication.getPrincipal();
        List<Address> addresses = addressRepository.findAllByUser_Username(user.getUsername());
        addresses.forEach(address -> {
            if (address.getId().equals(addressId)) {
                address.setDefaultAddress(true);
            } else {
                address.setDefaultAddress(false);
            }
        });
        addressRepository.saveAll(addresses);
    }


}
