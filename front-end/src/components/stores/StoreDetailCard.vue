<template >
  <div class="flex gap-20">
    <div class="store-card rounded-lg p-5 w-[500px]">
      <div class="flex items-center gap-4 relative z-10 text-white">
        <Avatar class="w-20 h-20 border-4 border-white border-opacity-65 bg-transparent">
          <AvatarImage :src="shopDetailResponse.imageUrl" alt="Shop avatar" />
          <AvatarFallback class="text-4xl text-white">{{ shopDetailResponse.name && shopDetailResponse.name.charAt(0) }}</AvatarFallback>
        </Avatar>
        <div>
          <label class="text-3xl font-bold">{{ shopDetailResponse.name }}</label>
        </div>
      </div>
    </div>
    <div class="flex flex-col justify-around items-start">
        <div class="flex gap-2">
            <Store strokeWidth={1} />
            <label class="text-lg ">Products:</label>
            <label class="text-lg  text-orange-500">{{ shopDetailResponse.numberOfProducts }}</label>
        </div>
        <div class="flex gap-2">
            <UserRoundCheck strokeWidth={1} />
            <label class="text-lg ">Joined:</label>
            <label class="text-lg  text-orange-500">{{ shopDetailResponse.joinedDate }}</label>
        </div>
        <div class="flex gap-2">
            <Star strokeWidth={1} />
            <label class="text-lg ">Ratings:</label>
            <label class="text-lg  text-orange-500">{{ shopDetailResponse.averageRate }} ({{ shopDetailResponse.numberOfRates }} Rating)</label>
        </div>
    </div>
    
  </div>
</template>
<script lang="ts" setup>
import type { ShopInformationType } from '@/apiTypes';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Store, UserRoundCheck, Star } from 'lucide-vue-next'

const props =defineProps<{
    shopDetailResponse: ShopInformationType

}>();
</script>
<style lang="scss" scoped>
.store-card {
  background: url('../../public/images/shops/background.jpg') center center / cover no-repeat;
  position: relative;

  &:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    border-radius: inherit;
  }
}
</style>