<template>
  <div class="container mx-auto p-4 space-y-6">
    <h1 class="text-2xl font-bold mb-4">Thêm sản phẩm mới</h1>

    <Card>
      <CardHeader>
        <CardTitle>Product Information</CardTitle>
      </CardHeader>
      <CardContent class="space-y-4">
        <div class="grid grid-cols-3 gap-4">
          <div class="col-span-2">
            <Label for="productName">Product name</Label>
            <Input id="productName" v-model="productName" placeholder="Enter product name" />
          </div>
          <div>
            <Label>Product images</Label>
            <div class="flex gap-2 mt-2">
              <Button
                variant="outline"
                class="w-24 h-24 flex flex-col items-center justify-center"
              >
                <ImagePlus class="h-8 w-8 text-gray-400" />
                <span class="text-xs mt-1">Upload Image</span>
              </Button>
            </div>
          </div>
        </div>
        <div>
            <Label for="category">Category</Label>
            <Select v-model="brand">
              <SelectTrigger id="category">
                <SelectValue placeholder="Select category" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="brand1">Thương hiệu 1</SelectItem>
                <SelectItem value="brand2">Thương hiệu 2</SelectItem>
              </SelectContent>
            </Select>
        </div>
        <div>
          <Label for="description">Description</Label>
          <Textarea id="description" v-model="description" placeholder="Enter product description"></Textarea>
        </div>
        
      </CardContent>
    </Card>

    <Card>
      <CardHeader>
        <CardTitle>Product Details</CardTitle>
      </CardHeader>
      <CardContent class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <Label for="brand">Brand</Label>
            <Input id="brand" v-model="brand" placeholder="Enter brand" />
            
          </div>
          <div>
            <Label for="origin">Origin</Label>
            <Input id="origin" v-model="brand" placeholder="Enter origin" />
          </div>
          <div>
            <Label for="material">Material</Label>
            <Input id="material" v-model="brand" placeholder="Enter material" />
            
          </div>
          <div>
            <Label for="weight">Weight</Label>
            <Input id="weight" v-model="style" placeholder="Enter weight" />
          </div>
        </div>
      </CardContent>
    </Card>

    <Card>
      <CardHeader>
        <CardTitle>Sales information</CardTitle>
      </CardHeader>
      <CardContent class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <Label for="weight">Cân nặng</Label>
            <Input id="weight" v-model="weight" type="number" placeholder="Nhập cân nặng (gram)" />
          </div>
          <div>
            <Label for="dimensions">Kích thước đóng gói</Label>
            <div class="flex gap-2">
              <Input v-model="dimensions.length" placeholder="Dài" />
              <Input v-model="dimensions.width" placeholder="Rộng" />
              <Input v-model="dimensions.height" placeholder="Cao" />
            </div>
          </div>
        </div>
        <div>
          <Label for="shippingFee">Phí vận chuyển</Label>
          <Select v-model="shippingFee">
            <SelectTrigger id="shippingFee">
              <SelectValue placeholder="Chọn loại phí vận chuyển" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="free">Miễn phí vận chuyển</SelectItem>
              <SelectItem value="flat">Phí cố định</SelectItem>
              <SelectItem value="weight">Theo cân nặng</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </CardContent>
    </Card>

    <Card>
      <CardHeader>
        <CardTitle>Thông tin khác</CardTitle>
      </CardHeader>
      <CardContent class="space-y-4">
        <div>
          <Label for="sku">Mã SKU</Label>
          <Input id="sku" v-model="sku" placeholder="Nhập mã SKU" />
        </div>
        <div>
          <Label for="barcode">Mã vạch (ISBN, UPC, GTIN, etc.)</Label>
          <Input id="barcode" v-model="barcode" placeholder="Nhập mã vạch" />
        </div>
        <div>
          <Label for="condition">Tình trạng sản phẩm</Label>
          <Select v-model="condition">
            <SelectTrigger id="condition">
              <SelectValue placeholder="Chọn tình trạng" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="new">Mới</SelectItem>
              <SelectItem value="used">Đã sử dụng</SelectItem>
              <SelectItem value="refurbished">Tân trang</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </CardContent>
    </Card>

    <div class="flex justify-end gap-4">
      <Button variant="outline">Lưu & Ẩn</Button>
      <Button>Lưu & Hiện</Button>
    </div>
  </div>
</template>
  
  <script setup lang="ts">
import { ref } from 'vue'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ImagePlus, VideoIcon } from 'lucide-vue-next'

const productName = ref('')
const description = ref('')
const brand = ref('')
const origin = ref('')
const material = ref('')
const style = ref('')
const weight = ref(0)
const dimensions = ref({ length: '', width: '', height: '' })
const shippingFee = ref('')
const sku = ref('')
const barcode = ref('')
const condition = ref('')
</script>
  