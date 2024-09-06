package org.example.exercise_shop.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.ShopFeeRepository;
import org.example.exercise_shop.Repository.ShopRepository;
import org.example.exercise_shop.entity.ShopFee;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfitFeeScheduler {
    private final ShopFeeRepository shopFeeRepository;
    private final ShopRepository shopRepository;

    @Scheduled(cron = "0 14 21 * * *")
    public void applyScheduledProfitFee(){
        log.info("Schedule task running....");
        LocalDate today = LocalDate.now();
        ShopFee currentShopfee = shopFeeRepository.findAll().getFirst();
        if (currentShopfee.getEffectiveDate().isEqual(today)){
            shopRepository.updateAllByFeePercentage(currentShopfee.getPercentage());
        }
    }
}
