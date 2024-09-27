<template>
  <div>
    <swiper
      class=""
      :slides-per-view="1"
      :space-between="20"
      :autoplay="{ delay: 3000, disableOnInteraction: false }"
      :pagination="{ clickable: true }"
      :scrollbar="{ draggable: true }"
      :modules="[Navigation, Pagination, Scrollbar, A11y, Autoplay]"
    >
      <swiper-slide class="" v-for="slide in slides" :key="slide.id">
        
            <img
            class="rounded-lg mx-auto shadow-gray-200 shadow-lg"
            :src="slide.imageUrl"
            :alt="slide.title"
            @click="handleClick(slide.link)"
            />
        
      </swiper-slide>
    </swiper>
  </div>
</template>
<script lang="ts" setup>
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation, Pagination, Scrollbar, A11y, Autoplay } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'
import 'swiper/css/scrollbar'

import SliderApi from '@/api/SliderApi'
import { sliderNames, type slidesResponse } from '@/apiTypes'
import { onMounted, ref } from 'vue'
import { toast } from 'vue-sonner'
import Container from './Container.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const slides = ref<slidesResponse[]>([])

const fetchSlides = async () => {
  try {
    const response = await SliderApi.getAllSlidesBySliderName(sliderNames.HOME)
    slides.value = response.data
  } catch (error: any) {
    console.log(error)
    toast.error(error.message)
  }
}

const handleClick = (link: string) => {
  router.push(link)
}

onMounted(() => {
  fetchSlides()
})
</script>
<style lang="">
</style>