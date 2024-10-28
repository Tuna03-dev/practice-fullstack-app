<template>
  <div class="bg-gradient-to-b from-gray-100 to-gray-50 rounded-lg p-5  flex flex-col items-center justify-start gap-6">
    <div class="relative">
      <Avatar class="h-32 w-32 border-8 border-slate-500">
        <AvatarImage :src="avatar ? avatar : '../src/assets/hi.gif'" alt="User Avatar"></AvatarImage>
      </Avatar>
      <label
        for="avatar"
        class="absolute -bottom-2 -right-5 text-blue-400 hover: cursor-pointer hover:text-blue-300 hover:scale-110 transition duration-200 ease-in-out"
      >
        <iconify-icon class="text-3xl" icon="ic:outline-file-upload" />
      </label>
      <input class="hidden" id="avatar" type="file" />
    </div>
    <div class="font-medium text-2xl">
      <p>{{ username }} <span class="text-red-400 text-xs">({{ firstName }} {{ lastName }})</span></p>
    </div>
  </div>
</template>
<script lang="ts" setup>

import { Avatar, AvatarImage } from '../components/ui/avatar'

const props = defineProps({
    avatar: String,
    username: String,
    firstName: String,
    lastName: String
})
</script>
<style lang="">
</style>