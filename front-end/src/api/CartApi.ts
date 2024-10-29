import type { AddCartRequest, ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const CartApi = {
    addToCart: async (addToCartRequest: AddCartRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/customer/carts/add-to-cart', addToCartRequest);
        return res.data;
    },
    getCart: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/carts');
        return res.data;
    },
    getListCartGroupByShop: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/carts/get-cart');
        return res.data;
    },
    updateCartQuantity: async (id: string, quantity: number): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>(`/customer/carts/update-quantity/${id}`, { quantity });
        return res.data;
    },
    deleteCart: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.delete<ApiResponse<any>>(`/customer/carts/delete/${id}`);
        return res.data;
    },
    clearCart: async (productIds: string[]): Promise<ApiResponse<any>> => {
        const res = await httpClient.delete<ApiResponse<any>>('/customer/carts/clear', { data: productIds });
        return res.data;
    }
};

export default CartApi;
