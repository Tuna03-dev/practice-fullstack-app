<template >
  <div>
    <div class="my-10">
      <p class="text-2xl text-center font-bold mb-5">Description</p>
      <p class="text-xl text-center">{{ product.description }}</p>
      
      <img v-if="product.imageDescription" class="w-full object-cover" :src="product.imageDescription" alt="Product Image description">
    </div>

    <Card className="w-full max-w-3xl mx-auto shadow-lg">
      <CardHeader className="bg-gradient-to-r from-red-500 to-orange-500">
        <CardTitle className="text-2xl font-bold flex items-center gap-2 p-6 text-white">
          Product details
        </CardTitle>
      </CardHeader>
      <CardContent className="p-6">
        <div v-if="product.origin || product.material || product.brand || product.weight" className="space-y-6">
          <div v-if="product.origin" className="space-y-2 flex items-center justify-between">
            <h3 className="font-semibold text-lg flex items-center gap-2">
              <Globe className="w-5 h-5 text-primary" />
              Origin
            </h3>
            <p className="text-sm text-muted-foreground leading-relaxed">{{ product.origin }}</p>
          </div>
          <div v-if="product.material" className="space-y-2 flex items-center justify-between">
            <h3 className="font-semibold text-lg flex items-center gap-2">
              <InspectionPanel className="w-5 h-5 text-primary" />
              Material
            </h3>
            <p className="text-sm text-muted-foreground leading-relaxed">{{ product.material }}</p>
          </div>
          <div v-if="product.brand" className="space-y-2 flex items-center justify-between">
            <h3 className="font-semibold text-lg flex items-center gap-2">
              <Info className="w-5 h-5 text-primary" />
              Brand
            </h3>
            <p className="text-sm text-muted-foreground leading-relaxed">{{ product.brand }}</p>
          </div>
          <div v-if="product.weight" className="space-y-2 flex items-center justify-between">
            <h3 className="font-semibold text-lg flex items-center gap-2">
              <Weight className="w-5 h-5 text-primary" />
              Weight
            </h3>
            <p className="text-sm text-muted-foreground leading-relaxed">{{ product.weight }}</p>
          </div>
        </div>
        <div v-else>
          <p className="text-center text-gray-400 bg-gray-100 rounded-lg p-3 ">No details found</p>
        </div>
      </CardContent>
    </Card>
  </div>
</template>
<script setup lang="ts">
import type { ProductDetailResponse } from '@/apiTypes'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Weight, Globe, Info, InspectionPanel } from 'lucide-vue-next'
const props = defineProps<{
  product: ProductDetailResponse
}>()
</script>
<style lang="">
</style>