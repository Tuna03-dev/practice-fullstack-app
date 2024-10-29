import httpClient from "./httpClient";
import type { ApiResponse } from "@/apiTypes";

const AuthApi = {
    Login: async (data: any): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/authenticate', data);
        return res.data;
    },

    Register: async (data: any): Promise<ApiResponse<any>> => {
        const res = await httpClient.post<ApiResponse<any>>('/register', data);
        return res.data;
    },

    CheckUsername: async (username: string): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/check-username?username=${username}`);
        return res.data;
    }
}

export default AuthApi;
