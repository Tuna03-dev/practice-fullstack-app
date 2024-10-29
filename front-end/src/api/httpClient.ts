import axios, { type AxiosResponse } from "axios";
import { useAuthStore } from "@/stores/authStore";
import router from "@/router";
import { toast } from "vue-sonner";
import type { ApiResponse } from "@/apiTypes";

const httpClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers:{
        "Content-Type": "application/json",
    },
    withCredentials: true
});

httpClient.interceptors.request.use(
    async (config) => {
        const authStore = useAuthStore();
        const token =authStore.accessToken;
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }

);

httpClient.interceptors.response.use(
    (response: AxiosResponse<ApiResponse<any>>): AxiosResponse<ApiResponse<any>> => {
        return response;
      },
    async(error) => {
        if (error.response) {
            const { status, data } = error.response;
            console.error(`API error: ${status}`, data);
            
            const authStore = useAuthStore();
            const originalRequest = error.config;
            
            if(error.response && (error.response.status === 401 || error.response.status === 403)  && !originalRequest._retry){
                originalRequest._retry = true;
                try{
                    const newTokens = await httpClient.post("/refresh", null, {withCredentials: true});
                    if(newTokens){
                        authStore.setAccessToken(newTokens.data.data.accessToken);
                        originalRequest.headers.Authorization = `Bearer ${newTokens.data.data.accessToken}`;

                    }
                    return httpClient(originalRequest);

                }catch(error){
                    console.log("Refresh token error:", error);
                    authStore.logout();
                    router.push("/");
                    toast.error("Your session has expired. Please login again.");
                }

            }


            return Promise.reject(data);
        }

        return Promise.reject(error);
    }
    
)

export default httpClient;