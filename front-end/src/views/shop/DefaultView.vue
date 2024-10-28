<template >
  <div class="flex items-center justify-center p-4">
    <Card class="w-full max-w-2xl">
      <CardHeader>
        <CardTitle class="text-3xl font-bold text-center">Welcome to Shop Seller</CardTitle>
        <CardDescription class="text-center text-lg">
          Comprehensive store management solution for your business
        </CardDescription>
      </CardHeader>
      <CardContent class="space-y-4">
        <p class="text-center">
          Shop Seller provides powerful tools to help you manage your store efficiently:
        </p>
        <ul class="list-disc list-inside space-y-2 text-green-500">
          <li>Real-time inventory management</li>
          <li>Easy order processing and payments</li>
          <li>Detailed sales reports</li>
          <li>Customer and promotion management</li>
        </ul>
      </CardContent>
      <CardFooter class="flex justify-center">
        <Button @click="handleRedirect" v-if="isRedirecting">
          You need to complete your profile to continue
          <ArrowRight class="ml-2 h-4 w-4" />
        </Button>
      </CardFooter>
    </Card>
  </div>
</template>
<script lang="ts" setup>
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import { ArrowRight } from 'lucide-vue-next'
import { useShop } from '@/composables/useShop';
import { onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const {error, fetchShopInfor, loading, shopInfo} = useShop();
const isRedirecting = ref(false);
const router = useRouter();
const handleRedirect = () => {
    router.push('/management/shops/profile')
}

onMounted(async () => {
  await fetchShopInfor(authStore.username)
  if (shopInfo) {
    if(!shopInfo.value?.name || !shopInfo.value?.address || !shopInfo.value.categories || !shopInfo.value.description) {
        isRedirecting.value = true
    }
  }else{
    isRedirecting.value = true
  }
})

</script>
<style lang="">
</style>