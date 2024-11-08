<template >
  <div class="bg-white py-2 border-b">
    <Container class="flex items-center gap-10">
      <div class="flex items-center gap-2">
        <iconify-icon icon="pajamas:github" class="text-3xl" />
        <RouterLink to="/" class="text-xl md:flex hidden font-medium">Tuna03-dev</RouterLink>
      </div>
      <ul class="hidden md:flex  gap-2">
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

      <div class="fixed bottom-0 left-0 right-0 bg-white border-t md:hidden flex justify-around py-2">
      <RouterLink class="flex items-center justify-center gap-1" v-for="item in menuItems" :key="item.path" :to="item.path" :class="{'text-black': isActive(item.path), 'text-gray-400': !isActive(item.path)}">
        <iconify-icon :icon="item.icon" class="text-2xl" />
        <span class="text-xs">{{ item.name }}</span>
      </RouterLink>
      <div @click="handleGotoCart" class="flex  items-center cursor-pointer">
        <iconify-icon icon="ic:round-shopping-cart" class="text-2xl" />
        <span class="text-xs">Cart</span>
      </div>
    </div>

      <div class="flex items-center md:ml-auto ">
        <input
          type="text"
          v-model="searchQuery"
          @keyup.enter="handleSearch"
          placeholder="Search..."
          class="border-2 border-gray-300 md:px-4 md:py-2 px-2 py-1  w-full lg:w-[300px] rounded-lg focus:rounded-lg outline-none hover:bg-gray-100"
        />

        <Button class="ml-2  md:py-5 md:px-3 px-2 font-medium" @click="handleSearch"><Search /></Button>
      </div>
      <div class="hidden md:flex ml-auto">
        <shopping-cart-header :cartItems="data" @click="handleGotoCart"/>
      </div>

      <template v-if="isLogged">
        <div class="flex space-x-2 mr-10 ml-auto">
          <UserNav />
        </div>
      </template>
      <template v-else>
        <div class="flex space-x-2 ml-auto">
          <Dialog>
            <DialogTrigger>
              <button class="border-2 border-black md:px-4 md:py-2 md:text-lg text-xs px-1 py-1  rounded-lg font-medium">
                Sign in
              </button>
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
              <button class="bg-black text-white md:px-4 md:py-2 md:text-lg text-xs px-1 py-1 rounded-lg font-medium">Sign up</button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
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
import { Search } from 'lucide-vue-next'
import ShoppingCartHeader from './cart/ShoppingCartHeader.vue'
import Container from './Container.vue'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from '../components/ui/dialog'
import { Button } from './ui/button'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import SignInForm from './SignInForm.vue'
import RegisterForm from './RegisterForm.vue'
import { useAuthStore } from '@/stores/authStore'
import { computed, onMounted, ref } from 'vue'
import UserNav from './UserNav.vue'
import type { CartItemResponse } from '@/apiTypes'
import CartApi from '@/api/CartApi'
import { useQuery } from '@tanstack/vue-query'
import { toast } from 'vue-sonner'


const route = useRoute()
const router = useRouter()

const authStore = useAuthStore()
const isLogged = computed(() => authStore.isLoggedIn)
const searchQuery = ref<string>('')
const cartItems = ref<CartItemResponse[]>([])

const {data, error, isLoading} = useQuery({
  queryKey: ['cartitems'],
  queryFn:  async () => {
    try {
      const response = await CartApi.getCart()
      return response.data
    } catch (error:any) {
      console.error('API request failed:', error)
      toast.error('API request failed:', error.message)
    }
  },
  
  refetchOnMount: true 
  
})

if (isLoading) {
  console.log('Loading cart items...')
}

if (error) {
  console.error('Error loading cart items:', error)
}

const handleGotoCart = () => {
  router.push('/cart')
}
const isActive = (path: string) => {
  return route.path === path
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/products', query: { ...route.query, search: searchQuery.value } })
  }
}

const menuItems = [
  {
    name: 'Home',
    path: '/',
    icon: 'ic:outline-other-houses'
  },
  {
    name: 'Products',
    path: '/products',
    icon: 'ic:baseline-shopify'
  },
  // {
  //   name: 'Stores',
  //   path: '/stores',
  //   icon: 'lucide:store'
  // }
]


</script>
<style lang="scss">
</style>