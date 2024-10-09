import { defineStore, setActivePinia } from "pinia";

export const useAuthStore =defineStore('auth',{
    
    state: () => ({
        accessToken: '',
        userRole: '',
        isLoggedIn:false,
        username: '',
    }),
    actions:{
        setAccessToken(token: string){
            this.accessToken = token;
            this.isLoggedIn = !!token;
            localStorage.setItem("accessToken", token);
        },
        clearAccessToken(){
            this.accessToken = "";
            this.isLoggedIn = false; 
            localStorage.removeItem('accessToken');
        },
        setIsLoggedIn(loggedIn: boolean){
            this.isLoggedIn = loggedIn
        },
        setUserRole(role: string){
            this.userRole = role
            localStorage.setItem('userRole', role);
        },
        setUsername(username: string){
            this.username = username;
            localStorage.setItem('username', username);
        },
        logout(){
            this.clearAccessToken();
            this.isLoggedIn = false
        },

        restoreSession() {
            const token = localStorage.getItem('accessToken');
            const role = localStorage.getItem('userRole');
            const username = localStorage.getItem('username');
            if (token && role && username) {
                this.setAccessToken(token); 
                this.setUserRole(role);
                this.setUsername(username);
            }
        }

    },
    getters:{
        isAdmin: (state) => state.userRole === 'ADMIN',
        isCustomer: (state) => state.userRole === 'CUSTOMER',
        isShop: (state) => state.userRole === 'SHOP',

    }



})