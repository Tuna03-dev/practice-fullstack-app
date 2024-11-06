<template>
    <div class="container mx-auto p-4">

        <div class="flex items-center gap-4">
            <input
                type="text"
                v-model="searchQuery"
                @input="fetchOrdersWithType(selectedType, 1)"
                placeholder="Search orders..."
                class="w-1/2 px-4 py-2 border rounded"
            />
            <div class="grid w-full grid-cols-5 bg-slate-100 rounded-lg text-center transition-all">
                <div @click="fetchOrdersWithType('all', 1)" :class="tabClass('all')" class="px-4 p-3">All</div>
                <div @click="fetchOrdersWithType('pending', 1)" :class="tabClass('pending')" class="px-4 p-3">Pending</div>
                <div @click="fetchOrdersWithType('delivering', 1)" :class="tabClass('delivering')" class="px-4 p-3">Delivering</div>
                <div @click="fetchOrdersWithType('delivered', 1)" :class="tabClass('delivered')" class="px-4 p-3">Completed</div>
                <div @click="fetchOrdersWithType('cancelled', 1)" :class="tabClass('cancelled')" class="px-4 p-3">Cancelled</div>
            </div>
        </div>

        <div  class="mt-6 space-y-6">

            <div v-for="shoporder in orderResponses" :key="shoporder.id" class="rounded-lg border">
                <div class="border-b p-4">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center space-x-2">
                            <span v-if="shoporder.shopInformationResponse.favourite"
                                class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">FAVOURITE</span>
                            <span class="font-medium  text-lg">{{ shoporder.shopInformationResponse.name
                                }}</span>

                            <Button variant="ghost" size="sm" class="h-8">
                                <Store class="h-4 w-4 mr-2" />
                                <router-link :to="`/stores/${shoporder.shopInformationResponse.id}`">View
                                    Shop</router-link>
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
                        <img :src="product.productImageUrl" alt="Product" width="80" height="80"
                            class="rounded-md hover:cursor-pointer"
                            @click="handleShowProductDetail(product.productId)" />
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
                                <span class="text-lg font-medium text-orange-500">₫{{
                                    shoporder.totalAmount.toLocaleString() }}</span>
                            </div>
                            <div class="space-x-2">
                                <AlertDialog>
                                    <AlertDialogTrigger as-child>
                                        <Button v-if="shoporder.status !== 'CANCELLED'" variant="destructive">Cancel
                                            Order</Button>

                                    </AlertDialogTrigger>
                                    <AlertDialogContent>
                                        <AlertDialogHeader>
                                            <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                                            <AlertDialogDescription>
                                                This action cannot be undone.
                                            </AlertDialogDescription>
                                        </AlertDialogHeader>
                                        <AlertDialogFooter>
                                            <AlertDialogCancel>Cancel</AlertDialogCancel>
                                            <AlertDialogAction @click="handleCancelOrder(shoporder.id)">Continue
                                            </AlertDialogAction>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>
                                </AlertDialog>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <Pagination v-if="totalPage > 1" class="mt-6 flex justify-center" v-slot="{ page }" :total="totalPage * 10" :sibling-count="1"
            show-edges :default-page="1">
            <PaginationList v-slot="{ items }" class="flex items-center gap-1">
                <PaginationFirst @click="navigateToPage(1)" />
                <PaginationPrev @click="navigateToPage(page - 1)" />

                <template v-for="(item, index) in items">
                    <PaginationListItem v-if="item.type === 'page'" :key="index" :value="item.value" as-child>
                        <Button class="w-10 h-10 p-0" :variant="item.value === page ? 'default' : 'outline'"
                            @click="navigateToPage(item.value)">
                            {{ item.value }}
                        </Button>
                    </PaginationListItem>
                    <PaginationEllipsis v-else :key="item.type" :index="index" />
                </template>

                <PaginationNext @click="navigateToPage(page + 1)" />
                <PaginationLast @click="navigateToPage(totalPage)" />
            </PaginationList>
        </Pagination>
        <div v-if="orderResponses.length === 0" class="flex items-center justify-center h-[200px]">
            <MessageCircle class="h-12 w-12 text-muted-foreground" />
            <p class="text-xl text-muted-foreground">No orders found</p>
        </div>
    </div>
</template>

<script lang="ts" setup>
import {
    Pagination,
    PaginationEllipsis,
    PaginationFirst,
    PaginationLast,
    PaginationList,
    PaginationListItem,
    PaginationNext,
    PaginationPrev,
} from '@/components/ui/pagination'
import { onMounted, ref } from 'vue'
import { MessageCircle, Store, X } from 'lucide-vue-next'
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Button } from "@/components/ui/button"
import type { OrderResponseType, ShopOrderResponse } from '@/apiTypes';
import OrderApi from '@/api/OrderApi';
import moment from 'moment';
import { useRouter } from 'vue-router';
import { toast } from 'vue-sonner';
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
const orderResponses = ref<ShopOrderResponse[]>([]);
const router = useRouter();
const totalPage = ref(0);
const selectedType = ref('all');
const searchQuery = ref('')

const fetchOrdersWithType = async (type: string = 'all', page: number = 1) => {
    selectedType.value = type;
    try {
        const response = await OrderApi.getOrderByCurrentUser(page - 1, type, searchQuery.value);
        if (response.code === 200) {
            orderResponses.value = response.data.content;
            totalPage.value = response.data.totalPages;
        }
    } catch (error) {
        console.error(error);
    }
}

const tabClass = (type: string) => {
    return [
        "px-4 p-3 hover:cursor-pointer",
        type === selectedType.value ? "bg-orange-300 text-white rounded-lg" : "hover:bg-orange-300 hover:text-white hover:rounded-lg"
    ];
};

const navigateToPage = (page: number) => {
    if (page > 0 && page <= totalPage.value) {
        fetchOrdersWithType(selectedType.value,page);
    }
}

const handleShowProductDetail = (productId: string) => {
    router.push(`/products/details/${productId}`);
}

const handleCancelOrder = async (orderId: string) => {
    try {
        const response = await OrderApi.cancelOrder(orderId);
        if (response.code === 200) {
            toast.success('Order cancelled successfully');
            fetchOrdersWithType();
        }
    } catch (error) {
        toast.error('Order cannot be cancelled');
        console.error(error);
    }
}

onMounted(() => {
    fetchOrdersWithType();
})
</script>

<style scoped></style>