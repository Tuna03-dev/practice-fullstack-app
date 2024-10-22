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
    }
    

}

export default ShopApi;