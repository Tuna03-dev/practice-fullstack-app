<template>
  <div class="flex border p-2 w-full rounded-lg hover:cursor-pointer" @click="handleClick(product.id)">
    <div>
      <img class="w-24" :src="product.image" alt="Product Image" />
    </div>
    <div class="flex flex-col my-2 justify-between">
      <div>
        <p class="font-medium">{{ product.name }}</p>
        <p class="text-gray-400 text-xs">{{ product.categoryName }}</p>
        <vue3star-ratings :star-size="16" :disable-click="true" :star-color="'#FED000'"
          :model-value="product.averageRate"></vue3star-ratings>
      </div>
      <div class="flex flex-col justify-end">
        <div>
          <span v-if="product.priceWithDiscount < product.price" class="text-gray-400 line-through">{{
            product.price.toLocaleString() }} VND</span>
        </div>
        <div>
          <span class="font-medium text-red-400">{{ product.priceWithDiscount.toLocaleString() }} VND</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { type ProductResponse } from '@/apiTypes'
import vue3starRatings from "vue3-star-ratings";
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter();

const props = defineProps<{ product: ProductResponse }>();
const handleClick = (id: string) => {
  router.push(`/products/details/${id}`)
}
</script>
<style lang="">
  </style>