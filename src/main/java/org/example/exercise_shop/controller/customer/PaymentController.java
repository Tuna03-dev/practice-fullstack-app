package org.example.exercise_shop.controller.customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.config.ConfigVNPayment;
import org.example.exercise_shop.dto.request.OrderRequest;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.dto.response.CheckoutResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/customer/checkout")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<CheckoutResponse> createPayment(@RequestBody OrderRequest orderRequest,
                                                       HttpServletRequest request
                                ) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        BigDecimal amount = (orderService.handlerCheckoutInfor(orderRequest)).multiply(BigDecimal.valueOf(100)).stripTrailingZeros();

        HttpSession session = request.getSession();
        session.setAttribute("orderRequest", orderRequest);




        String vnp_TxnRef = ConfigVNPayment.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = ConfigVNPayment.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amount.toPlainString());
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_ReturnUrl", ConfigVNPayment.vnp_ReturnUrl);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVNPayment.hmacSHA512(ConfigVNPayment.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = ConfigVNPayment.vnp_PayUrl + "?" + queryUrl;
        CheckoutResponse checkoutResponse = CheckoutResponse.builder()
                .paymentUrl(paymentUrl)
                .build();

        return ApiResponse.<CheckoutResponse>builder()
                .data(checkoutResponse)
                .build();
    }

    @GetMapping("/success")
    public ApiResponse<?> successPayment(HttpServletRequest request,@RequestParam(name = "vnp_Amount")BigDecimal totalAmount){
        HttpSession session = request.getSession();
        OrderRequest orderRequest = (OrderRequest) session.getAttribute("orderRequest");
        orderService.saveOrder(orderRequest, totalAmount);

        return ApiResponse.builder()
                .message("Create Order Successfully")
                .build();
    }

}
