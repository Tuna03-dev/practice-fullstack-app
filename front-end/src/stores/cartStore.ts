import type { CartResponse } from "@/apiTypes";
import { defineStore } from "pinia";



export const useCartStore = defineStore("cart", {
    state: () => ({
        items: [] as CartResponse[],
        total: 0,
        isOrder: false,
        shippingFee: 0,
        productAmount: 0
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
        },
        setIsOrder(isOrder: boolean){
            this.isOrder = isOrder;
        },
        setShippingFee(shippingFee: number){
            this.shippingFee = shippingFee;
        },
        setProductAmount(productAmount: number){
            this.productAmount = productAmount;
        },

    },
    persist: true
})