import type { AddCartRequest } from "@/apiTypes";
import httpClient from "./httpClient";


const CartApi = {
    addToCart: async (addToCartRequest: AddCartRequest) => {
        return await httpClient.post('/customer/carts/add-to-cart', addToCartRequest);
    },
    getCart: async () => {
        return await httpClient.get('/customer/carts')
    }
}


export default CartApi