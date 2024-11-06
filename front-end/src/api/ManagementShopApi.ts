import type { ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ManagementShopApi = {
    getShopInforByUsername: async (username: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/info/get-by-user/${username}`);
        return res.data;
    },
    getOrderDetailByShopId: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/orders`);
        return res.data;
    },
    getNumberOrderDetailByShopId: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/orders/number`);
        return res.data;
    },
};

export default ManagementShopApi;
