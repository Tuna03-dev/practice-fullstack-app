import { get } from "@vueuse/core";
import httpClient from "./httpClient";


const ProductApi = {

    getTopBestSellers: async (size: number) => {
        return await httpClient.get(`/customer/products/best-sellers?size=${size}`);
    },
    getTopNewArrivals: async (size: number) => {
        return await httpClient.get(`/customer/products/new-arrival?size=${size}`);
    },
    getTopOfTopRated: async (size: number) => {
        return await httpClient.get(`/customer/products/top-rates?size=${size}`);
    },

    getAllProducts: async ( {page, size, sort, name, categoryId}:{page?: number, size?: number, sort?: string, name?: string, categoryId?: string}) => {
        return await httpClient.get('/customer/products', { params: { page: page, size: size, sort: sort, name: name, category: categoryId } });
    },

    getProductDetails: async (id: string) => {
        return await httpClient.get(`/customer/products/details/${id}`);
    },
    getAllProductByShopId: async ({shopId, page, size, sort, name, categoryId}:{shopId?: string, page?: number, size?: number, sort?: string, name?: string, categoryId?: string}) => {
        return await httpClient.get(`/customer/products/get-by-shopId/${shopId}`, { params: { page: page, size: size, sort: sort, name: name, category: categoryId } });
    }


}

export default ProductApi