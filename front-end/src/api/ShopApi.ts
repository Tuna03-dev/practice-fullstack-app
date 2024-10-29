import type { ProductCreationRequest, ShopUpdateRequest, ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ShopApi = {
    getShopdetailsByProductId: async (shopId: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/shops/by-product/' + shopId);
        return res.data;
    },
    getShopdetailsById: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/shops/details/' + id);
        return res.data;
    },
    getRecommedProductsByShopId: async (shopId: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/shops/recommended/' + shopId);
        return res.data;
    },
    getShopInforByUsername: async (username: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/shops/infor/get-by-user/' + username);
        return res.data;
    },
    addNewProduct: async (products: ProductCreationRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/shop/products/add', products);
        return res.data;
    },
    updateProduct: async (products: ProductCreationRequest, id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>('/shop/products/update/' + id, products);
        return res.data;
    },
    updateShopProfile: async (shop: ShopUpdateRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>('/shop/info/update', shop);
        return res.data;
    }
};

export default ShopApi;
