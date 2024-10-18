import httpClient from "./httpClient";

const CategoryApi = {
    getAllCategories: async () => {
        return await httpClient.get('/categories')
    },
    getAllCategorisByShopId: async (shopId: string) => {
        return await httpClient.get(`/categories/get-by-shop/${shopId}`)
    }
}

export default CategoryApi