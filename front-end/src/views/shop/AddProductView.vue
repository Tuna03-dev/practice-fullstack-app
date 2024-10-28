<template>
  <div class="container mx-auto p-4 space-y-6">
    <h1 class="text-2xl font-bold mb-4">{{ isEditing ? 'Edit product' : 'Add new product' }}</h1>

    <form @submit="onSubmit" class="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle class="text-orange-400">Product Information</CardTitle>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="grid grid-cols-3 gap-4">
            <div class="col-span-3">
              <FormField v-slot="{ componentField }" name="productName">
                <FormItem>
                  <FormLabel>Product name</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter product name" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div class="col-span-3">
              <Label>Main images</Label>
              <div class="flex gap-2 mt-2 text-orange-400">
                <FormField name="mainImage">
                  <FormItem>
                    <FormLabel
                      class="border-2 hover:cursor-pointer hover:text-orange-500 hover:scale-105 transition-all border-dashed rounded-md w-24 h-24 flex flex-col items-center justify-center"
                      ><ImagePlus class="h-8 w-8" />
                      <span class="text-xs mt-1">Upload Image</span></FormLabel
                    >
                    <FormControl>
                      <Input multiple class="hidden" type="file" @change="previewMainImageHandle" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <div class="w-24 h-24" v-if="previewMainImage">
                  <img
                    :src="previewMainImage"
                    alt="Selected Image"
                    class="w-full h-full object-cover rounded-md"
                  />
                </div>
              </div>
            </div>
            <div class="col-span-3">
              <Label>Product images</Label>
              <div class="flex gap-2 mt-2 text-orange-400">
                <FormField name="productImages">
                  <FormItem>
                    <FormLabel
                      class="border-2 hover:cursor-pointer hover:text-orange-500 hover:scale-105 transition-all border-dashed rounded-md w-24 h-24 flex flex-col items-center justify-center"
                      ><ImagePlus class="h-8 w-8" />
                      <span class="text-xs mt-1">Upload Image</span></FormLabel
                    >
                    <FormControl>
                      <Input
                        placeholder="Enter product name"
                        multiple
                        class="hidden"
                        type="file"
                        @change="previewImages"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
                <div class="flex gap-2">
                  <div v-for="(image, index) in previewImagesList" :key="index" class="w-24 h-24">
                    <img
                      :src="image"
                      alt="Selected Image"
                      class="w-full h-full object-cover rounded-md"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="w-52">
            <FormField v-slot="{ componentField }" name="productCategory">
              <FormItem>
                <FormLabel>Category</FormLabel>
                <Select v-bind="componentField">
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select category" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem
                      v-for="category in categories"
                      :key="category.id"
                      :value="category.id"
                      >{{ category.name }}</SelectItem
                    >
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
          <div class="w-52">
            <FormField v-slot="{ value }" name="stockQuantity">
              <FormItem>
                <FormLabel>Stock quantity</FormLabel>
                <NumberField
                  class="gap-2"
                  :min="1"
                  :default-value="100"
                  :model-value="value"
                  @update:model-value="
                    (v) => {
                      if (v) {
                        form.setFieldValue('stockQuantity', v)
                      } else {
                        form.setFieldValue('stockQuantity', undefined)
                      }
                    }
                  "
                >
                  <NumberFieldContent>
                    <NumberFieldDecrement />
                    <FormControl>
                      <NumberFieldInput />
                    </FormControl>
                    <NumberFieldIncrement />
                  </NumberFieldContent>
                </NumberField>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
          <div class="w-52">
            <FormField v-slot="{ value }" name="price">
              <FormItem>
                <FormLabel>Price</FormLabel>
                <NumberField
                  class="gap-2"
                  :default-value="1500"
                  :min="0"
                  :step="1000"
                  :format-options="{ style: 'currency', currency: 'VND' }"
                  :model-value="value"
                  @update:model-value="
                    (v) => {
                      if (v) {
                        form.setFieldValue('price', v)
                      } else {
                        form.setFieldValue('price', undefined)
                      }
                    }
                  "
                >
                  <NumberFieldContent>
                    <NumberFieldDecrement />
                    <FormControl>
                      <NumberFieldInput />
                    </FormControl>
                    <NumberFieldIncrement />
                  </NumberFieldContent>
                </NumberField>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
          <div>
            <Label for="description">Description</Label>
            <div class="flex gap-2 mt-2 text-orange-400 bg-gray-50 my-2 items-center">
              <FormField name="imageDescription">
                <FormItem>
                  <FormLabel
                    class="border-2 bg-white hover:cursor-pointer hover:text-orange-500 hover:scale-105 transition-all border-dashed rounded-md w-24 h-24 flex flex-col items-center justify-center"
                    ><ImagePlus class="h-8 w-8" />
                    <span class="text-xs mt-1">Upload Image</span></FormLabel
                  >
                  <FormControl>
                    <Input multiple class="hidden" type="file" @change="previewImagesDes" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>

              <div class="flex gap-2" v-if="previewImagesDescription">
                <div class="w-24 h-24">
                  <img
                    :src="previewImagesDescription"
                    alt="Selected Image"
                    class="w-full h-full object-cover rounded-md"
                  />
                </div>
              </div>
              <span v-else>You can use image to describe your product</span>
            </div>
            <FormField v-slot="{ componentField }" name="description">
              <FormItem>
                <FormControl>
                  <Textarea
                    placeholder="Enter product description"
                    class="w-full h-36"
                    v-bind="componentField"
                  ></Textarea>
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle class="text-orange-400">Product Details</CardTitle>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <FormField v-slot="{ componentField }" name="brand">
                <FormItem>
                  <FormLabel>Brand</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter brand" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div>
              <FormField v-slot="{ componentField }" name="origin">
                <FormItem>
                  <FormLabel>Origin</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter origin" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div>
              <FormField v-slot="{ componentField }" name="material">
                <FormItem>
                  <FormLabel>Material</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter material" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div>
              <FormField v-slot="{ componentField }" name="weight">
                <FormItem>
                  <FormLabel>Weight</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter weight" v-bind="componentField" />
                  </FormControl>
                  <FormDescription />
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
          </div>
        </CardContent>
      </Card>

      <div class="flex justify-end gap-4 mt-4">
        <Button type="submit" class="w-24 bg-orange-400 hover:bg-orange-500">Save</Button>
      </div>
    </form>
  </div>
