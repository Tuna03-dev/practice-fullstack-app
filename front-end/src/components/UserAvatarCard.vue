<template>
  <div class="bg-gradient-to-b from-blue-100 to-gray-50 rounded-lg p-5  flex flex-col items-center justify-start gap-6">
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
      <input class="hidden" id="avatar" type="file" @change="handleFileUpload" />
    </div>
    <div class="font-medium text-2xl">
      <p>{{ username }} <span class="text-red-400 text-xs">({{ firstName }} {{ lastName }})</span></p>
    </div>
  </div>
</template>
<script lang="ts" setup>

import { ref } from 'vue';
import { Avatar, AvatarImage } from '../components/ui/avatar'
import { toast } from 'vue-sonner';
import useStorage from '../composables/useStorage';
import UserApi from '../api/UserApi';

const props = defineProps({
    avatar: String,
    username: String,
    firstName: String,
    lastName: String
})

const emits = defineEmits(['loadAvatar']);

const file = ref<File | null>(null)
const {uploadFile, url} = useStorage('avatar');

const handleFileUpload = async (e: Event) => {
  const selectFile = (e.target as HTMLInputElement)?.files?.[0];
  const type = ["image/png", "image/jpg", "image/jpeg", "image/webp"];

  if (selectFile && type.includes(selectFile.type)) {
    file.value = selectFile;
    if(file.value) await uploadFile(file.value);
    console.log(url.value);
    const response = await UserApi.updateAvatar(url.value);
    if(response.code === 200){
      toast.success(response.message);
    }else{
      toast.error(response.message);
    }

    emits('loadAvatar');
  }else{
    file.value = null;
    toast.error("Please select a valid image file (png, jpg, jpeg, webp)");
  }
}


</script>
<style lang="">
</style>