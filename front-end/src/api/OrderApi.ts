import type { ApiResponse, OrderCreationRequest } from "@/apiTypes";
import httpClient from "./httpClient";

const OrderApi = {
    createOrder: async (orderCreationRequest: OrderCreationRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/customer/checkout/create-order', orderCreationRequest);
        return res.data;
    },
    getOrderByCurrentUser: async (page: number, type: string, search: string) : Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/order-histories'+`?page=${page}&type=${type}&search=${search}`);
        return res.data;
    },
    cancelOrder: async (shopOrderId: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>(`/customer/order-histories/${shopOrderId}/cancel`);
        return res.data;
    }
}
export default OrderApi