import type { CartResponse } from "@/apiTypes";
import { defineStore } from "pinia";



export const useCartStore = defineStore("cart", {
    state: () => ({
        items: [] as CartResponse[],
        total: 0
    }),

    actions:{
        addItem(item: CartResponse){
            this.items.push(item);
        },
        clearItems(){
            this.items = [];
        },
        setTotal(total: number){
            this.total = total;
        }

    },
    persist: true
})