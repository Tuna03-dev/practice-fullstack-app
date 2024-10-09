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
    }


}

export default ProductApi