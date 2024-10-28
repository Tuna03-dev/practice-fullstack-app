<template>
  <form @submit="onSubmit">
    <FormField v-slot="{ componentField }" name="username">
      <FormItem class="flex flex-col mb-5">
        <FormLabel class="mb-2 text-lg text-black">Username</FormLabel>
        <FormControl
          class="p-3 border-2 border-black rounded-lg focus:rounded-lg outline-none hover:bg-gray-100"
        >
          <Input type="text" placeholder="Customer123" v-bind="componentField" />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="password">
      <FormItem class="relative flex flex-col mb-5">
        <FormLabel class="mb-2 text-lg text-black">Password</FormLabel>
        <FormControl
          class="p-3 border-2 border-black rounded-lg focus:rounded-lg outline-none hover:bg-gray-100"
        >
          <Input
            :type="showpassword ? 'text' : 'password'"
            placeholder="@Customer123"
            v-bind="componentField"
          />
          <iconify-icon
            @click="togglePassShow"
            class="absolute right-4 top-16 translate-y-[-50%] text-lg"
            :icon="showpassword ? 'fa6-solid:eye' : 'fa6-solid:eye-slash'"
          />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <div class="py-4">
      <Button
        :class="[
          'bg-red-500 w-full h-full text-white text-lg rounded-lg font-medium hover:bg-red-400 outline-none',
          { 'cursor-not-allowed bg-red-400': loading }
        ]"
      >
        <iconify-icon :class="loading ? 'mx-1' : 'hidden'" icon="svg-spinners:90-ring-with-bg" />
        <span>Sign in</span>
      </Button>
    </div>
  </form>
</template>
  
<script setup lang="ts">
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '../components/ui/form'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { Button } from './ui/button'
import * as z from 'zod'
import { ref } from 'vue'
import { toast } from 'vue-sonner'
import AuthApi from '../api/AuthApi'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useQueryClient } from '@tanstack/vue-query'

const queryClient = useQueryClient()
const loading = ref<boolean>(false)
const showpassword = ref<boolean>(false)
const route = useRouter()

const formSchema = z.object({
  username: z.string(),
  password: z
    .string()
    // .min(6, { message: 'Password must be at least 6 characters' })
    // .regex(/[a-z]/, { message: 'Password must contain at least one lowercase letter' })
    // .regex(/[A-Z]/, { message: 'Password must contain at least one uppercase letter' })
    // .regex(/[0-9]/, { message: 'Password must contain at least one number' })
})

const form = useForm({
  validationSchema: toTypedSchema(formSchema),
  initialValues: {
    username: 'Customer123',
    password: '@Customer123'
  }
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    loading.value = true
    const dataResponse = await AuthApi.Login(values)
    console.log(dataResponse)
    if (dataResponse.code === 200) {
      if (dataResponse.data.accessToken) {
        const authStore = useAuthStore()
        authStore.setAccessToken(dataResponse.data.accessToken)
        authStore.setUserRole(dataResponse.data.role)
        authStore.setUsername(form.values.username || '')
        toast.success('Login successful!')
        queryClient.invalidateQueries({queryKey: ['cartitems']})
        route.push('/')
      }
    }
  } catch (error: any) {
    console.log(error)
    toast.error('Invalid Username or Password')
  }
  loading.value = false
})

const togglePassShow = () => {
  showpassword.value = !showpassword.value
}
</script>
  