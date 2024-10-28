import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '@/views/ProductsView.vue'
import HomeLayout from '@/layouts/HomeLayout.vue'
import PersonalInformationView from '@/views/PersonalInformationView.vue'
import { useAuthStore } from '@/stores/authStore'
import { record } from 'zod'
import { toast } from 'vue-sonner'
import ProfileLayout from '@/layouts/ProfileLayout.vue'
import ProductDetailsView from '@/views/ProductDetailsView.vue'
import ProductDetailLayout from '@/layouts/ProductDetailLayout.vue'
import CartView from '@/views/CartView.vue'
import UserAddressView from '@/views/UserAddressView.vue'
import ShopManagementLayout from '@/layouts/ShopManagementLayout.vue'
import ProductTableView from '@/views/shop/ProductTableView.vue'
import AddProductView from '@/views/shop/AddProductView.vue'
import ShopProfileView from '@/views/shop/ShopProfileView.vue'

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
      path: '/products',
      name: 'products',
      component: ProductDetailLayout,
      children: [
        {
          path: '',
          component: ProductsView
        },
        {
          path: 'details/:id',
          component: ProductDetailsView
        }
      ]
    },
    {
      path: '/profile',
      name: 'profile',
      component: HomeLayout,
      meta:{requireAuth: true, role: 'CUSTOMER'},
      children: [
        {
          path: '',
          component: ProfileLayout,
          children: [
            {
              path: 'personal-information',
              component: PersonalInformationView
            }
          ]
        },
        {
          path: '',
          component: ProfileLayout,
          children: [
            {
              path: 'address',
              component: UserAddressView
            }
          ]
        }
      ]
    },
    {
      path: '/stores',
      name: 'stores',
      component: HomeLayout,
      children: [
        {
          path: ':id',
          component: () => import('@/views/StoreDetailView.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue')
    },
    {
      path: '/cart',
      name: 'cart',
      component: HomeLayout,
      children: [
        {
          path: '',
          component: CartView
        }
      ]
    },
    {
      path: '/management/shops',
      name: 'manage-shop',
      component: ShopManagementLayout,
      meta: {requireAuth: true, role: 'SHOP'},
      children: [
        {
          path: 'products',
          component: ProductTableView
        },
        {
          path: 'products/add/',
          component: AddProductView
        },
        {
          path: 'products/edit/:id',
          component: AddProductView
        },
        {
          path: 'profile',
          component: ShopProfileView
        }
      ]
    }
    
  ]
})


router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isLoggedIn;
  const userRole = authStore.userRole;


  if(to.matched.some(record => record.meta.requireAuth)){
    if(!isAuthenticated){
      toast.error("You don't have permission to access this page");
      return next({path: '/'});
    }

    const requiredRole = to.meta.role;
    if(requiredRole && userRole !== requiredRole){
      toast.error("You don't have permission to access this page");

      return next({path: '/'});

    }

  }

  next();
})



export default router
