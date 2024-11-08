<template >
  <Container>
    <div class="font-bold text-5xl mb-10 text-center">Products</div>

    <div class="grid grid-cols-4 gap-4">
      <div class="md:col-span-1 col-span-4">
        <div class="flex justify-between">
          <div class="flex gap-2 text-2xl font-bold">
            <Filter />
            <span>Filter</span>
          </div>
          <div
            class="flex justify-start items-center hover:cursor-pointer hover:bg-gray-100"
            @click="handleClear"
          >
            <button>Clear</button>
            <X />
          </div>
        </div>
        <div>
          <p class="font-medium text-xl mt-6">Categories</p>
          <div class="flex flex-col px-0 py-4">
            <div
              class="w-full flex items-center gap-2 py-3 font-medium hover:bg-gray-100 hover:cursor-pointer"
              v-for="category in categories"
              :key="category.id"
              @click="handleClick(category.id)"
            >
              <iconify-icon :icon="category.icon" class="text-2xl" />
              {{ category.name }}
            </div>
          </div>
        </div>
      </div>
      <div class="md:col-span-3 col-span-4">
        <div v-if="productList.length === 0" class="text-center text-3xl text-red-400 bg-gray-100 p-4">
          No products
        </div>
        <div v-else class="flex items-center gap-2 bg-orange-300 px-4 py-4 mb-4">
          <p class="font-medium text-xl">Sort by</p>
          <Button
            class="ml-6 text-lg w-20"
            :class="sort === 'HOT' ? 'bg-orange-500 text-white' : 'bg-white text-orange-500'"
            @click="handleSortClick('HOT')"
          >
            Hot
          </Button>
          <Button
            class="w-20 text-lg"
            :class="sort === 'NEW' ? 'bg-orange-500 text-white' : 'bg-white text-orange-500'"
            @click="handleSortClick('NEW')"
          >
            New
          </Button>
          <Select v-on:update:model-value="handleSortClick" >
            <SelectTrigger class="w-[180px] bg-white text-lg text-orange-500">
              <SelectValue placeholder="Price" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="PRICE_DESC" class="text-lg"> Price Descending </SelectItem>
                <SelectItem value="PRICE_ASC" class="text-lg"> Price Ascending </SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <div class="grid lg:grid-cols-4 md:grid-cols-3 grid-cols-2 gap-4">
          <product-card
            v-for="product in productList"
            :product="product"
            :key="product.id"
          ></product-card>
        </div>
        <div v-if="totalPages > 1" class="flex justify-center mt-6">
          <Pagination
            
            :total="totalPages * 10"
            :sibling-count="1"
            show-edges  
            :default-page="1"
          >
            <PaginationList v-slot="{ items }" class="flex items-center gap-1">
              <PaginationFirst @click="navigateToPage(1)" />
              <PaginationPrev @click="navigateToPage(currentPages - 1)" />
              <template v-for="(item, index) in items">
                <PaginationListItem
                  v-if="item.type === 'page'"
                  :key="index"
                  :value="item.value"
                  as-child
                >
                  <Button
                    @click="navigateToPage(item.value)"
                    class="w-10 h-10 p-0"
                    :variant="item.value === currentPages ? 'default' : 'outline'"
                  >
                    {{ item.value }}
                  </Button>
                </PaginationListItem>
                <PaginationEllipsis v-else :key="item.type" :index="index" />
              </template>
              <PaginationNext @click="navigateToPage(currentPages + 1)" />
              <PaginationLast @click="navigateToPage(totalPages)" />
            </PaginationList>
          </Pagination>
        </div>
      </div>
    </div>
  </Container>
</template>
<script lang="ts" setup>
import {
  Pagination,
  PaginationEllipsis,
  PaginationFirst,
  PaginationLast,
  PaginationList,
  PaginationListItem,
  PaginationNext,
  PaginationPrev
} from '@/components/ui/pagination'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { Button } from '@/components/ui/button'
import { Filter, X } from 'lucide-vue-next'
import ProductApi from '@/api/ProductApi'
import type { CategoryResponse, ProductResponse } from '@/apiTypes'
import { onMounted, ref, watch } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import Container from '@/components/Container.vue'
import { useRoute, useRouter } from 'vue-router'
import CategoryApi from '@/api/CategoryApi'
import { toast } from 'vue-sonner'

const productList = ref<ProductResponse[]>([])
const totalPages = ref<number>(0)
const currentPages = ref<number>(1)
const route = useRoute()
const router = useRouter()
const pageSize = ref<number>(12)
const query = ref<string>('')
const categories = ref<CategoryResponse[]>([])
const categoryId = ref<string>('')
const sort = ref<string>('NONE')

const handleClear = () => {
  router.push(route.path)
}
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
const fetchProducts = async () => {
  try {
    const response = await ProductApi.getAllProducts({
      page: currentPages.value - 1,
      size: pageSize.value,
      name: query.value,
      sort: sort.value,
      categoryId: categoryId.value
    })
    productList.value = response.data.content
    totalPages.value = response.data.totalPages
    window.scrollTo(0, 0)
    console.log(response.data)
  } catch (error) {
    console.error(error)
  }
}
const handleClick = (id: string) => {
  const { page, ...others } = route.query
  
  router.push({ query: { ...others, category: id } })
}

const handleSortClick = (sortOption: string) => {
  const { page, ...others } = route.query
  
  router.push({ query: { ...others, sort: sortOption } })
}
const navigateToPage = (page: number) => {
  if (page < 1 || page > totalPages.value) return
  router.push({ query: { ...route.query, page } })
}

watch(
  () => [route.query.search, route.query.page, route.query.category, route.query.sort],
  ([newSearch, newPage, newCategoryId, newSort]) => {
    query.value = newSearch?.toString() || ''
    categoryId.value = newCategoryId?.toString() || ''
    sort.value = newSort?.toString() || 'NONE'
    currentPages.value = Number(newPage) || 1
    fetchProducts()
  },
  { immediate: true }
)

onMounted(() => {
  fetchCategory()
  fetchProducts()
})
</script>
<style>
</style>