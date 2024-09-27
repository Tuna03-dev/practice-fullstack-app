import httpClient from "./httpClient";

const CategoryApi = {
    getAllCategories: async () => {
        return await httpClient.get('/categories')
    }
}

export default CategoryApi