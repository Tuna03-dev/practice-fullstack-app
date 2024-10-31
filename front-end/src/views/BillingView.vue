<template>
    <div class="container mx-auto p-4">
        <Tabs default-value="all" class="w-full">
            <TabsList class="grid w-full grid-cols-5">
                <TabsTrigger value="all" class="px-4">All</TabsTrigger>
                <TabsTrigger value="pending" class="px-4">Pending</TabsTrigger>
                <TabsTrigger value="delivering" class="px-4">Delivering</TabsTrigger>
                <TabsTrigger value="completed" class="px-4">Completed</TabsTrigger>
                <TabsTrigger value="cancelled" class="px-4">Cancelled</TabsTrigger>
            </TabsList>

            <TabsContent value="all" class="mt-6 space-y-6">
                <div v-for="order in orderResponses" :key="order.id" class="flex flex-col gap-6">

                    <div v-for="shoporder in order.shopOrderResponses" :key="shoporder.id" class="rounded-lg border">
                        <div class="border-b p-4">
                            <div class="flex items-center justify-between">
                                <div class="flex items-center space-x-2">
                                    <span v-if="shoporder.shopInformationResponse.favourite" class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">FAVOURITE</span>
                                    <span class="font-medium  text-lg">{{ shoporder.shopInformationResponse.name }}</span>

                                    <Button variant="ghost" size="sm" class="h-8">
                                        <Store class="h-4 w-4 mr-2" />
                                        <router-link :to="`/stores/${shoporder.shopInformationResponse.id}`">View Shop</router-link>
                                    </Button>
                                </div>
                                <span v-if="shoporder.status === 'SHIPPING'" class="text-orange-500">Delivering</span>
                                <span v-if="shoporder.status === 'PROCESSING'" class="text-orange-500">Processing</span>
                                <span v-if="shoporder.status === 'DELIVERED'" class="text-green-500">Delivered</span>
                                <span v-if="shoporder.status === 'PENDING'" class="text-orange-500">Pending</span>
                                <span v-if="shoporder.status === 'CANCELLED'" class="text-red-500">Cancelled</span>

                            </div>
                        </div>

                        <div v-for="product in shoporder.orderItems" :key="product.id" class="p-4">
                            <div class="flex gap-4">
                                <img :src="product.productImageUrl" alt="Product" width="80" height="80" class="rounded-md hover:cursor-pointer" @click="handleShowProductDetail(product.productId)" />
                                <div class="flex-1">
                                    <h3 class="font-medium">{{ product.productName }}</h3>
                                    <div class="flex items-center">
                                        <X class="h-4 w-4 "></X>
                                        <p class="text-sm">{{ product.quantity }}</p>
                                    </div>
                                </div>
                                <div class="text-right">
                                    <p class="text-sm">₫{{ product.totalAmount.toLocaleString() }}</p>
                                </div>
                            </div>
                        </div>

                        <div class="border-t p-4">
                            <div class="flex items-center justify-between">
                                <p class="text-sm text-muted-foreground">Orders will be prepared and shipped before
                                    {{ moment(shoporder.estimatedDeliveryTime).format('DD/MM/YYYY') }}</p>
                                <div class="flex items-center gap-4">
                                    <div class="flex items-center">
                                        <span class="text-sm mr-2">Total amount:</span>
                                        <span class="text-lg font-medium text-orange-500">₫{{ shoporder.totalAmount.toLocaleString() }}</span>
                                    </div>
                                    <div class="space-x-2">
                                        <Button variant="destructive">Cancel Order</Button>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>



                </div>
            </TabsContent>
        </Tabs>
    </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { MessageCircle, Store, X } from 'lucide-vue-next'
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Button } from "@/components/ui/button"
import type { OrderResponseType } from '@/apiTypes';
import OrderApi from '@/api/OrderApi';
import moment from 'moment';
import { useRouter } from 'vue-router';

const orderResponses = ref < OrderResponseType[] > ([]);
const router = useRouter();
const fetchAllOrder = async () => {
    try {
        const response = await OrderApi.getOrderByCurrentUser();
        if (response.code === 200) {
            orderResponses.value = response.data.content;
        }
        console.log(orderResponses.value);
    } catch (error) {
        console.error(error);
    }
}

const handleShowProductDetail = (productId: string) => {
    router.push(`/products/details/${productId}`);
}

onMounted(() => {
    fetchAllOrder();
})
</script>

<style scoped>
/* Add any additional styles here */
</style>