</template>
  
<script setup lang="ts">
import { onMounted, ref } from 'vue'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { ImagePlus } from 'lucide-vue-next'
import type { CategoryResponse, ProductCreationRequest, ProductDetailResponse } from '@/apiTypes'
import CategoryApi from '@/api/CategoryApi'
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput
} from '@/components/ui/number-field'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormDescription,
  FormMessage
} from '@/components/ui/form'
import { z } from 'zod'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import useStorage from '@/composables/useStorage'
import ShopApi from '@/api/ShopApi'
import { toast } from 'vue-sonner'
import { watch } from 'vue'
import { useRoute } from 'vue-router'
import ProductApi from '@/api/ProductApi'


const route = useRoute()
const isEditing = ref(false)
const { productUrl, uploadProductImage, fetchImageMetadata } = useStorage('products')
const categories = ref<CategoryResponse[]>([])
const previewImagesList = ref<string[]>([])
const previewImagesDescription = ref<string>()
const previewMainImage = ref<string>()
const productCreationRequest = ref<ProductCreationRequest>({} as ProductCreationRequest)
const updatedProduct = ref<ProductDetailResponse>({} as ProductDetailResponse)
const formSchema = z.object({
  productName: z
    .string()
    .min(1, { message: 'Product name is required' })
    .max(255, { message: 'Product name should not exceed 255 characters' }),
  description: z.string().min(1, { message: 'Description is required' }),
  brand: z.string().optional(),
  origin: z.string().optional(),
  material: z.string().optional(),
  weight: z.string().optional(),
  price: z.number().min(1, { message: 'Price is required' }),
  stockQuantity: z.number().min(1, { message: 'Stock quantity is required' }),
  productCategory: z.string(),
  productImages: z.instanceof(FileList, { message: 'Product images is required' }),
  mainImage: z.instanceof(File),
  imageDescription: z.instanceof(File).optional()
})

