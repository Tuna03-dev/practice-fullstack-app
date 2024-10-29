import ManagementShopApi from '@/api/ManagementShopApi'
import ShopApi from '@/api/ShopApi'
import type { ShopInformationType} from '@/apiTypes'
import { ref } from 'vue'

const loading = ref(false)
const error = ref<string | null>(null)
const shopInfo = ref<ShopInformationType | null>(null)


const fetchShopInfor = async (username: string) => {
  loading.value = true
  error.value = null
  try {
    const response = await ManagementShopApi.getShopInforByUsername(username) 
    if (response.code === 200) {
      shopInfo.value = response.data
    } else {
      error.value = response.message || 'Failed to fetch shop info'
    }
  } catch (err: any) {
    error.value = err.message || 'An error occurred'
  } finally {
    loading.value = false
  }
};

export const useShop = () => {
    return { shopInfo, fetchShopInfor, loading, error }
}
