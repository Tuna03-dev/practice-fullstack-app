import type { ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ProductApi = {
    getTopBestSellers: async (size: number): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/best-sellers?size=${size}`);
        return res.data;
    },
    getTopNewArrivals: async (size: number): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/new-arrival?size=${size}`);
        return res.data;
    },
    getTopOfTopRated: async (size: number): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/top-rates?size=${size}`);
        return res.data;
    },
    getAllProducts: async ({ page, size, sort, name, categoryId }: { page?: number, size?: number, sort?: string, name?: string, categoryId?: string }): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/customer/products', { params: { page, size, sort, name, category: categoryId } });
        return res.data;
    },
    getProductDetails: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/details/${id}`);
        return res.data;
    },
    getAllProductByShopId: async ({ shopId, page, size, sort, name, categoryId }: { shopId?: string, page?: number, size?: number, sort?: string, name?: string, categoryId?: string }): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/get-by-shopId/${shopId}`, { params: { page, size, sort, name, category: categoryId } });
        return res.data;
    },
    getByShopId: async (shopId: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/customer/products/get-all-by-shop/${shopId}`);
        return res.data;
    }
};

export default ProductApi;
