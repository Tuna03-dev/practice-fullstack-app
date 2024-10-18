<template >

  <div class="flex justify-center gap-6">
    <div v-for="product in recommendedProducts" :key="product.id" class="flex flex-col border w-[15%] rounded-lg hover:cursor-pointer hover:shadow-lg hover:-translate-y-1 transition-all" @click="handleClick(product.id)">
      <div class="relative h-[150px] flex justify-center rounded-t-lg">
        <div class="absolute top-0 right-0 bg-red-500 text-white text-xs font-bold rounded-tr-lg rounded-bl-lg h-6 w-8 flex items-center justify-center">
          <span>{{ Math.round((1 - Number(product.priceWithDiscount) / Number(product.price)) * 100) }}%</span>
        </div>
        <img class="h-full object-cover" :src="product.image" alt="Product Image" />
      </div>
      <div class="flex flex-col gap-2 p-2 text-xl bg-slate-200 rounded-b-lg">
        <p>{{ product.name }}</p>
        <div>
          <p class="text-red-500">₫{{ product.priceWithDiscount.toLocaleString() }}</p>
        </div>
        <div class="flex gap-2 text-sm">
          <div class="flex items-center border-r pr-2 gap-1 border-orange-200">
            <Star :size="18" strokeWidth="{1}" fill="#FFB91E" color="#FFB91E" />
            <p>{{ product.averageRate }}</p>
          </div>
          <p>{{ product.soldQuantity }} sold</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import type { ProductResponse } from '@/apiTypes';
import router from '@/router';
import { Star } from 'lucide-vue-next'

const props = defineProps<{
  recommendedProducts: ProductResponse[]
}>();

const handleClick = (id: string) => {
  router.push(`/products/details/${id}`)
}
</script>
<style lang="">
</style>