<template >
  <div
    class="relative hover:cursor-pointer hover:text-gray-500 rounded-3xl p-3"
    @mouseenter="isOpen = true"
    @mouseleave="isOpen = false"
  >
    <ShoppingCart />
    <span
      className="absolute top-0 -right-1 bg-red-500 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center"
    >
      {{cartItems ? cartItems.length : 0 }}
    </span>
    <div
      v-if="isOpen"
      className="absolute z-10 top-full right-0  w-64 bg-white rounded-lg shadow-lg border-[1px] border-gray-200 overflow-hidden "
      @mouseenter="isOpen = true"
      @mouseleave="isOpen = false"
    >
      <div  className="">
        <div className="p-4">
          <h3 className="text-lg font-semibold mb-2">Recently Added Products</h3>
          
          <ScrollArea v-if="cartItems.length > 0"  class="h-[200px]">
            <div
            v-for="cartItem in props.cartItems" :key="cartItem.productId"
              className="flex items-center gap-2 mb-2 hover:bg-gray-100 p-2 rounded transition-colors duration-200"
            >
              <img :src="cartItem.productImage" alt="" className="w-10 h-10 object-cover rounded" />
              <div>
                <p className="text-sm font-medium">{{ cartItem.productName }}</p>
                <p className="text-xs text-orange-500">₫{{ cartItem.pricePerProduct }}</p>
              </div>
            </div>
          </ScrollArea>
          <div v-else class="h-fit">
            <p className="text-center text-gray-400 bg-gray-100 rounded-lg p-3 ">No items found</p>
          </div>
        </div>
      </div>
      <div className="p-4 bg-gray-100">
        <Button className="w-full bg-orange-500 hover:bg-orange-600 text-white rounded-sm">
          View My Shopping Cart
        </Button>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Car, ShoppingCart } from 'lucide-vue-next'
import { ref } from 'vue'
import { Button } from '../ui/button';
import { string } from 'zod';
import type { CartItemResponse } from '@/apiTypes';
import { ScrollArea } from '@/components/ui/scroll-area'

const isOpen = ref(false)

const props = defineProps<{cartItems: CartItemResponse[]}>();


</script>
<style lang="">
</style>