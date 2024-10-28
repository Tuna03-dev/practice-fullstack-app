<template>
  <Card >
    <CardHeader class="border-b-[1px]">
      <CardTitle>CATEGORY</CardTitle>
      <CardDescription>Some category you can find!</CardDescription>
    </CardHeader>
    <CardContent class="flex flex-col px-0 py-4">
      <div class="w-full flex items-center gap-2 px-6 py-3 font-medium hover:bg-gray-100 hover:cursor-pointer" v-for="category in categories" :key="category.id" @click="handleClick(category.id)">
        <iconify-icon :icon="category.icon" class="text-2xl" />
        {{ category.name }}
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import CategoryApi from '@/api/CategoryApi'
import type { CategoryResponse } from '@/apiTypes'

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import { onMounted, ref } from 'vue'
import { toast } from 'vue-sonner'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute();
const router = useRouter();
const categories = ref<CategoryResponse[]>([])

const fetchCategory = async () => {
  try {
    const response = await CategoryApi.getAllCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error: any) {
    console.log(error)
    toast.error(error.message)
  }
}

const handleClick = (id: string) => {
  router.push({path: "/products", query: { ...route.query, category: id }})
    
}

onMounted(() => {
  fetchCategory()
})
</script>

<style>
</style>