<template >
    <div class="h-full">
        <personal-information v-if="userInfo" :user="userInfo" v-on:update-profile="updateUserProfile"></personal-information>
    </div>
</template>
<script lang="ts" setup>
import PersonalInformation from '@/components/PersonalInformation.vue';
import type { UpdateUserProfileRequest, UserProfileResponse } from '@/apiTypes'
import UserApi from '@/api/UserApi'
import { toast } from 'vue-sonner'
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/authStore'

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
  
    console.error('Failed to fetch user profile:', error)
  }
}



const updateUserProfile = async (updateProfile: UpdateUserProfileRequest) => {
    try{
        const response = await UserApi.updateUserProfile(updateProfile);
        if(response.code === 200){
            toast.success(response.message)
            fetchUserInfo()
        }
    }catch(err: any){
      toast.error(err.message)
    }
}

onMounted(() => {
  fetchUserInfo()
})
</script>
<style lang="">
</style>