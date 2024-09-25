import httpClient from "./httpClient";


const AuthApi = {
    Login: async (data: any) => {
        return await httpClient.post('/authenticate', data)      
    },

    Register: async (data: any) => {
        return await httpClient.post('/register', data)
    },

    CheckUsername: async (username: String) => {
        return await httpClient.post(`/check-username/${username}`)
    }
}

export default AuthApi;