<template>
    <div class="container mx-auto flex flex-col p-4  gap-6">
        <div class="flex justify-between items-center">
            <h1 class="text-2xl font-bold">Manage orders</h1>
            <div class="flex gap-2">
                
                <Button variant="outline">
                    <Plus class="w-4 h-4 mr-2" />
                    Thêm đơn hàng
                </Button>
                <Button>
                    <FileDown class="w-4 h-4 mr-2" />
                    Chuẩn bị đơn hàng loạt
                </Button>
            </div>
        </div>

        <Card>
            <Tabs v-model="activeTab" class="w-full">
                <TabsList class="w-full grid grid-cols-6 border-b-2 rounded-none h-auto p-0 bg-transparent">
                    <TabsTrigger value="all"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        All
                        
                    </TabsTrigger>
                    <TabsTrigger value="processing"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        Processing
                        <Badge v-if="numberOrderDetail.processing > 0" variant="destructive" class="ml-2">{{ numberOrderDetail.processing }}</Badge>
                    </TabsTrigger>
                    <TabsTrigger value="pending"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        Pending
                        <Badge v-if="numberOrderDetail.pending > 0" variant="destructive" class="ml-2">{{ numberOrderDetail.pending }}</Badge>
                    </TabsTrigger>
                    <TabsTrigger value="shipping"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        Shipping
                        <Badge v-if="numberOrderDetail.shipping > 0" variant="destructive" class="ml-2">{{ numberOrderDetail.shipping }}</Badge>
                    </TabsTrigger>
                    <TabsTrigger value="delivered"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        Delivered
                        <Badge v-if="numberOrderDetail.delivered > 0" variant="destructive" class="ml-2">{{ numberOrderDetail.delivered }}</Badge>

                    </TabsTrigger>
                    <TabsTrigger value="cancelled"
                        class="rounded-none border-b-2 border-transparent data-[state=active]:border-1 data-[state=active]:text-orange-500 data-[state=active]:bg-transparent">
                        Cancelled
                        <Badge v-if="numberOrderDetail.cancelled > 0" variant="destructive" class="ml-2">{{ numberOrderDetail.cancelled }}</Badge>
                    </TabsTrigger>
                </TabsList>

                <div class="p-4 space-y-4">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div class="flex gap-2">
                            <Select >
                                <SelectTrigger class="w-[140px]">
                                    <SelectValue placeholder="Select" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="id">ID Order</SelectItem>
                                    <SelectItem value="product">Product</SelectItem>
                                </SelectContent>
                            </Select>
                            <div class="relative flex-1">
                                <Search class="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                                <Input class="pl-8" placeholder="Enter..."  />
                            </div>
                        </div>

                        

                        <Select >
                            <SelectTrigger>
                                <SelectValue placeholder="Delivery method" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">All</SelectItem>
                                <SelectItem v-for="shippingMethod in deliveries" :key="shippingMethod.id" :value="shippingMethod.id">{{ shippingMethod.provider }} - {{ shippingMethod.name }} </SelectItem>
                                
                            </SelectContent>
                        </Select>
                    </div>

                    <Table class="border border-gray-200">
                        <TableHeader>
                            <TableRow>
                                <TableHead>Items</TableHead>
                                <TableHead>Total Price</TableHead>
                                <TableHead>
                                    Delivery provider
                                    
                                </TableHead>
                                <TableHead>
                                    Order date
                                
                                    
                                </TableHead>
                                <TableHead>Status</TableHead>
                                <TableHead>Actions</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            <TableRow  v-for="shoporder in shopOrders" :key="shoporder.id">
                                <TableCell class="border border-gray-200 flex flex-col gap-2">
                                    <div v-for="product in shoporder.orderItems" :key="product.id" class="flex items-center gap-3 bg-slate-100 p-2 " >
                                        <img :src="product.productImageUrl" alt="Product" width="60" height="60"
                                            class="rounded-md" />
                                        <div>
                                            <div class="font-medium">{{product.productName}}</div>
                                            <div class="text-sm text-muted-foreground">x{{ product.quantity }}</div>
                                        </div>
                                    </div>
                                </TableCell>
                                <TableCell class="border border-gray-200">đ{{ shoporder.totalAmount.toLocaleString() }}</TableCell>
                                <TableCell class="border border-gray-200">{{ shoporder.shippingMethod.provider }} - {{ shoporder.shippingMethod.name }}</TableCell>
                                <TableCell class="border border-gray-200">
                                    {{ moment(shoporder.audit.createdAt).format('DD/MM/YYYY') }}
                                    <div class="text-sm text-muted-foreground">{{ moment(shoporder.audit.createdAt).format('HH:mm') }}</div>
                                </TableCell>
                                <TableCell class="border border-gray-200">
                                    <Badge :variant="getBadgeVariant(shoporder.status)">{{ shoporder.status }}</Badge>
                                </TableCell>
                                <TableCell class="border border-gray-200">
                                    <Button variant="link" class="text-primary">See More</Button>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>

                    <!-- <div class="flex items-center justify-between">
                        <div class="flex items-center gap-2">
                            <Button variant="outline" size="icon" @click="prevPage">
                                <ChevronLeft class="h-4 w-4" />
                            </Button>
                            <Button variant="outline" size="icon" @click="nextPage">
                                <ChevronRight class="h-4 w-4" />
                            </Button>
                            <span class="text-sm text-muted-foreground">
                                Trang 
                            </span>
                        </div>
                        <Select v-model="itemsPerPage">
                            <SelectTrigger class="w-[120px]">
                                <SelectValue />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="10">10 / trang</SelectItem>
                                <SelectItem value="20">20 / trang</SelectItem>
                                <SelectItem value="50">50 / trang</SelectItem>
                                <SelectItem value="100">100 / trang</SelectItem>
                            </SelectContent>
                        </Select>
                    </div> -->
                </div>
            </Tabs>
        </Card>
    </div>
