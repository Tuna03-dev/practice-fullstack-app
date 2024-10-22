import type { UpdateUserProfileRequest } from "@/apiTypes";
import httpClient from "./httpClient";


const UserApi = {

    getInformation: async (id: string) => {
        return await httpClient.get(`/user/me/${id}`)
    },
    updateUserProfile: async ( updateUserRequest: UpdateUserProfileRequest) =>{
        return await httpClient.put('/user/me/update', updateUserRequest)
    },
    updateAvatar: async (url: string) => {
        return await httpClient.put('/user/me/save-avatar-url', {url})
    }

}

export default UserApi