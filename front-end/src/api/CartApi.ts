import type { AddCartRequest } from "@/apiTypes";
import httpClient from "./httpClient";


const CartApi = {
    addToCart: async (addToCartRequest: AddCartRequest) => {
        return await httpClient.post('/customer/carts/add-to-cart', addToCartRequest);
    },
    getCart: async () => {
        return await httpClient.get('/customer/carts')
    },
    getListCartGroupByShop: async () => {
        return await httpClient.get('/customer/carts/get-cart')
    },
    updateCartQuantity: async (id: string, quantity: number) => {
        return await httpClient.put('/customer/carts/update-quantity/' + id, {
            quantity: quantity
        })
    },
    deleteCart: async (id: string) => {
        return await httpClient.delete('/customer/carts/delete/' + id)
    },
    clearCart: async (productIds: string[]) => {
        return await httpClient.delete('/customer/carts/clear', {
            data: productIds
        })
    }
}


export default CartApi