<script setup lang="ts">
import SliderHome from '@/components/SliderHome.vue'
import CategoryCard from '@/components/CategoryCard.vue'
import Container from '@/components/Container.vue'
import ProductBestSellerCard from '@/components/ProductBestSellerCard.vue'
import { onMounted, ref } from 'vue'
import type { ProductResponse } from '@/apiTypes'
import ProductApi from '@/api/ProductApi'
import { toast } from 'vue-sonner'

const productSellers = ref<ProductResponse[]>([])

const fetchProductSellers = async () => {
  try {
    const response = await ProductApi.getTopBestSellers(5)
    productSellers.value = response.data
  } catch (error: any) {
    console.error(error)
    toast.error(error.message)
  }
}


onMounted(() => {
  fetchProductSellers();
})
</script>

<template>
  <container>
    <slider-home></slider-home>
    <category-card class="category-card"></category-card>
    <product-best-seller-card  v-for="product in productSellers" :product="product" :key="product.id"></product-best-seller-card>
  </container>
</template>

<style lang="scss">
.category-card {
  margin-top: 60px;
}
</style>
