<template >
   <DropdownMenu>
    <DropdownMenuTrigger as-child>
        <Avatar >
            <AvatarImage :src="'./src/assets/hi.gif'" alt="User Avatar"></AvatarImage>
        </Avatar>
    </DropdownMenuTrigger>
    <DropdownMenuContent  class="w-56 font-medium">
      <DropdownMenuLabel >My Account</DropdownMenuLabel>
      <DropdownMenuSeparator />
      <DropdownMenuGroup>
        <DropdownMenuItem>
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
import { useAuthStore } from '@/stores/authStore';
import { toast } from 'vue-sonner';

const authStore = useAuthStore();
const logout = () => {
authStore.logout();
toast.success('Logout successful!');
}

</script>
<style lang="">
</style>