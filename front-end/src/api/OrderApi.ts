import type { ApiResponse, OrderCreationRequest } from "@/apiTypes";
import httpClient from "./httpClient";

const OrderApi = {
    createOrder: async (orderCreationRequest: OrderCreationRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/customer/checkout/create-order', orderCreationRequest);
        return res.data;
    }
}

export default OrderApi