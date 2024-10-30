import type { ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const ShippingMethodApi = {
    getAllShippingMethods: async (): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>('/shipping-method');
        return res.data;
    }

}

export default ShippingMethodApi;