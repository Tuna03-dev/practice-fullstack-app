<template >
  <div class="bg-gray-100 rounded-lg p-8 flex gap-10">
    <div class="flex gap-4 border-r-2  border-black pr-10">
      <Avatar class="w-20 h-20">
        <AvatarImage src="../../public/images/shops/default-avatar.png" alt="@radix-vue" />
        <AvatarFallback>STORE</AvatarFallback>
      </Avatar>
      <div class="flex flex-col gap-2">
        <label for="" class="text-lg font-bold">{{ shop.name }}</label>
        <button class="border-[2px] hover:bg-white border-black px-3 py-1 rounded-md flex gap-1">
          <Store strokeWidth="{1}" /> View Shop
        </button>
      </div>
    </div>
    <div class="flex-1 flex flex-col justify-between">
      <div class="flex justify-between">
        <label class="text-lg font-bold">Ratings</label>
        <label class="text-lg font-bold text-orange-500">{{ shop.averageRate }}</label>
      </div>
      <div class="flex justify-between">
        <label class="text-lg font-bold">Number of Reviews</label>
        <label class="text-lg font-bold text-orange-500">{{ shop.numberOfRates }}</label>
      </div>
    </div>
    <div class="flex-1 flex flex-col justify-between">
      <div class="flex justify-between">
        <label class="text-lg font-bold">Products</label>
        <label class="text-lg font-bold text-orange-500">{{ shop.numberOfProducts }}</label>
      </div>
      <div class="flex justify-between">
        <label class="text-lg font-bold">Joined</label>
        <label class="text-lg font-bold text-orange-500">{{ shop.joinedDate }}</label>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import type { ShopInformationType } from '@/apiTypes';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Store } from 'lucide-vue-next'

const props = defineProps<{
    shop: ShopInformationType
}>()
</script>
<style lang="">
</style>