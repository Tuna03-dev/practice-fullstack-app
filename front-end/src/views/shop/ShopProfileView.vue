<template>
  <div class="container mx-auto py-10">
    <form @submit="handleSubmit" class="space-y-6">
      <Card class="max-w-3xl mx-auto">
        <CardHeader class="text-3xl text-orange-400">
          <CardTitle>Store Profile Management</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="space-y-2">
            <Label for="avatar">Store Avatar</Label>
            <div class="flex items-center space-x-4">
              <Avatar class="w-20 h-20">
                <AvatarImage :src="avatarPreview || '/placeholder.svg'" alt="Store Avatar" />
                <AvatarFallback>ST</AvatarFallback>
              </Avatar>
              <Label for="avatar-upload" class="cursor-pointer">
                <div
                  class="flex items-center space-x-2 bg-secondary text-secondary-foreground px-4 py-2 rounded-md"
                >
                  <Upload :size="16" />
                  <span>Upload Avatar</span>
                </div>
                <Input
                  id="avatar-upload"
                  type="file"
                  accept="image/*"
                  class="hidden"
                  @change="handleAvatarChange"
                />
              </Label>
            </div>
          </div>

          <div class="space-y-2">
            <FormField v-slot="{ componentField }" name="name">
              <FormItem>
                <FormLabel>Store name</FormLabel>
                <FormControl>
                  <Input type="text" placeholder="Enter store name" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>

          <div class="space-y-2">
            <FormField v-slot="{ componentField }" name="address">
              <FormItem>
                <FormLabel>Store Adress</FormLabel>
                <FormControl>
                  <Input type="text" placeholder="Enter store address" v-bind="componentField" />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>

          
          <div class="space-y-2">
            <FormField v-slot="{ componentField }" name="description">
              <FormItem>
                <FormLabel>Description</FormLabel>
                <FormControl>
                  <ckeditor
                    :v-model="componentField"
                    :editor="editor"
                    v-bind="componentField"
                  ></ckeditor>
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
          </div>

          <div class="space-y-2">
            <Label for="description-image">Description Image</Label>
            <div class="flex items-center space-x-4">
              <img
                v-if="descriptionImage"
                :src="descriptionImagePreview || '/placeholder.svg'"
                alt="Description"
                class="w-40 h-40 object-cover rounded-md"
              />
              <div
                v-else
                class="w-40 h-40 bg-gray-100 text-gray-500 flex items-center justify-center rounded-md"
              >
                <ImageIcon :size="40" class="text-muted-foreground" />
              </div>
              <Label for="description-image-upload" class="cursor-pointer">
                <div
                  class="flex items-center space-x-2 bg-gray-100 text-gray-500 px-4 py-2 rounded-md"
                >
                  <Upload :size="16" />
                  <span>Upload Image</span>
                </div>
                <Input
                  id="description-image-upload"
                  type="file"
                  accept="image/*"
                  class="hidden"
                  @change="handleDescriptionImageChange"
                />
              </Label>
            </div>
          </div>
        </CardContent>
        <CardFooter>
          <Button type="submit" class="w-full">Save Changes</Button>
        </CardFooter>
      </Card>
    </form>
  </div>
</template>
  
  <script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Button } from '@/components/ui/button'
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
import { Textarea } from '@/components/ui/textarea'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormDescription,
  FormMessage
} from '@/components/ui/form'
import { Upload, Image as ImageIcon } from 'lucide-vue-next'
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import { useForm } from 'vee-validate'
import useStorage from '@/composables/useStorage'
import { useShop } from '@/composables/useShop'
import { useAuthStore } from '@/stores/authStore'
import { getDownloadURL, ref as storageRef, getBlob } from 'firebase/storage'
import { projectStorage } from '@/configs/firebase'
import { Ckeditor } from '@ckeditor/ckeditor5-vue'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic'

const editor = ClassicEditor

const avatar = ref<File | null>(null)
const avatarPreview = ref<string | null>(null)
const descriptionImagePreview = ref<string | null>(null)
const descriptionImage = ref<File | null>(null)
const { uploadFile, uploadHtmlContent, fetchImageMetadata } = useStorage('shops')
const { error, fetchShopInfor, loading, shopInfo } = useShop()
const authStore = useAuthStore()

const formSchema = toTypedSchema(
  z.object({
    name: z.string().min(1, { message: 'Name is required' }),
    address: z.string().min(1, { message: 'Address is required' }),
    description: z.string().min(50, { message: 'Description is required' })
  })
)

const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    name: shopInfo.value?.name || '',
    address: shopInfo.value?.address || '',
    description: shopInfo.value?.description || ''
  }
})



const handleAvatarChange = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (file) {
    const reader = new FileReader()
    avatar.value = file
    reader.readAsDataURL(file)
    reader.onload = () => {
      avatarPreview.value = reader.result as string
    }
  }
}

const handleDescriptionImageChange = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (file) {
    const reader = new FileReader()
    descriptionImage.value = file
    reader.readAsDataURL(file)
    reader.onload = () => {
      descriptionImagePreview.value = reader.result as string
    }
  }
}

const handleSubmit = form.handleSubmit(async (values) => {
  try {
    let avaterUrl = '';
    let descriptionImageUrl = '';
    let descriptionUrl = '';
    const imageUrl = avatar.value ? await uploadFile(avatar.value) : null

    if (imageUrl) {
      avaterUrl = imageUrl.value
    }
    const description = descriptionImage.value
      ? await uploadFile(descriptionImage.value)
      : null
    
    if (description) {
      descriptionImageUrl = description.value
    }

    const urlDescription = await uploadHtmlContent(values.description);

    if (urlDescription) {
      descriptionUrl = urlDescription.value
    }
    console.log(avaterUrl, descriptionImageUrl, descriptionUrl);
    
  } catch (err) {
    console.log(err)
  }
})
const loadDescriptionFromUrl = async (filePath: string) => {
  try {
    const fileRef = storageRef(projectStorage, filePath)
    const blob = await getBlob(fileRef)
    const text = await blob.text()
    return text
  } catch (error) {
    console.error('Error loading description:', error)
    return null
  }
}

async function urlToFile(url: string, filename: string, mimeType: string): Promise<File> {
  const response = await fetch(url);
  const blob = await response.blob();
  return new File([blob], filename, { type: mimeType });
}

onMounted(async () => {
  await fetchShopInfor(authStore.username)
  if (shopInfo) {
    avatarPreview.value = shopInfo.value?.imageUrl || null
    descriptionImagePreview.value = shopInfo.value?.descriptionImage || null
    const avaterMineType = await fetchImageMetadata(shopInfo.value?.imageUrl || '')
    const descriptionMineType = await fetchImageMetadata(shopInfo.value?.descriptionImage || '')
    if (avaterMineType) {
      avatar.value = await urlToFile(shopInfo.value?.imageUrl || '', 'avatar', avaterMineType)
    }
    if (descriptionMineType) {
      descriptionImage.value = await urlToFile(shopInfo.value?.descriptionImage || '', 'descriptionImage', descriptionMineType)
    }

    form.setFieldValue('name', shopInfo.value?.name || '')
    form.setFieldValue('address', shopInfo.value?.address || '')
    const descriptionText = await loadDescriptionFromUrl(shopInfo.value?.description || '')
    form.setFieldValue('description', descriptionText || '')
  }
})
</script>
  
  