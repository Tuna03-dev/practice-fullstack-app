import React from 'react'
import axios, {AxiosResponse} from 'axios'
import queryString from 'query-string';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    headers: {
        'Content-Type': 'application/json',
    },
    paramsSerializer: (params) => queryString.stringify(params),
})

axiosClient.interceptors.request.use(
    async (config) => {
        const token = localStorage.getItem('access_token');
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }

);

axiosClient.interceptors.response.use(
    (response: AxiosResponse) => {
        if(response && response.data){
            return response.data;
        }
        return response;
    },
    (error) => {
        console.error('API call error: ', error);
        return Promise.reject(error);
    }


)

export default axiosClient