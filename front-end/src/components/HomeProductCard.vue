<template >
  <div class="flex border p-2 w-full rounded-lg my-4 hover:cursor-pointer" @click="handleClick(product.id)">
    <div>
      <img class="w-24" :src="product.image" alt="Product Image" />
    </div>
    <div class="flex flex-col my-2 gap-1">
      <p class="font-medium">{{ product.name }}</p>
      <p class="text-gray-400 text-xs">{{ product.categoryName }}</p>
      <div>
        <span class="text-gray-400 line-through">{{ product.price }} VND</span>
      </div>
      <div>
        <span class="font-medium text-red-400">{{ product.priceWithDiscount }} VND</span>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { type ProductResponse } from '@/apiTypes'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter();

const props = defineProps<{ product: ProductResponse }>();
const handleClick = (id: string) => {
    router.push(`/products/${id}`)
}
</script>
<style lang="">
</style>