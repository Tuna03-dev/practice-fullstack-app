package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.response.ReportResponse;
import org.example.exercise_shop.entity.Shop;

public interface ReportService {

    void generateMonthlyReportForAllShop();
    void generateMonthlyReportForOneShop(Shop shop, int year, int month);
    ReportResponse generateReportByShopAndYearAndMonth(String ShopId, int startMonth, int startYear, int endMonth, int endYear);
}
