import type { ProductCreationRequest, ShopUpdateRequest } from "@/apiTypes";
import httpClient from "./httpClient";

const ShopApi = {
    getShopdetailsByProductId: async (shopId: string) => {
        return await httpClient.get('/customer/shops/by-product/' + shopId)
    },
    getShopdetailsById: async (id: string) => {
        return await httpClient.get('/customer/shops/details/' + id)
    },
    getRecommedProductsByShopId: async (shopId: string) => {
        return await httpClient.get('/customer/shops/recommended/' + shopId)
    },
    getShopInforByUsername: async (username: string) => {
        return await httpClient.get('/customer/shops/infor/get-by-user/' + username)
    },
    addNewProduct: async (products: ProductCreationRequest) =>{
        return await httpClient.post('/shop/products/add', products)
    },
    updateProduct: async (products: ProductCreationRequest, id: string) =>{
        return await httpClient.put('/shop/products/update/' + id, products)
    },
    updateShopProfile: async (shop: ShopUpdateRequest) =>{
        return await httpClient.put('/shop/info/update', shop)
    },
    

}

export default ShopApi;