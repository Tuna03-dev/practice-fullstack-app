import type { ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ReviewsApi = {
    getAllReviewsByProductId: async (id: string, { page, size, sort }: { page?: number, size?: number, sort?: string }): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/rate-products/${id}`, { params: { page, size, sort } });
        return res.data;
    }
};

export default ReviewsApi;
