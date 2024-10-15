import httpClient from "./httpClient";

const ShopApi = {
    getShopdetails: async (shopId: string) => {
        return await httpClient.get('/customer/shops/details/' + shopId)
    }
    

}

export default ShopApi;