package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.ShopFeeRepository;
import org.example.exercise_shop.entity.ShopFee;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShopFeeServiceImp implements ShopFeeService{

    private final ShopFeeRepository shopFeeRepository;

    @Override
    @Transactional
    public ShopFee addShopFee(ShopFee shopFee) {
        if (shopFeeRepository.count() > 0){
             shopFeeRepository.updateAllByPercentageAndEffectiveDate(shopFee.getPercentage(), shopFee.getEffectiveDate());
             return shopFeeRepository.findAll().getFirst();
        }
        return shopFeeRepository.save(shopFee);
    }

    @Override
    public ShopFee getCurrentShopFee() {

        return shopFeeRepository.findAll().getFirst();
    }


}
