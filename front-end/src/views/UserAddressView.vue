<template>
  <Card class="w-full max-w-4xl mx-auto">
    <CardHeader>
      <CardTitle class="text-3xl font-bold text-center mb-6 text-orange-400"
        >My Addresses</CardTitle
      >
      <Button @click="isAdding = !isAdding" class="w-full mb-4 bg-orange-500 hover:bg-orange-400">
        <Plus class="mr-2 h-4 w-4" /> Add New Address
      </Button>
    </CardHeader>
    <CardContent>
      <div
        v-motion
        :initial="{ opacity: 0, y: -100 }"
        :enter="{ opacity: 1, y: 0, transition: { duration: 300 } }"
        v-if="isAdding"
      >
        <form
          @submit="onSubmit"
          class="grid grid-cols-2 gap-x-4 gap-y-2 mb-6 p-4 bg-secondary rounded-lg"
        >
          <FormField v-slot="{ componentField }" name="name">
            <FormItem class="mb-2">
              <FormControl>
                <Input type="text" placeholder="Name" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="phone">
            <FormItem class="mb-2">
              <FormControl>
                <Input type="text" placeholder="Phone" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="province">
            <FormItem>
              <Select v-bind="componentField">
                <SelectTrigger class="mb-2">
                  <SelectValue placeholder="Select a city" />
                </SelectTrigger>
                <SelectContent class="absolute z-100">
                  <SelectGroup>
                    <SelectLabel>City</SelectLabel>
                    <SelectItem
                      v-for="province in provinces"
                      :key="province.name"
                      :value="province.name"
                      >{{ province.name }}</SelectItem
                    >
                  </SelectGroup>
                </SelectContent>
              </Select>

              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="district">
            <FormItem>
              <Select v-bind="componentField" :disabled="!form.values.province">
                <SelectTrigger class="mb-2">
                  <SelectValue placeholder="Select a distrcit" />
                </SelectTrigger>
                <SelectContent class="absolute z-100">
                  <SelectGroup>
                    <SelectLabel>District</SelectLabel>
                    <SelectItem
                      v-for="district in districts"
                      :key="district.name"
                      :value="district.name"
                      >{{ district.name }}</SelectItem
                    >
                  </SelectGroup>
                </SelectContent>
              </Select>

              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="ward">
            <FormItem>
              <Select v-bind="componentField" :disabled="!form.values.district">
                <SelectTrigger class="mb-2">
                  <SelectValue placeholder="Select a ward" />
                </SelectTrigger>
                <SelectContent class="absolute z-100">
                  <SelectGroup>
                    <SelectLabel>Ward</SelectLabel>
                    <SelectItem v-for="ward in wards" :key="ward.name" :value="ward.name">{{
                      ward.name
                    }}</SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>

              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="street">
            <FormItem>
              <FormControl>
                <Input type="text" placeholder="Street" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>

          <div class="flex justify-center items-center col-span-2">
            <Button class="w-[50%] bg-emerald-300">Save Address</Button>
          </div>
        </form>
      </div>

      <div v-if="addresses.length > 0">
        <div
          v-for="address in addresses"
          :key="address.id"
          class="mb-4 p-4 rounded-lg"
          :class="address.defaultAddress ? 'bg-primary ' : 'bg-card'"
        >
          <div class="flex justify-between items-start mb-2">
            <h3 class="text-xl font-semibold">{{ address.name }}</h3>
            <div>
              <span v-if="address.defaultAddress" class="text-xs">Default</span>
              <AlertDialog>
                <AlertDialogTrigger as-child>
                  <Button variant="ghost" size="icon">
                    <Star
                      :class="[address.defaultAddress ? 'text-[#FFB91E]' : '', 'h-4 w-4']"
                      :fill="address.defaultAddress ? '#FFB91E' : 'none'"
                    />
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle>Are you want to set as default?</AlertDialogTitle>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction @click="setDefaultAdress(address.id)">Accept</AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>

              <Button variant="ghost" size="icon">
                <Edit2 class="h-4 w-4" />
              </Button>
              <AlertDialog>
                <AlertDialogTrigger as-child>
                  <Button variant="ghost" size="icon">
                    <Trash2 class="h-4 w-4" />
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                    <AlertDialogDescription>
                      This action cannot be undone. This will permanently delete your address and
                      remove your data from our servers.
                    </AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction @click="removeAddress(address.id)">Delete</AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            </div>
          </div>
          <p class="flex items-center mb-1">
            <Phone class="mr-2 h-4 w-4" />(+84) {{ address.phone.substring(1) }}
          </p>
          <p class="flex items-center">
            <MapPin class="mr-2 h-4 w-4" /> {{ address.province }}, {{ address.district }},
            {{ address.ward }}, {{ address.street }}
          </p>
        </div>
      </div>
      <div v-else>
        <p class="text-center text-gray-400 bg-gray-100 rounded-lg p-3">No address found</p>
      </div>
    </CardContent>
  </Card>
