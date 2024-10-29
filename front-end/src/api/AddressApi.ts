import type { AddressCreationRequest, ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const AddressApi = {
    getAddresses: async (username: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/user/address/get?username=${username}`);
        return res.data;
    },

    addAddress: async (creationAddress: AddressCreationRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>("/user/address/add", creationAddress);
        return res.data;
    },

    deleteAddress: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.delete<ApiResponse<any>>(`/user/address/delete/${id}`);
        return res.data;
    },

    setDefault: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>(`/user/address/set-default/${id}`);
        return res.data;
    }
};

export default AddressApi;
