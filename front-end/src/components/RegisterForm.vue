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
          <Input type="password" placeholder="@Customer123" v-bind="componentField" />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="confirmPassword">
      <FormItem class="relative flex flex-col mb-5">
        <FormLabel class="mb-2 text-lg text-black">Confirm password</FormLabel>
        <FormControl
          class="p-3 border-2 border-black rounded-lg focus:rounded-lg outline-none hover:bg-gray-100"
        >
          <Input type="password" placeholder="@Customer123" v-bind="componentField" />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="role">
      <FormItem class="relative flex flex-col mb-5">
        <FormLabel class="mb-2 text-lg text-black">Select role</FormLabel>
        <FormControl>
            <RadioGroup
            class="flex flex-col space-y-1"
            v-bind="componentField"
          >
            <FormItem class="flex items-center space-y-0 gap-x-3">
              <FormControl>
                <RadioGroupItem value="CUSTOMER" />
              </FormControl>
              <FormLabel class="font-normal">
                Customer
              </FormLabel>
            </FormItem>
            <FormItem class="flex items-center space-y-0 gap-x-3">
              <FormControl>
                <RadioGroupItem value="SHOP" />
              </FormControl>
              <FormLabel class="font-normal">
                Shop
              </FormLabel>
            </FormItem>
          </RadioGroup>
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
        <span>Register</span>
      </Button>
    </div>
  </form>
</template>
  
  <script setup lang="ts">
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '../components/ui/form'
import { RadioGroup, RadioGroupItem } from '../components/ui/radio-group'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { Button } from './ui/button'
import * as z from 'zod'
import { ref } from 'vue'
import { toast } from 'vue-sonner'
import AuthApi from '../api/AuthApi'
import { useRouter } from 'vue-router'

const loading = ref<boolean>(false)
const route = useRouter()

const formSchema = z
  .object({
    username: z.string().min(6, { message: 'Username must be at least 6 characters' }).refine(
        async (username)  => {
            const data = await AuthApi.CheckUsername(username);
            console.log(data)
            return data.data === false;
        },
        {message: 'Username already exists', path: ['username']}
    ),
    password: z
      .string()
      .min(6, { message: 'Password must be at least 6 characters' })
      .regex(/[a-z]/, { message: 'Password must contain at least one lowercase letter' })
      .regex(/[A-Z]/, { message: 'Password must contain at least one uppercase letter' })
      .regex(/[0-9]/, { message: 'Password must contain at least one number' }),
    confirmPassword: z.string().min(1, { message: 'Confirm password must required' }),
    role: z.enum(['SHOP', 'CUSTOMER'], {required_error: 'Role is required'})
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Password does not match',
    path: ['confirmPassword']
  })

const form = useForm({
  validationSchema: toTypedSchema(formSchema),
  initialValues:{
    role: "CUSTOMER"
  },

})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    loading.value = true
    const dataResponse = await AuthApi.Register({username: values.username, password: values.password, role: values.role})
    console.log(dataResponse)
    if (dataResponse.code === 200) {
      toast.success('Register successful!')
      route.push('/')
    }
  } catch (error: any) {
    console.log(error)
    toast.error(error.message)
  }
  loading.value = false
})


</script>
  