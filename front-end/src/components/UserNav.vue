<template >
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Avatar>
        <AvatarImage :src="userInfo?.imageUrl ? userInfo.imageUrl : '../src/assets/hi.gif'" alt="User Avatar"></AvatarImage>
      </Avatar>
    </DropdownMenuTrigger>
    <DropdownMenuContent class="w-56 font-medium">
      <DropdownMenuLabel>My Account</DropdownMenuLabel>
      <DropdownMenuSeparator />
      <DropdownMenuGroup>
        <DropdownMenuItem @click="profile">
          <iconify-icon icon="lucide:user" class="mr-2 h-4 w-4" />
          <span>Profile</span>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <iconify-icon icon="lucide:credit-card" class="mr-2 h-4 w-4" />

          <span>Billing</span>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <iconify-icon icon="lucide:settings" class="mr-2 h-4 w-4" />
          <span>Settings</span>
        </DropdownMenuItem>
      </DropdownMenuGroup>

      <DropdownMenuSeparator />
      <DropdownMenuItem @click="logout">
        <iconify-icon icon="lucide:log-out" class="mr-2 h-4 w-4" />
        <span>Log out</span>
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>
<script setup lang="ts">
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger
} from '../components/ui/dropdown-menu'
import { Avatar, AvatarImage } from '../components/ui/avatar'
import { useAuthStore } from '@/stores/authStore'
import { toast } from 'vue-sonner'
import { useRouter } from 'vue-router'
import { useQueryClient } from '@tanstack/vue-query'
import UserApi from '../api/UserApi'
import { onMounted, ref } from 'vue'
import type { UserProfileResponse } from '@/apiTypes'

const queryClient = useQueryClient()
const route = useRouter()
const authStore = useAuthStore()
const logout = () => {
  authStore.logout()
  queryClient.invalidateQueries({ queryKey: ['cartitems'] })
  route.push('/')
  toast.success('Logout successful!')
}
const userInfo = ref<UserProfileResponse>()
const profile = () => {
  route.push('/profile/personal-information')
}

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

onMounted(() => {
  fetchUserInfo()
})
</script>
<style lang="">
</style>