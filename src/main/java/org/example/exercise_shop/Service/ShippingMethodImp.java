package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.ShippingMethodRepository;
import org.example.exercise_shop.entity.ShippingMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingMethodImp implements ShippingMethodService{

    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    public List<ShippingMethod> getAllShippingMethod() {
        return shippingMethodRepository.findAll();
    }
}
