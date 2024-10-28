<template>
  <Container>
    <div class="flex flex-col justify-between gap-10 border-b">
      <store-detail-card :shop-detail-response="shopDetail"></store-detail-card>
      <div class="flex w-full relative">
        <button
          v-for="item in navItem"
          :key="item.name"
          :class="{
            'flex-1 text-2xl hover:text-cyan-600 py-2 relative': true,
            'border-b-2 text-cyan-600 border-cyan-600 -bottom-[1px]': activeSection === item.name
          }"
          @click="scrollToSection(item.name)"
        >
          {{ item.name }}
        </button>
      </div>
    </div>
    <div class="mt-10">
      <p class="text-2xl font-bold mb-5">Recommended For You</p>
      <recommened-product :recommended-products="recommendedProducts"></recommened-product>
    </div>
    <div ref="aboutShopSection" class="mt-10">
      <div class="bg-gray-50 p-5 text-xl">
        <div class="mb-2">ABOUT SHOP</div>
        <div class="grid grid-cols-2 gap-10">
          <div class="col-span-1">
            <img
              v-if="shopDetail.descriptionImage"
              :src="shopDetail.descriptionImage"
              alt="Image about shop"
            />
            <div v-else class="text-center text-lg text-red-400 font-bold">No image about shop</div>
          </div>
          <div>
            <iframe
              v-if="shopDetail.description"
              class="w-full h-full"
              :src="shopDetail.description"
            ></iframe>
            <div v-else class="text-center text-lg text-red-400 font-bold">
              No description about shop
            </div>
          </div>
        </div>
      </div>
    </div>

    <div ref="productsSection" class="mt-10 min-h-screen">
      <p class="text-2xl font-bold mb-5">All Products</p>
      <all-products-of-store :total-pages="totalPages" :categories="categories" :product-list="productList" @fetch-products="fetchProducts"></all-products-of-store>
    </div>
  </Container>
</template>
  
<script lang="ts" setup>
import AllProductsOfStore from '@/components/stores/AllProductsOfStore.vue'
import ShopApi from '@/api/ShopApi'
import type { CategoryResponse, ProductResponse, ShopInformationType } from '@/apiTypes'
import Container from '@/components/Container.vue'
import StoreDetailCard from '@/components/stores/StoreDetailCard.vue'
import router from '@/router'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { toast } from 'vue-sonner'
import RecommenedProduct from '@/components/stores/RecommenedProduct.vue'
import ProductApi from '@/api/ProductApi'
import CategoryApi from '@/api/CategoryApi'
import useStorage from '@/composables/useStorage'


const navItem = ref<{ name: string }[]>([
  { name: 'Home' },
  { name: 'About Shop' },
  { name: 'All Products' }
])
const activeSection = ref(navItem.value[0].name)
const productsSection = ref<HTMLElement | null>(null)
const aboutShopSection = ref<HTMLElement | null>(null)
const shopDetail = ref<ShopInformationType>({} as ShopInformationType)
const route = useRoute()
const recommendedProducts = ref<ProductResponse[]>([])
const productList = ref<ProductResponse[]>([]);
const categories = ref<CategoryResponse[]>([]);
const totalPages = ref<number>(0)
const {loadDescriptionFromUrl} = useStorage('shops');
const description = ref<string>('')

const fetchCategory = async () => {
  try {
    const response = await CategoryApi.getAllCategorisByShopId(route.params.id as string)
    categories.value = response.data
  } catch (err: any) {
    toast.error(err.message)
  }
}





const fetchProducts = async (currentPages: number, pageSize: number, query: string, sort: string, categoryId: string) => {
  try {
    const response = await ProductApi.getAllProductByShopId({
      shopId: route.params.id as string,
      page: currentPages - 1,
      size: 12,
      name: query,
      sort: sort,
      categoryId: categoryId
    })
    productList.value = response.data.content
    totalPages.value = response.data.totalPages
    console.log(response.data)
  } catch (error) {
    console.error(error)
  }
}

const fetchRecommendProducts = async () => {
  try {
    const response = await ShopApi.getRecommedProductsByShopId(route.params.id as string)
    recommendedProducts.value = response.data
  } catch (err: any) {
    toast.error(err.message)
  }
}

const fetchShopDetails = async () => {
  try {
    const response = await ShopApi.getShopdetailsById(route.params.id as string)
    shopDetail.value = response.data

    console.log(response.data)
  } catch (err: any) {
    toast.error(err.message)
  }
}

const scrollToSection = (sectionName: string) => {
  activeSection.value = sectionName
  if (sectionName === 'Home') {
    console.log(router.currentRoute.value.path)
    router.push({ path: route.path })
    window.scrollTo(0, 0)
    return
  }

  const sectionRef = sectionName === 'All Products' ? productsSection : aboutShopSection
  sectionRef?.value?.scrollIntoView({ behavior: 'smooth' })
}




onMounted(async () => {
  fetchShopDetails()
  fetchRecommendProducts()
  fetchCategory()
  fetchProducts(1, 12, '', 'NONE', '')
  const descriptionText = await loadDescriptionFromUrl(shopDetail.value.description || '') 
  description.value = descriptionText || ''
})
</script>
  
<style lang="scss" scoped>
</style>
  