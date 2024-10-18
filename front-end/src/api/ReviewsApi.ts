import httpClient from "./httpClient";

const ReviewsApi = {

    getAllReviewsByProductId: async (id: string, {page, size, sort}: {page?: number, size?: number, sort?: string}) => {
        return await httpClient.get(`/rate-products/${id}`, {params: {page: page, size: size, sort: sort}})
    }
}

export default ReviewsApi