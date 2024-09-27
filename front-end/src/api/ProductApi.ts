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


}

export default ProductApi