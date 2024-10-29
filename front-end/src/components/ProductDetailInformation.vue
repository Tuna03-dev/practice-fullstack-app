<template>
  <div class="flex flex-col gap-10">
    <div class="flex flex-col gap-4">
      <label class="text-4xl font-bold">{{ product.name }}</label>
      <label class="text-xl">{{ product.categoryName }}</label>
    </div>
    <vue3starRatings
      :star-color="'#FED000'"
      :star-size="26"
      :disable-click="true"
      :model-value="product.averageRate"
      
    ></vue3starRatings>
    <div class="flex flex-col border-b-2 pb-10">
      <label v-if="product.priceWithDiscount !== undefined" class="text-2xl font-bold line-through text-gray-400">{{ product.price.toLocaleString() }} VND</label>
      <label v-if="product.priceWithDiscount !== undefined" class="text-4xl font-bold text-red-400">
        {{ product.priceWithDiscount.toLocaleString() }} VND
      </label>
    </div>
    <div>
      <Label class="text-base font-bold">Last {{ product.stockQuantity }} left</Label>
      <div class="flex gap-4 items-center">

        <NumberField v-model="numberProducts" class="w-32" :default-value="1" :min="1" :max="product.stockQuantity">
          <NumberFieldContent>
            <NumberFieldDecrement />
            <NumberFieldInput />
            <NumberFieldIncrement />
          </NumberFieldContent>
        </NumberField>
        
        <button class="bg-red-400 text-white p-2 rounded-lg hover:bg-red-300" @click="addToCart">Add to cart</button>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput
} from '@/components/ui/number-field'
import { Label } from '@/components/ui/label'
import { type ProductDetailResponse } from '@/apiTypes'
import vue3starRatings from "vue3-star-ratings";
import { ref } from 'vue';
const props = defineProps<{
  product: ProductDetailResponse
}>()

const numberProducts = ref<number>(1)

const emit = defineEmits(['add-to-cart'])


const addToCart = () => {
  emit('add-to-cart', props.product, numberProducts.value)
}

</script>