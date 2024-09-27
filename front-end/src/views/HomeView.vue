<script setup lang="ts">
import SliderHome from '@/components/SliderHome.vue'
import CategoryCard from '@/components/CategoryCard.vue'
import Container from '@/components/Container.vue'
import ProductBestSellerCard from '@/components/ProductBestSellerCard.vue'
import { onMounted, ref } from 'vue'
import type { ProductResponse } from '@/apiTypes'
import ProductApi from '@/api/ProductApi'
import { toast } from 'vue-sonner'
import HomeProductCard from '@/components/HomeProductCard.vue'


const productSellers = ref<ProductResponse[]>([])
const productToprates = ref<ProductResponse[]>([])
  const productNewArrivals = ref<ProductResponse[]>([])

const fetchProductSellers = async () => {
  try {
    const response = await ProductApi.getTopBestSellers(5)
    productSellers.value = response.data
  } catch (error: any) {
    console.error(error)
    toast.error(error.message)
  }
}

const fetchProductTopRate= async () => {
  try {
    const response = await ProductApi.getTopOfTopRated(5)
    productToprates.value = response.data
  } catch (error: any) {
    console.error(error)
    toast.error(error.message)
  }
}

const fetchProductNewArrivals = async () => {
  try {
    const response = await ProductApi.getTopNewArrivals(5)
    productNewArrivals.value = response.data
  } catch (error: any) {
    console.error(error)
    toast.error(error.message)
  }
}

onMounted(() => {
  fetchProductSellers();
  fetchProductTopRate();
  fetchProductNewArrivals();
})
</script>

<template>
  <container>
    <slider-home></slider-home>
    <div class=" grid grid-cols-3 gap-4 mt-16">
      <div class="col-span-1">
        <category-card  class="category-card w-10/12"></category-card>

        <div>
          <p class="font-bold text-2xl mt-6">Best Seller</p>
        </div>
        <product-best-seller-card class="w-10/12"  v-for="product in productSellers" :product="product" :key="product.id"></product-best-seller-card>
      </div>
      <div>
        <p class="font-bold text-2xl">New Arrivals</p>
        <home-product-card v-for="product in productNewArrivals" :product="product" :key="product.id"></home-product-card>
      </div>
      <div>
        <p class="font-bold text-2xl">Top Rated</p>
        <home-product-card v-for="product in productToprates" :product="product" :key="product.id"></home-product-card>
      </div>
    </div>
    
  
  </container>
</template>

<style lang="scss">

</style>
