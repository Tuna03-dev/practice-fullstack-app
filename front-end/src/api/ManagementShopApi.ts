import httpClient from "./httpClient";

const ManagementShopApi = {
    getShopInforByUsername: async (username: string) => {
        return await httpClient.get('/shop/info/get-by-user/' + username)
    }
    

}

export default ManagementShopApi;