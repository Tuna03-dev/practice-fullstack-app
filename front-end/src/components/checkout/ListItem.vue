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
                    <div>
                        <shipping-component
                            @method-selected="(method: ShippingMethod) => updateShippingMethod(shop.shopId, method)"></shipping-component>
                    </div>
                    <div>
                        <div class="flex gap-2 text-lg font-medium text-gray-500  ">
                            <p class="w-1/5">Product amount:</p>
                            <p>₫{{ shop.totalAmount.toLocaleString() }}</p>
                        </div>
                        <div class="flex gap-2 text-lg font-medium text-gray-500 ">
                            <p class="w-1/5">Shipping fee:</p>
                            <p>₫{{ getShippingFee(shop.shopId).toLocaleString() }}</p>
                        </div>
                        <div class="flex gap-2 text-lg font-medium text-gray-500 ">
                            <p class="w-1/5">Total amount:</p>
                            <p>₫{{ getTotalAmount(shop.shopId).toLocaleString() }}</p>
                        </div>
                    </div>
                </CardContent>
                <CardFooter>
                    <div class="flex gap-2 text-xl font-bold ml-auto mr-3">
                        <p>Total amount:</p>
                        <p class="text-orange-500">₫{{ getToTalPriceOfOrder().toLocaleString() }}</p>
                    </div>
                </CardFooter>
            </Card>

        </div>
    </div>

</template>
<script lang="ts" setup>
import ShippingComponent from './ShippingComponent.vue';
import type { CartResponse, ShippingMethod, ShopOrderRequest } from '@/apiTypes';
import { computed, onMounted, ref, watch } from 'vue';
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
import { Button } from '@/components/ui/button';

const cartStore = useCartStore();
const totalPrice = ref(0)
const cartReponses = ref<CartResponse[]>([])
const router = useRouter();
const shippingMethodsMap = ref<{ [key: string]: ShippingMethod | null }>({});
const shopOrderList = ref<ShopOrderRequest[]>([]);
const emit = defineEmits(['on-select-shipping'])
const shoppingFee = computed(() => {
    return Object.values(shippingMethodsMap.value).filter((method) => method !== null).reduce((acc, method) => {
        return acc + Number(method?.cost);
    }, 0)
})


const allShippingSelected = computed(() => {
    return cartReponses.value.every((shop) => shippingMethodsMap.value[shop.shopId]);
})

const handleShowProductDetail = (productId: string) => {
    router.push(`/products/details/${productId}`)
}

const updateShippingMethod = (shopId: string, method: ShippingMethod) => {
    shippingMethodsMap.value[shopId] = method;
}

const getShippingFee = (shopId: string) => {
    const method = shippingMethodsMap.value[shopId];
    cartStore.setShippingFee(shoppingFee.value)
    return method ? method.cost : 0;
}

const getTotalAmount = (shopId: string) => {
    const method = shippingMethodsMap.value[shopId];
    const productPrice = cartReponses.value.find((shop) => shop.shopId === shopId)?.totalAmount
    const price = productPrice ?? 0;
    return method ? Number(method.cost) + Number(price) : Number(price);
}

const getToTalPriceOfOrder = () => {
    let total = 0;
    for (const shop of cartReponses.value) {
        const method = shippingMethodsMap.value[shop.shopId];
        const productPrice = shop.totalAmount
        const price = productPrice ?? 0;
        total += method ? Number(method.cost) + Number(price) : Number(price);
    }
    cartStore.setTotal(total)
    return total;
}

watch(shippingMethodsMap,
    () => {
        shopOrderList.value = cartReponses.value.map((shop) => {
            return {
                shopId: shop.shopId,
                shippingMethodId: shippingMethodsMap.value[shop.shopId]?.id ?? '',
                timeDelivery: shippingMethodsMap.value[shop.shopId]?.estimatedDays ?? 0,
                totalAmount: getTotalAmount(shop.shopId),
                cartItemIds: shop.cartItemResponses.map((cartItem) => cartItem.id)
            }
        })
        console.log(shopOrderList.value)
        emit('on-select-shipping', {
            isSelectedAllShipping: allShippingSelected.value,
            shopOrderList: shopOrderList.value,

        });
    },
    {
        deep: true
    }
)


onMounted(() => {
    console.log(cartStore.items)
    cartReponses.value = cartStore.items
    totalPrice.value = cartStore.total
    const productPrice = cartReponses.value.map((shop) => Number(shop.totalAmount)).reduce((acc, price) => acc + price, 0)
    cartStore.setProductAmount(productPrice)
})
</script>
<style lang="">

</style>