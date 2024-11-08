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
  <Container>
    <!-- Slider Section -->
    <SliderHome />

    <!-- Responsive Grid for Category, Best Seller, New Arrivals, and Top Rated -->
    <div class="grid gap-4 mt-16 md:grid-cols-3">
      <!-- Category Section -->
      <div class="md:col-span-1">
        <CategoryCard class="category-card w-full md:w-10/12" />

        <!-- Best Seller Section -->
        <div>
          <p class="font-bold text-2xl mt-6">Best Seller</p>
        </div>
        <div class="grid grid-cols-2 gap-4 md:block">
          <ProductBestSellerCard 
            class="w-full" 
            v-for="product in productSellers" 
            :product="product" 
            :key="product.id" 
          />
        </div>
      </div>

      <!-- New Arrivals Section -->
      <div>
        <p class="font-bold text-2xl">New Arrivals</p>
        <div class="grid grid-cols-2 gap-4 md:block">
          <HomeProductCard 
            v-for="product in productNewArrivals" 
            :product="product" 
            :key="product.id" 
          />
        </div>
      </div>

      <!-- Top Rated Section -->
      <div>
        <p class="font-bold text-2xl">Top Rated</p>
        <div class="grid grid-cols-2 gap-4 md:block">
          <HomeProductCard 
            v-for="product in productToprates" 
            :product="product" 
            :key="product.id" 
          />
        </div>
      </div>
    </div>
  </Container>
</template>

<style lang="scss">

</style>
