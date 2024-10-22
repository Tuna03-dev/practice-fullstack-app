<template >
  <div class="flex flex-col h-screen">
    <!-- Top Navigation Bar -->
    <header
      class="bg-[#ee4d2d] text-white h-14 flex items-center px-4 fixed top-0 left-0 right-0 z-50"
    >
      <div class="flex items-center space-x-4 flex-grow">
        <ShoppingBag :size="38" color="white" strokeWidth="1.75" />
        <span class="font-semibold text-2xl">Shop Seller</span>
      </div>
      <div class="flex items-center space-x-4">
        <Button variant="ghost" size="icon" class="text-white">
          <Bell class="h-5 w-5" />
        </Button>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" class="text-white">
              <img
                :src="
                  shopInfor?.imageUrl
                    ? shopInfor.imageUrl
                    : '../public/images/shops/default-avatar.png'
                "
                alt="User Avatar"
                class="h-8 w-8 rounded-full mr-2"
              />
              <span>{{ shopInfor?.name }}</span>
              <ChevronDown class="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuItem>Profile</DropdownMenuItem>
            <DropdownMenuItem>Settings</DropdownMenuItem>
            <DropdownMenuItem>Logout</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>

    <div class="flex flex-grow pt-14">
      <aside
        :class="[
          isCollapsed ? 'w-20' : 'w-64',
          'bg-white shadow-lg transition-all duration-300 fixed left-0 top-14 bottom-0 z-40'
        ]"
      >
        <div class="flex flex-col h-full">
          <div class="p-4 flex justify-between items-center border-b">
            <h2 :class="['font-bold text-lg', isCollapsed && 'sr-only']">Menu Management</h2>
            <Button
              variant="ghost"
              size="icon"
              @click="isCollapsed = !isCollapsed"
              :aria-label="isCollapsed ? 'Expand sidebar' : 'Collapse sidebar'"
            >
              <ChevronRight
                :class="['h-4 w-4 transition-transform', !isCollapsed && 'rotate-180']"
              />
            </Button>
          </div>
          <nav class="flex-1 overflow-y-auto">
            <ul class="p-2 space-y-1">
              <li v-for="(item, index) in menuItems" :key="index">
                <Collapsible v-if="item.items">
                  <CollapsibleTrigger asChild>
                    <Button variant="ghost" class="w-full justify-between font-normal">
                      <span class="flex items-center">
                        <component :is="item.icon" class="h-4 w-4 mr-2" />
                        <span :class="['text-sm', isCollapsed && 'sr-only']">{{ item.label }}</span>
                      </span>
                      <ChevronDown class="h-4 w-4" />
                    </Button>
                  </CollapsibleTrigger>
                  <CollapsibleContent class="space-y-1">
                    <Button
                      v-for="(subItem, subIndex) in item.items"
                      :key="subIndex"
                      variant="ghost"
                      class="w-full justify-start pl-9 font-normal"
                      @click="navigateTo(subItem.href)"
                    >
                      <span :class="['text-sm', isCollapsed && 'sr-only']">{{
                        subItem.label
                      }}</span>
                    </Button>
                  </CollapsibleContent>
                </Collapsible>
                <Button v-else variant="ghost" class="w-full justify-start font-normal">
                  <component :is="item.icon" class="h-4 w-4 mr-2" />
                  <span :class="['text-sm', isCollapsed && 'sr-only']">{{ item.label }}</span>
                </Button>
              </li>
            </ul>
          </nav>
        </div>
      </aside>

      <main :class="[isCollapsed ? 'ml-16' : 'ml-64', 'flex-grow p-6 transition-all duration-300']">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { RouterView, useRouter } from 'vue-router'
import { defineComponent, onMounted, ref } from 'vue'
import {
  Bell,
  ChevronDown,
  ChevronRight,
  LayoutDashboard,
  ShoppingCart,
  Package,
  Megaphone,
  Users,
  FileText,
  BarChart2,
  Settings,
  HelpCircle,
  ShoppingBag
} from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { useAuthStore } from '@/stores/authStore'
import type { ShopInformationType, UserProfileResponse } from '@/apiTypes'
import UserApi from '@/api/UserApi'
import ShopApi from '@/api/ShopApi'
import ManagementShopApi from '@/api/ManagementShopApi'

type MenuItem = {
  icon: any
  label: string
  items?: { label: string; href: string }[]
}

const isCollapsed = ref(false)

const menuItems: MenuItem[] = [
  {
    icon: Settings,
    label: 'Manage Shop',
    items: [
      { label: 'Shop profile', href: '#' },
      { label: 'Danh mục của Shop', href: '#' },
      { label: 'Kho hình ảnh/Video', href: '#' },
      { label: 'Báo cáo của tôi', href: '#' }
    ]
  },
  { icon: ShoppingCart, label: 'Manage Orders' },
  {
    icon: Package,
    label: 'Manage Products',
    items: [
      { label: 'List Products', href: '#' },
      { label: 'Add Products', href: '/management/shops/products/add' }
    ]
  },
  { icon: Megaphone, label: 'Kênh Marketing' },
  { icon: Users, label: 'Quản lý khách hàng' },
  { icon: FileText, label: 'Tài chính' },
  { icon: BarChart2, label: 'Phát triển' },

  { icon: HelpCircle, label: 'Trợ giúp' }
]

const authStore = useAuthStore()
const shopInfor = ref<ShopInformationType>()
const router = useRouter();
const fetchShopInformation = async () => {
  try {
    const response = await ManagementShopApi.getShopInforByUsername(authStore.username)
    if (response.code === 200) {
      console.log(response.data)
      shopInfor.value = response.data
    }
  } catch (error: any) {
    console.error('Failed to fetch user profile:', error)
  }
}

const navigateTo = (href: string) => {
    router.push(href);
}

onMounted(() => {
  fetchShopInformation()
})
</script>
<style lang="">
</style>