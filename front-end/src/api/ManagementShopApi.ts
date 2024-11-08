import type { ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ManagementShopApi = {
    getShopInforByUsername: async (username: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/info/get-by-user/${username}`);
        return res.data;
    },
    getOrderDetailByShopId: async (page:number, search: string, status: string, delivery: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/orders?status=${status}&delivery=${delivery}&search=${search}&page=${page}`);
        return res.data;
    },
    getNumberOrderDetailByShopId: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/shop/orders/number`);
        return res.data;
    },
};

export default ManagementShopApi;
