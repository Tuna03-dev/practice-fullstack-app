<template>
  <div class="grid auto-rows-auto gap-4 justify-center p-4 bg-gray-50">
    <div class="flex justify-center">
      <a data-fancybox="gallery" :href="mainImage" class="w-96">
        <img
        :src="mainImage"
        class="w-full h-auto object-cover rounded-lg hover:cursor-pointer"
        alt=""
        />
      </a>
    </div>
    <div class="flex gap-4 w-full overflow-auto">
      <a data-fancybox="gallery" :href="mainImage" >
        <img
          :src="mainImage"
          class="w-24 h-24 object-cover rounded-lg hover:border-2 hover:border-orange-500"
          alt=""
        />
      </a>
      <a data-fancybox="gallery" v-for="image in images" :key="image.id" :href="image.url"  >
        <img
          :src="image.url"
          :alt="image.description"
          class="w-24 h-24 object-cover rounded-lg hover:border-2 hover:border-orange-500"
        />
      </a>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { ProductImageResponse } from '@/apiTypes'
import { Fancybox } from '@fancyapps/ui'
import '@fancyapps/ui/dist/fancybox/fancybox.css'
import { onMounted, ref } from 'vue';


const props = defineProps<{
  images: ProductImageResponse[]
  mainImage: string
}>()


const currentMainImage = ref<string>(props.mainImage);

// const changeMainImage = (url: string) => {
//   currentMainImage.value = url;
// }


onMounted(() => {
  currentMainImage.value = props.mainImage;

  Fancybox.bind("[data-fancybox='gallery']", {
    Thumbs: false,
    Toolbar: true,
    animated: true
  });
});
</script>



<style scoped>
</style>