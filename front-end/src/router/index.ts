import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '@/views/ProductsView.vue'
import HomeLayout from '@/layouts/HomeLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeLayout,
      children:[
        {
          path: '',
          component: HomeView
        }

      ]
    },
    {
      path: '/product',
      name: 'products',
      component: HomeLayout,
      children: [
        {
          path: '',
          component: ProductsView
        }
      ]
    },
  ]
})

export default router
