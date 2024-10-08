<template >
  <Container class="grid grid-cols-4">
    <div>
        <Filter /> Filter
    </div>
    <div class="col-span-3 grid grid-cols-4 gap-4">
      <product-card
        v-for="product in productList"
        :product="product"
        :key="product.id"
      ></product-card>
    </div>
  </Container>
</template>
<script lang="ts" setup>
import { Filter } from 'lucide-vue-next';
import ProductApi from '@/api/ProductApi'
import type { ProductResponse } from '@/apiTypes'
import { onMounted, ref } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import Container from '@/components/Container.vue';

const productList = ref<ProductResponse[]>([])

const fetchProducts = async () => {
  try {
    const response = await ProductApi.getAllProducts()
    productList.value = response.data.content

    console.log(response.data.content)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchProducts()
})
</script>
<style>
</style>