const form = useForm({
  validationSchema: toTypedSchema(formSchema)
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    const uploadedUrls: string[] = []

    let mainImageUrl: string = ''
    const mainImage = await uploadProductImage(values.mainImage, values.productName)

    if (mainImage) mainImageUrl = mainImage.value

    const uploadPromises = Array.from(values.productImages).map(async (element) => {
      const url = await uploadProductImage(element, values.productName)
      uploadedUrls.push(url?.value || '')
    })

    await Promise.all(uploadPromises)

    if (values.imageDescription) {
      const descriptionUrl = await uploadProductImage(values.imageDescription, values.productName)
      uploadedUrls.push(descriptionUrl?.value || '')
    }

    productCreationRequest.value = {
      name: values.productName,
      description: values.description,
      brand: values.brand,
      origin: values.origin,
      material: values.material,
      weight: values.weight,
      price: values.price,
      stockQuantity: values.stockQuantity,
      categoryId: values.productCategory,
      listImages: uploadedUrls,
      imageDescription: values.imageDescription ? uploadedUrls[uploadedUrls.length - 1] : '',
      image: mainImageUrl
    }

    if (isEditing.value) {
      const response = await ShopApi.updateProduct(productCreationRequest.value, route.params.id as string)
      if (response.code === 200) {
        toast.success('Product updated successfully')
        form.resetForm()
      }
    }else if (isEditing.value === false) {
      const response = await ShopApi.addNewProduct(productCreationRequest.value)
      if (response.code === 200) {
        toast.success('Product added successfully')
        form.resetForm()
      } else {
        toast.error('Failed to add product, product name already exists')
      }
    }
  } catch (error) {
    toast.error('Failed to add product, product name already exists')
    console.log(error)
  }
})
const previewImages = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files) return

  previewImagesList.value = []

  for (let i = 0; i < input.files.length; i++) {
    const file = input.files[i]
    previewImagesList.value.push(URL.createObjectURL(file))
  }
  form.setFieldValue('productImages', input.files)
}

const previewMainImageHandle = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files) return

  previewMainImage.value = ''
  const file = input.files[0]
  previewMainImage.value = URL.createObjectURL(file)
  form.setFieldValue('mainImage', file)
}

const previewImagesDes = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files) return

  previewImagesDescription.value = ''
  const file = input.files[0]
  previewImagesDescription.value = URL.createObjectURL(file)
  form.setFieldValue('imageDescription', file)
}

const fetchCategory = async () => {
  try {
    const response = await CategoryApi.getAllCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error: any) {
    console.log(error)
  }
}

function filesToFileList(files: File[]): FileList {
  const dataTransfer = new DataTransfer()
  files.forEach((file) => dataTransfer.items.add(file))
  return dataTransfer.files
}

async function urlToFile(url: string, filename: string, mimeType: string): Promise<File> {
  const response = await fetch(url);
  const blob = await response.blob();
  return new File([blob], filename, { type: mimeType });
}

onMounted(async () => {
  fetchCategory()
  if (route.params.id) {
    isEditing.value = true
    const productId = route.params.id as string
    const response = await ProductApi.getProductDetails(productId)
    updatedProduct.value = response.data
    console.log(response.data)
    const productImagesFileList = filesToFileList(
      updatedProduct.value.productImages
        ? await Promise.all(
            updatedProduct.value.productImages.map(async (image: any) => {
              const mimeType = await fetchImageMetadata(image.url);
              return await urlToFile(image.url, 'productImage.jpg', mimeType || 'image/jpeg');
            })
          )
        : []
    );
    console.log(productImagesFileList);

    const mainImageMimeType = updatedProduct.value.image ? (await fetchImageMetadata(updatedProduct.value.image))  : 'image/png';
    const imageDescriptionMimeType = updatedProduct.value.imageDescription ? (await fetchImageMetadata(updatedProduct.value.imageDescription))  : 'image/png';

    form.setValues({
      productName: updatedProduct.value.name || '',
      description: updatedProduct.value.description || '',
      brand: updatedProduct.value.brand || '',
      origin: updatedProduct.value.origin || '',
      material: updatedProduct.value.material || '',
      weight: updatedProduct.value.weight || '',
      price: Number(updatedProduct.value.price) || 0,
      stockQuantity: updatedProduct.value.stockQuantity || 0,
      productCategory: updatedProduct.value.categoryId || '',
      productImages: productImagesFileList,
      mainImage: updatedProduct.value.image ? (await urlToFile(updatedProduct.value.image, 'productImage.jpg', mainImageMimeType || 'image/jpeg')) : undefined,
      imageDescription: updatedProduct.value.imageDescription ? (await urlToFile(updatedProduct.value.imageDescription, 'productImage.jpg', imageDescriptionMimeType || 'image/jpeg')) : undefined
    })
    previewImagesList.value = updatedProduct.value.productImages?.map((image) => image.url) || []
    previewMainImage.value = updatedProduct.value.image
    previewImagesDescription.value = updatedProduct.value.imageDescription
    console.log(form.values.mainImage);
  } else {
    isEditing.value = false
  }
})
</script>
  