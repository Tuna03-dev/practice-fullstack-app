<template >
  <Container class="flex flex-col gap-10">
    <div class="grid grid-cols-2 gap-4">
      <div class="w-[90%]">
        <product-detail-images
          :images="productDetail.productImages"
          :main-image="productDetail.image"
        ></product-detail-images>
      </div>
      <div class="w-[90%]">
        <product-detail-information :product="productDetail" @add-to-cart="addToCart" ></product-detail-information>
      </div>
    </div>

    <shop-information :shop="shopDetail"></shop-information>
    <div>
      <nav class="flex justify-center items-center">
        <button
          v-for="item in navItems"
          :key="item.name"
          :class="[
            'px-4 py-2 text-xl font-medium w-full',
            activeTab === item.name
              ? 'text-primary border-b-2 border-black'
              : 'text-gray-500 border-b-2 border-gray-200 hover:text-gray-700'
          ]"
          @click="setActiveTab(item.name)"
        >
          {{ item.name }}
          <span v-if="item.count !== undefined" class="ml-1 bg-red-400 text-white rounded-full px-[10px]">
            {{ item.count }}
          </span>
        </button>
      </nav>

      <div className="my-10">
        <h2 className="text-2xl font-bold my-10">{{ activeTab }}</h2>
        <rating-and-review-component :has-more-reviews="hasMoreReviews" @scroll-end="fetchRevews" :loading="loading" :reviews="reviews" v-if="activeTab === 'Ratings & Reviews'"></rating-and-review-component>
        <description-component :product="productDetail"  v-if="activeTab === 'The Details'"></description-component>
      </div>
    </div>
  </Container>
</template>
<script setup lang="ts">
import RatingAndReviewComponent from '@/components/product-details/RatingAndReviewComponent.vue'
import ProductApi from '@/api/ProductApi'
import type { ProductDetailResponse, ReviewResponse, ShopInformationType } from '@/apiTypes'
import ShopInformation from '@/components/product-details/ShopInformation.vue'
import Container from '@/components/Container.vue'
import ProductDetailImages from '@/components/ProductDetailImages.vue'
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { toast } from 'vue-sonner'
import ProductDetailInformation from '@/components/ProductDetailInformation.vue'
import ShopApi from '@/api/ShopApi'
import ReviewsApi from '@/api/ReviewsApi'
import CartApi from '@/api/CartApi'
import { useQueryClient } from '@tanstack/vue-query'
import DescriptionComponent from '@/components/product-details/DescriptionComponent.vue'

const queryClient = useQueryClient()
const productDetail = ref<ProductDetailResponse>({} as ProductDetailResponse)
const route = useRoute()
const productId = route.params.id as string
const reviews = ref<ReviewResponse[]>([])
const shopDetail = ref<ShopInformationType>({} as ShopInformationType)
const numberOfRates = ref<number>(0)
const navItems = ref<{ name: string; count?: number }[]>([
  { name: 'The Details' },
  { name: 'Ratings & Reviews', count: numberOfRates.value }
])
const activeTab = ref(navItems.value[0].name)
const setActiveTab = (name: string) => {
  activeTab.value = name
}

const loading = ref<boolean>(false)
const page = ref<number>(0)
const hasMoreReviews = ref<boolean>(true)
const fetchProductDetails = async () => {
  try {
    const response = await ProductApi.getProductDetails(productId)
    console.log(response.data)
    productDetail.value = response.data
  } catch (err: any) {
    toast.error(err.message)
    console.log(err)
  }
}

const fetchShopDetails = async () => {
  try {
    const response = await ShopApi.getShopdetailsByProductId(productId)
    shopDetail.value = response.data
    numberOfRates.value = response.data.numberOfRates

    console.log(response.data)
  } catch (err: any) {
    toast.error(err.message)
  }
}

const fetchRevews = async () => {
    if(loading.value) return
    loading.value = true
    try{
        const response = await ReviewsApi.getAllReviewsByProductId(productId, {
            page: page.value,
            size: 5,
            sort: 'Newest'
        })
        reviews.value = [...reviews.value, ...response.data.content]
        page.value+=1
        if(response.data.content.length < 5) hasMoreReviews.value = false
        console.log(response.data)
    }catch(err: any){
      toast.error(err.message)
    }finally{
        loading.value = false
    }
}



const addToCart = async (product: ProductDetailResponse, quantity: number) => {
  try{
      const cartItem = {
          productId: product.id,
          quantity: quantity,
          pricePerProduct: product.priceWithDiscount
      }
      const response = await CartApi.addToCart(cartItem);
      queryClient.invalidateQueries({ queryKey: ['cartitems'] })
      toast.success(response.message)
      console.log(response)
  }catch(err: any){
    toast.error(err.message)
  }

}

watch(activeTab, (newVal) => {
  if (newVal === 'Ratings & Reviews') {
    fetchRevews()
  }
})

onMounted(() => {
  fetchProductDetails()
  fetchShopDetails()
})
</script>
<style lang="">
</style>