import type { ApiResponse } from '@/apiTypes'
import httpClient from './httpClient'

const CategoryApi = {
  getAllCategories: async (): Promise<ApiResponse<any>> => {
    const res = await httpClient.get<ApiResponse<any>>('/categories')
    return res.data
  },
  getAllCategoriesByShopId: async (shopId: string): Promise<ApiResponse<any>> => {
    const res = await httpClient.get<ApiResponse<any>>(`/categories/get-by-shop/${shopId}`)
    return res.data
  }
}

export default CategoryApi
