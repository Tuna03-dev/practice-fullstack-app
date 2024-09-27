import { defineStore, setActivePinia } from "pinia";

export const useAuthStore =defineStore('auth',{
    
    state: () => ({
        accessToken: '',
        userRole: '',
        isLoggedIn:false
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
            localStorage.removeItem('access_token');
        },
        setIsLoggedIn(loggedIn: boolean){
            this.isLoggedIn = loggedIn
        },
        setUserRole(role: string){
            this.userRole = role
        },
        logout(){
            this.clearAccessToken();
            this.isLoggedIn = false
        },

        restoreSession() {
            const token = localStorage.getItem('access_token');
            if (token) {
                this.setAccessToken(token); 
            }
        }

    }



})