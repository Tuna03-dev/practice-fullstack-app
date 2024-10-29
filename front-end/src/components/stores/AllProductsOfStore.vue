<template >
  <Container>
    <div class="grid grid-cols-4 gap-4">
      <div class="col-span-1">
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
      <div class="sm:col-span-3 col-span-4">
        <div class="flex items-center gap-2 bg-orange-300 px-4 py-4 mb-4">
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
          <Select v-on:update:model-value="handleSortClick">
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
          <div class="ml-auto relative">
            <input
              class="p-[6px] pl-10 rounded-md outline-none"
              type="text"
              v-model="query"
              @keyup.enter="handleSearch"
              @input="handleSearch"
              placeholder="Search..."
            />
            <Search
              class="absolute left-2 top-1/2 transform -translate-y-1/2 w-6 h-6 text-gray-400"
            />
          </div>
        </div>
        <div
          v-if="productList.length === 0"
          class="text-center text-3xl text-red-400 bg-gray-100 p-4"
        >
          No products
        </div>
        <div class="grid lg:grid-cols-4 md:grid-cols-3 sm:grid-cols-2 gap-4">
          <product-card
            v-for="product in productList"
            :product="product"
            :key="product.id"
          ></product-card>
        </div>
        <div v-if="props.totalPages > 1" class="flex justify-center mt-6 ">
          <Pagination
            
            :total="props.totalPages * 10"
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
              <PaginationLast @click="navigateToPage(props.totalPages)" />
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
import { Filter, X, Search } from 'lucide-vue-next'
import ProductApi from '@/api/ProductApi'
import type { CategoryResponse, ProductResponse } from '@/apiTypes'
import { onMounted, ref, watch } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import Container from '@/components/Container.vue'
import { useRoute, useRouter } from 'vue-router'
import CategoryApi from '@/api/CategoryApi'
import { toast } from 'vue-sonner'

const currentPages = ref<number>(1)
const route = useRoute()
const router = useRouter()
const pageSize = ref<number>(12)
const query = ref<string>('')
const categoryId = ref<string>('')
const sort = ref<string>('NONE')
const props = defineProps<{
  productList: ProductResponse[]
  categories: CategoryResponse[]
  totalPages: number
}>()

const emits = defineEmits<{
  fetchProducts: [
    currentPage: number,
    pageSize: number,
    query: string,
    sort: string,
    categoryId: string
  ]
}>()
const handleClear = () => {
  router.push(route.path)
}

const handleSearch = () => {
  const { page, ...others } = route.query
  router.push({ query: { ...others, search: query.value } })
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
  if (page < 1 || page > props.totalPages) return
  router.push({ query: { ...route.query, page } })
}

watch(
  () => [route.query.search, route.query.page, route.query.category, route.query.sort],
  ([newSearch, newPage, newCategoryId, newSort]) => {
    query.value = newSearch?.toString() || ''
    categoryId.value = newCategoryId?.toString() || ''
    sort.value = newSort?.toString() || 'NONE'
    currentPages.value = Number(newPage) || 1
    emits(
      'fetchProducts',
      currentPages.value,
      pageSize.value,
      query.value,
      sort.value,
      categoryId.value
    )
  },
  { immediate: true }
)
</script>
  <style>
</style>