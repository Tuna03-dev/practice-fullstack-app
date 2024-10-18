<template >
  <div class="flex border p-2  rounded-lg my-4 hover:cursor-pointer" @click="handleClick(product.id)">
    <div>
      <img class="w-24" :src="product.image" alt="Product Image" />
    </div>
    <div class="flex flex-col my-2 gap-1">
      <p class="font-medium">{{ product.name }}</p>
      <StarRating :star-size="16" :show-rating="false" :rating="product.averageRate" :read-only="true" :increment="0.01"></StarRating>
      <div>
        <span class="text-gray-400 line-through">{{ product.price.toLocaleString() }} VND</span>
      </div>
      <div>
        <span class="font-medium ">{{ product.priceWithDiscount.toLocaleString() }} VND</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { bigint } from 'zod'
import { type ProductResponse } from '@/apiTypes'
import StarRating from 'vue-star-rating' 
import { ref } from 'vue';
import { useRouter } from 'vue-router'

const router = useRouter();

const props = defineProps<{ product: ProductResponse }>()
const discountPrice = ref<number>(0);
  const handleClick = (id: string) => {
    router.push(`/products/details/${id}`)
}

</script>
<style lang="">
</style>