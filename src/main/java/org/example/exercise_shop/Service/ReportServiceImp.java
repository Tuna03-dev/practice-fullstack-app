package org.example.exercise_shop.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.OrderRepository;
import org.example.exercise_shop.Repository.ReportRepository;
import org.example.exercise_shop.Repository.ShopRepository;
import org.example.exercise_shop.dto.response.ReportResponse;
import org.example.exercise_shop.entity.Order;
import org.example.exercise_shop.entity.Report;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.StatusShop;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImp implements ReportService{

    private final ReportRepository reportRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;

    @Scheduled(cron = "0 45 22 * * ?")
    @Override
    public void generateMonthlyReportForAllShop() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        log.info("Starting generate report for all shop {}-{}", month,year);
        List<Shop> allShops = shopRepository.findByStatus(StatusShop.ACTIVE);

        try{
            for (Shop shop: allShops){
                generateMonthlyReportForOneShop(shop, year, month);
            }
            log.info("Finished generate report for all shop {}-{}",month,year);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void generateMonthlyReportForOneShop(Shop shop, int year, int month) {
        log.info("Generating report for Shop: {}, Month: {}, Year {}", shop.getId(),month,year);

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        List<Order> orders = orderRepository.findByShopAndOrderDateBetween(shop, startOfMonth, endOfMonth);
        int totalOrders = orders.size();
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double profitFee = shop.getFeePercentage();
        BigDecimal profit = totalRevenue.subtract(totalRevenue.multiply(BigDecimal.valueOf(profitFee)));

        Report report = Report.builder()
                .shopId(shop.getId())
                .year(year)
                .month(month)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .profit(profit)
                .build();

        reportRepository.save(report);
        log.info("Report generated for Shop: {}, Month: {}, Year: {}", shop.getName(), month, year);
    }

    @Override
    public ReportResponse generateReportByShopAndYearAndMonth(String shopId, int startMonth, int startYear, int endMonth, int endYear) {
        List<Report> reports = reportRepository.findByShopIdAndYearAndMonthBetween(shopId, startMonth, startYear, endMonth, endYear);

        int totalOrders = reports.stream().mapToInt(Report::getTotalOrders).sum();
        BigDecimal totalRevenue = reports.stream().map(Report::getTotalRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = reports.stream().map(Report::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);

        return ReportResponse.builder()
                .totalProfit(totalProfit)
                .shopId(shopId)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .build();
    }

}
