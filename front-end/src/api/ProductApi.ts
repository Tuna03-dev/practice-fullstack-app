import httpClient from "./httpClient";


const ProductApi = {

    getTopBestSellers: async (size: number) => {
        return await httpClient.get(`/customer/products/best-sellers?size=${size}`);
    },

}

export default ProductApi