import type { UpdateUserProfileRequest, ApiResponse } from "@/apiTypes";
import httpClient from "./httpClient";

const UserApi = {
    getInformation: async (id: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/user/me/${id}`);
        return res.data;
    },
    updateUserProfile: async (updateUserRequest: UpdateUserProfileRequest): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>('/user/me/update', updateUserRequest);
        return res.data;
    },
    updateAvatar: async (url: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.put<ApiResponse<any>>('/user/me/save-avatar-url', { url });
        return res.data;
    }
};

export default UserApi;
