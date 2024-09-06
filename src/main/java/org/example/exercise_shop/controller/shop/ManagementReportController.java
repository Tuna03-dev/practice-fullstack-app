package org.example.exercise_shop.controller.shop;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ReportService;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.dto.response.ReportResponse;
import org.example.exercise_shop.entity.Report;
import org.example.exercise_shop.entity.Shop;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/reports")
public class ManagementReportController {

    private final ShopService shopService;
    private final ReportService reportService;

    @GetMapping("/{shopId}")
    public ApiResponse<ReportResponse> getReports(@PathVariable(value = "shopId")String shopId,
                                                  @RequestParam(value = "start-month", defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}")int startMonth,
                                                  @RequestParam(value = "start-year", defaultValue = "#{T(java.time.LocalDate).now().getYear()}")int startYear,
                                                  @RequestParam(value = "end-month", defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}")int endMonth,
                                                  @RequestParam(value = "end-year", defaultValue = "#{T(java.time.LocalDate).now().getYear()}")int endYear){
        ReportResponse reportResponse = reportService.generateReportByShopAndYearAndMonth(shopId, startMonth, startYear, endMonth, endYear);


        return ApiResponse.<ReportResponse>builder()
                .data(reportResponse)
                .build();
    }

}