</template>
  
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { Plus, Edit2, Trash2, MapPin, Phone, Star } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormDescription,
  FormMessage
} from '@/components/ui/form'
import * as z from 'zod'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import AddressApi from '@/api/AddressApi'
import { useAuthStore } from '@/stores/authStore'
import { toast } from 'vue-sonner'
import type { Address } from '@/apiTypes'

const addresses = ref<Address[]>([])

export interface Province {
  code: number
  name: string
  'quan-huyen': District[]
}

export interface District {
  code: number
  name: string
  'xa-phuong': Ward[]
}

export interface Ward {
  code: number
  name: string
}

const isAdding = ref(false)
const provinces = ref<Province[]>([])
const districts = ref<District[]>([])
const wards = ref<Ward[]>([])
const authStore = useAuthStore()

const formSchema = toTypedSchema(
  z.object({
    name: z.string().min(1, { message: 'Name is required' }),
    phone: z
      .string()
      .regex(/^\d{10,15}$/, { message: 'Phone must be a valid number with 10 to 15 digits' }),
    street: z.string(),
    province: z.string().min(1, { message: 'Province is required' }),
    district: z.string().min(1, { message: 'District is required' }),
    ward: z.string().min(1, { message: 'Ward is required' })
  })
)

const form = useForm({
  validationSchema: formSchema
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    const response = await AddressApi.addAddress(values)
    if (response.code === 200) {
      toast.success('Add address successful!')
    } else {
      toast.warning('You can only have 3 addresses')
    }
    isAdding.value = false
    fetchAddresses()
  } catch (error) {
    console.error(error)
  }
})

const fetchProvinces = async () => {
  try {
    const response = await fetch('/province-data/tree.json')
    const data = await response.json().then((data: any) => data)
    provinces.value = Object.values(data).map((province: any) => ({
      code: province.code,
      name: province.name,
      'quan-huyen': Object.values(province['quan-huyen']).map((district: any) => ({
        code: district.code,
        name: district.name,
        'xa-phuong': Object.values(district['xa-phuong']).map((ward: any) => ({
          code: ward.code,
          name: ward.name
        }))
      }))
    }))
  } catch (error) {
    console.error('Failed to fetch provinces:', error)
  }
}

const loadDistricts = () => {
  if (form.values.province) {
    const province = provinces.value.find((p) => p.name === form.values.province)
    districts.value = province?.['quan-huyen'] || []
    form.setFieldValue('district', '')
    form.setFieldValue('ward', '')
    wards.value = []
  } else {
    console.error('Provinces is not an array or selectedProvince is invalid')
  }
}

const loadWards = () => {
  if (form.values.district) {
    const district = districts.value.find((d) => d.name === form.values.district)
    wards.value = district?.['xa-phuong'] || []
    form.setFieldValue('ward', '')
  } else {
    wards.value = []
  }
}

const fetchAddresses = async () => {
  try {
    const response = await AddressApi.getAddresses(authStore.username)
    console.log(response)
    addresses.value = response.data
  } catch (error) {
    console.error('Failed to fetch provinces:', error)
  }
}

const removeAddress = async (id: string) => {
  try {
    const response = await AddressApi.deleteAddress(id)
    if (response.code === 200) {
      toast.success('Delete address successful!')
      fetchAddresses()
    }
  } catch (error) {
    console.error(error)
  }
}

const setDefaultAdress = async (id: string) => {
  try {
    const response = await AddressApi.setDefault(id)
    if (response.code === 200) {
      toast.success('Set default address successful!')
      fetchAddresses()
    }
  } catch (error) {
    console.error(error)
  }
}

watch(() => form.values.province, loadDistricts)
watch(() => form.values.district, loadWards)

onMounted(() => {
  fetchProvinces()
  fetchAddresses()
})
</script>
  
<style scoped>
</style>

