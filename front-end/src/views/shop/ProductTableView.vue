<script setup lang="ts">

import { onMounted, ref } from 'vue'
import { columns, type Payment } from '@/components/data-table/columns'; 
import DataTable from '@/components/data-table/DataTable.vue';
import type { ProductDetailResponse } from '@/apiTypes';
import ProductApi from '@/api/ProductApi';
import { useAuthStore } from '@/stores/authStore';
import { useShop } from '@/composables/useShop';

const authStore = useAuthStore();
const {error, fetchShopInfor, loading, shopInfo} = useShop();
const data = ref<ProductDetailResponse[]>([])

const fetchProducts = async () => {
  if(!shopInfo.value) {
    return;
  }

  try {
    const response = await ProductApi.getByShopId(shopInfo.value.id);
    data.value = response.data
    console.log(response.data)
  } catch (error) {
    console.error(error)
  }
}
onMounted(async () => {
  await fetchShopInfor(authStore.username);
  if(!error.value) fetchProducts();
})
</script>

<template>
  <div class="container mx-auto p-4 space-y-6">
    <h1 class="text-2xl font-bold mb-4">List of products</h1>
    <div>

      <DataTable :columns="columns" :data="data" />
    </div>
  </div>
</template>