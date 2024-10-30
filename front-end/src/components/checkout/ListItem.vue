<template>

    <div class="w-full">
        <div v-if="cartReponses.length">
            <Card>
                <CardContent v-for="shop in cartReponses" :key="shop.shopId" class="mb-6 ">
                    <div class="flex gap-2 justify-start items-center">
                        <Store></Store>
                        <div class="font-bold text-2xl my-6">{{ shop.shopName }}</div>
                    </div>

                    <div class="flex justify-between mb-4 bg-gray-50 p-5" v-for="product in shop.cartItemResponses"
                        :key="product.productId">

                        <div class="flex items-start  gap-10 hover:cursor-pointer"
                            @click="handleShowProductDetail(product.productId)">
                            <img class="w-28" :src="product.productImage" :alt="product.productName" />
                            <div class="flex flex-col justify-between items-start h-full">
                                <p class="text-lg font-medium text-orange-500">{{ product.productName }}</p>
                                <p class="text-gray-500">₫{{ product.pricePerProduct.toLocaleString() }}</p>
                            </div>
                        </div>
                        <div class="flex items-center justify-center text-gray-500">
                            <X class="w-5 h-5"></X>
                            <p class="text-xl ">{{ product.quantity }}</p>
                        </div>
                    </div>
                </CardContent>
                <CardFooter>
                    <div class="flex gap-2 text-xl font-bold ml-auto mr-3">
                        <p>Total amount:</p>
                        <p class="text-orange-500">₫{{ totalPrice.toLocaleString() }}</p>
                    </div>
                </CardFooter>
            </Card>

        </div>
    </div>

</template>
<script lang="ts" setup>

import type { CartResponse } from '@/apiTypes';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cartStore';
import { X, Store } from 'lucide-vue-next';
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle
} from '@/components/ui/card'
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from '@/components/ui/table'
import { Button } from '@/components/ui/button'

const cartStore = useCartStore();
const totalPrice = ref(0)
const cartReponses = ref<CartResponse[]>([])
const router = useRouter();

const handleShowProductDetail = (productId: string) => {
    router.push(`/products/details/${productId}`)
}

onMounted(() => {
    console.log(cartStore.items)
    cartReponses.value = cartStore.items
    totalPrice.value = cartStore.total
})
</script>
<style lang="">

</style>