</template>
<script lang="ts" setup>
import moment from 'moment';
import ManagementShopApi from '@/api/ManagementShopApi';
import type { ShippingMethod, ShopOrderResponse } from '@/apiTypes';
import { onMounted, ref } from 'vue';
import { Badge } from '@/components/ui/badge'
import { ChevronLeft, ChevronRight, HelpCircle, Plus, FileDown, Search } from 'lucide-vue-next'
import { Input } from '@/components/ui/input'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@/components/ui/tabs'
import { Button } from '@/components/ui/button'
import ShippingMethodApi from '@/api/ShippingMethodApi';
const shopOrders = ref<ShopOrderResponse[]>([]);
const activeTab = ref("all")
const deliveries = ref<ShippingMethod[]>([]);
interface NumberOrderDetail {
    pending: number;
    shipping: number;
    delivered: number;
    cancelled: number;
    processing: number;
}
const numberOrderDetail = ref<NumberOrderDetail>({
    pending: 0,
    shipping: 0,
    delivered: 0,
    cancelled: 0,
    processing: 0
})
const getBadgeVariant = (status: string) => {
    switch (status) {
        case "PENDING":
            return "secondary";
        case "SHIPPING":
            return "outline";
        case "DELIVERED":
            return "secondary";
        case "CANCELLED":
            return "destructive";
        case "PROCESSING":
            return "default";
        
    }
}

const fetchAllDeliveries = async () => {
    try {
        const response = await ShippingMethodApi.getAllShippingMethods();
        deliveries.value = response.data;
        console.log(response.data);
    } catch (error) {
        console.log(error);
    }
}
const fetchNumberOrderDetail = async () => {
    try {
        const response = await ManagementShopApi.getNumberOrderDetailByShopId();
        numberOrderDetail.value = response.data;
        console.log(response.data);
    } catch (error) {
        console.log(error);
    }
}
const fetchShopOrders = async () => {
    try {
        const response = await ManagementShopApi.getOrderDetailByShopId();
        shopOrders.value = response.data.content;
        
        console.log(response.data);
    } catch (error) {
        console.log(error);
    }

}

onMounted(() => {
    fetchShopOrders();
    fetchNumberOrderDetail();
    fetchAllDeliveries();
})
</script>
<style lang="">

</style>