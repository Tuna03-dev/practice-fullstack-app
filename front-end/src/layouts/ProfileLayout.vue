<template >
  <container>
    <div class="grid grid-cols-4">
      <div class="col-span-1 flex flex-col gap-6">
        <user-avatar-card
          :avatar="userInfo?.imageUrl"
          :username="authStore.username"
          :first-name="userInfo?.firstname"
          :last-name="userInfo?.lastname"
          @load-avatar="fetchUserInfo"
        ></user-avatar-card>
        <ProfileBar></ProfileBar>
      </div>

      <div class="col-span-3 h-full ml-5">
        <router-view :key="$route.fullPath"></router-view>
      </div>
    </div>
  </container>
</template>
<script lang="ts" setup>
import UserAvatarCard from '@/components/UserAvatarCard.vue'
import ProfileBar from '@/components/ProfileBar.vue'
import type { UserProfileResponse } from '@/apiTypes'
import UserApi from '@/api/UserApi'
import { toast } from 'vue-sonner'
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { RouterView } from 'vue-router'
import Container from '@/components/Container.vue'
const authStore = useAuthStore()

const userInfo = ref<UserProfileResponse>()

const fetchUserInfo = async () => {
  try {
    const response = await UserApi.getInformation(authStore.username)
    if (response.code === 200) {
      console.log(response.data)
      userInfo.value = response.data
    }
  } catch (error: any) {
    toast.error(error.message)
    console.error('Failed to fetch user profile:', error)
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>
<style lang="">
</style>