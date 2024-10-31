<template >
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
      <Card class="w-full max-w-md">
        <CardHeader>
          <CardTitle class="text-center text-2xl font-bold">Order successful!</CardTitle>
        </CardHeader>
        <CardContent class="text-center">
          <CheckCircle class="mx-auto text-green-500 w-16 h-16 mb-4" />
          <p class="text-gray-600 mb-4">
            Thank you for your order. We will process your order as soon as possible.
          </p>

        </CardContent>
        <CardFooter class="flex justify-center">
          <Button class="bg-orange-400 hover:bg-orange-500" asChild>
            <router-link :to="'/'">Back to home page</router-link>
          </Button>
        </CardFooter>
      </Card>
    </div>
</template>
<script lang="ts" setup>
import Button from '@/components/ui/button/Button.vue';
import Card from '@/components/ui/card/Card.vue';
import CardContent from '@/components/ui/card/CardContent.vue';
import CardFooter from '@/components/ui/card/CardFooter.vue';
import CardHeader from '@/components/ui/card/CardHeader.vue';
import CardTitle from '@/components/ui/card/CardTitle.vue';
import { useCartStore } from '@/stores/cartStore';
import { CheckCircle} from 'lucide-vue-next';
import { onUnmounted } from 'vue';

const cartStore = useCartStore()

onUnmounted(()=> {
    cartStore.setIsOrder(false);

})
</script>
<style lang="">
    
</style>