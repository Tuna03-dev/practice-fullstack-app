<template >
  <div class="bg-white py-2 border-b">
    <Container class="flex items-center gap-10">
      <div class="flex items-center gap-2">
        <iconify-icon icon="pajamas:github" class="text-3xl" />
        <RouterLink to="/" class="text-xl font-medium">Tuna03-dev</RouterLink>
      </div>
      <ul class="flex gap-2">
        <li
          :class="[
            'hover:bg-gray-300 hover:text-gray-500 rounded-3xl px-4 py-2',
            { 'bg-black text-white': isActive(item.path) }
          ]"
          v-for="item in menuItems"
          :key="item.path"
        >
          <RouterLink class="flex items-center gap-1" :to="item.path">
            <iconify-icon :icon="item.icon" class="text-3xl" />
            <span>{{ item.name }}</span>
          </RouterLink>
        </li>
      </ul>

      <template v-if="isLogged">
        <div class="flex space-x-2 ml-auto">

          <UserNav />
        </div>
      </template>
      <template v-else>
        <div class="flex space-x-2 ml-auto">
          <Dialog>
            <DialogTrigger>
              <button class="border-2 border-black px-4 py-2 rounded-lg font-medium">Sign in</button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-center text-2xl font-bold">Sign in</DialogTitle>
        
              </DialogHeader>
              <SignInForm />
            </DialogContent>
          </Dialog>
        
          <Dialog>
            <DialogTrigger>
              <button class="bg-black text-white px-4 py-2 rounded-lg font-medium">Sign up</button>
            </DialogTrigger>
            <DialogContent >
              <DialogHeader >
                <DialogTitle class="text-center text-2xl font-bold">Sign up</DialogTitle>
        
              </DialogHeader>
              <RegisterForm />
            </DialogContent>
          </Dialog>
        
        </div>
      </template>
    </Container>
  </div>
</template>
<script setup lang="ts">
import Container from './Container.vue'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from '../components/ui/dialog'
import { RouterLink, useRoute } from 'vue-router'
import SignInForm from './SignInForm.vue';
import RegisterForm from './RegisterForm.vue';
import { useAuthStore } from '@/stores/authStore';
import { computed } from 'vue';
import UserNav from './UserNav.vue';
const route = useRoute()

const authStore = useAuthStore();
const isLogged = computed(() => authStore.isLoggedIn);

const isActive = (path: string) => {
  return route.path === path
}

const menuItems = [
  {
    name: 'Home',
    path: '/',
    icon: 'ic:outline-other-houses'
  },
  {
    name: 'Products',
    path: '/product',
    icon: 'ic:baseline-shopify'
  },
  {
    name: 'Stores',
    path: '/store',
    icon: 'lucide:store'
  }
]
</script>
<style lang="scss">
</style>