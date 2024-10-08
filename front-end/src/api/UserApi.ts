import httpClient from "./httpClient";


const UserApi = {

    getInformation: async (id: string) => {
        return await httpClient.get(`/user/me/${id}`)
    },

}

export default UserApi