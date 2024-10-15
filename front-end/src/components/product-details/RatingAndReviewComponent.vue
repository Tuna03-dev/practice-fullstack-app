<template>
  <div class="flex flex-col gap-4">
    <div class="flex items-center gap-2">
      <label for="sort">Sort by</label>
      <Select v-model="selectedSort">
        <SelectTrigger class="w-[150px]">
          <SelectValue>{{ selectedSort }}</SelectValue>
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            <SelectLabel>Time</SelectLabel>
            <SelectItem value="Newest"> Newest </SelectItem>
            <SelectItem value="Oldest"> Oldest </SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
    </div>
    <div v-if="reviews.length > 0"
      @scroll="handleScroll"
      class="border border-gray-200 rounded-xl p-4 max-h-[500px] overflow-y-auto"
    >
      <div class="space-y-6">
        <div
          v-for="review in reviews"
          :key="review.id"
          class="space-y-2 border-b border-gray-200 pb-4"
        >
          <div class="flex items-start space-x-4">
            <Avatar>
              <AvatarImage :src="review.userAvatar" alt="Avatar" />
              <AvatarFallback>{{ review.userName.charAt(0) }}</AvatarFallback>
            </Avatar>
            <div class="flex-1 space-y-1">
              <div class="flex items-center justify-between">
                <div class="flex flex-col">
                  <h4 class="font-semibold">{{ review.userName }}</h4>
                  <StarRateing
                    :star-size="16"
                    :show-rating="false"
                    :rating="review.rating"
                    :read-only="true"
                    :increment="0.01"
                  ></StarRateing>
                </div>
                <span class="text-sm text-gray-500">{{ review.updatedAt }}</span>
              </div>
              <div class="flex flex-col">
                <p v-if="review.colour">
                  <span class="text-gray-400">Colour:</span> {{ review.colour }}
                </p>
                <p v-if="review.material">
                  <span class="text-gray-400">Material:</span> {{ review.material }}
                </p>
                <p v-if="review.trueDescription">
                  <span class="text-gray-400">True to description:</span>
                  {{ review.trueDescription }}
                </p>
                <p class="text-sm">{{ review.comment }}</p>
              </div>

              <Button class="flex items-center space-x-4" variant="ghost" size="sm">
                <ThumbsUp class="mr-2 h-4 w-4" />
                Like
                <span class="text-sm text-gray-500">{{ review.likes }} likes</span>
              </Button>
            </div>
          </div>
        </div>
        <div v-if="loading" class="text-center">Loading...</div>
      </div>
    </div>
    <div v-else>
      <p class="text-center text-gray-400 bg-gray-100 rounded-lg p-3 ">No reviews found</p>
    </div>
  </div>
</template>
  
<script lang="ts" setup>
import type { ReviewResponse } from '@/apiTypes'
import StarRateing from 'vue-star-rating'
import { ScrollArea } from '@/components/ui/scroll-area'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { ThumbsUp } from 'lucide-vue-next'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { ref } from 'vue'

const selectedSort = ref('Newest')
const props = defineProps<{
  reviews: ReviewResponse[]
  loading: boolean,
  hasMoreReviews: boolean
}>()

const emit = defineEmits(['scrollEnd'])

const handleScroll = (e: Event) => {
  const container = e.target as HTMLElement
  if (
    container.scrollTop + container.clientHeight >= container.scrollHeight - 5 &&
    props.hasMoreReviews
  ) {
    emit('scrollEnd')
  }
}
</script>
  
<style lang="scss"  scoped>
.overflow-y-auto {
  scrollbar-width: thin; 
  scrollbar-color: #A0AEC0 transparent; 
  

  &::-webkit-scrollbar {
    width: 8px; 
  }

  ::-webkit-scrollbar-thumb {
    background-color: #A0AEC0;
    border-radius: 8px; 
    border: 2px solid transparent; 
    background-clip: content-box; 
  }

  &::-webkit-scrollbar-track {
    background-color: transparent; 
  }

  &::-webkit-scrollbar-button {
    display: none; 
  }
}


</style>
  