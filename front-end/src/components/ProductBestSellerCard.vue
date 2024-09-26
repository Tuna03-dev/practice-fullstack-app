<template >
  <div class="flex border p-2 w-[300px] rounded-lg my-4">
    <div>
      <img class="w-24" :src="product.image" alt="Product Image" />
    </div>
    <div class="flex flex-col my-2 gap-1">
      <p class="font-medium">{{ product.name }}</p>
      <StarRating :star-size="16" :show-rating="false" :rating="product.averageRate" :read-only="true" :increment="0.01"></StarRating>
      <div>
        <span class="text-gray-400 line-through">{{ product.price }} VND</span>
      </div>
      <div>
        <span class="font-medium ">{{ product.priceWithDiscount }} VND</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { bigint } from 'zod'
import { type ProductResponse } from '@/apiTypes'
import StarRating from 'vue-star-rating' 
import { ref } from 'vue';


const props = defineProps<{ product: ProductResponse }>()
const discountPrice = ref<number>(0);


</script>
<style lang="">
</style>