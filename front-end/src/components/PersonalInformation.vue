<template >
  <div class="p-10 bg-gradient-to-b from-blue-50 to-white rounded-xl">
    <div class="flex">
      <img class="w-40 h-auto" src="../../public/images/profile/personal-info.png" alt="" />
      <div>
        <p class="text-3xl font-bold">Personal Information</p>
        <p class="text-lg text-gray-400">Edit your personal information</p>
      </div>
    </div>
    <div class="mt-10">
      <form class="grid grid-cols-2 gap-x-10 gap-y-5" @submit="onSubmit">
        <FormField v-slot="{ componentField }" name="username">
          <FormItem class="">
            <FormLabel>Username</FormLabel>
            <FormControl>
              <Input readonly type="text" placeholder="shadcn" v-bind="componentField" />
            </FormControl>

            <FormMessage />
          </FormItem>
        </FormField>
        <div class="text-start relative top-1/2 text-gray-400">
          You can not change your username
        </div>
        <FormField v-slot="{ componentField }" name="firstname">
          <FormItem>
            <FormLabel>First Name</FormLabel>
            <FormControl>
              <Input type="text" placeholder="shadcn" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="lastname">
          <FormItem>
            <FormLabel>Last Name</FormLabel>
            <FormControl>
              <Input type="text" placeholder="shadcn" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>Email</FormLabel>
            <FormControl>
              <Input type="text" placeholder="shadcn" v-bind="componentField" />
            </FormControl>

            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="phone">
          <FormItem>
            <FormLabel>Phone Number</FormLabel>
            <FormControl>
              <Input type="text" placeholder="shadcn" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="birthDate">
          <FormItem>
            <FormLabel>Birth Date</FormLabel>
            <FormControl>
              <Input type="date" placeholder="shadcn" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="gender">
          <FormItem>
            <FormLabel>Gender</FormLabel>

            <Select v-bind="componentField">
              <FormControl>
                <SelectTrigger>
                  <SelectValue placeholder="Select a gender" />
                </SelectTrigger>
              </FormControl>
              <SelectContent>
                <SelectGroup>
                  <SelectItem value="Male"> Male </SelectItem>
                  <SelectItem value="Female"> Female </SelectItem>
                  <SelectItem value="Other"> Other </SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>

            <FormMessage />
          </FormItem>
        </FormField>
        <Button
          class="col-span-2 w-1/2 bg-orange-400 hover:bg-orange-500 relative left-1/2 top-1/2 transform translate-x-[-50%]"
          type="submit"
        >
          Update
        </Button>
      </form>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Button } from '@/components/ui/button'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormDescription,
  FormMessage
} from '@/components/ui/form'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { Input } from '@/components/ui/input'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { z } from 'zod'
import { Phone } from 'lucide-vue-next'
import type { UserProfileResponse } from '@/apiTypes'
import { toast } from 'vue-sonner'
import UserApi from '@/api/UserApi'

const props = defineProps<{
  user: UserProfileResponse
}>()

const emits = defineEmits(['updateProfile'])

const formSchema = toTypedSchema(
  z.object({
    username: z.string(),
    email: z
      .string()
      .email({ message: 'Email must be a valid email' })
      .nullable()
      .or(z.literal('')), 
    phone: z
      .string()
      .regex(/^\d{10,15}$/, { message: 'Phone must be a valid number with 10 to 15 digits' })
      .nullable()
      .or(z.literal('')),
    birthDate: z.string().optional().refine((val) => {
      const age = new Date().getFullYear() - new Date(val).getFullYear();
      return age >= 12;
    },{message: "You must be at least 12 years old"}),
    gender: z.enum(['Male', 'Female', 'Other']),
    firstname: z.string().optional(),
    lastname: z.string().optional(),
  })
);

const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    username: props.user ? props.user.username : '',
    email: props.user ? props.user.email : '',
    phone: props.user ? props.user.phone : '',
    birthDate: props.user ? props.user.birthDate.toString() : '',
    firstname: props.user ? props.user.firstname : '',
    lastname: props.user ? props.user.lastname : '',
    gender: props.user && ['Male', 'Female', 'Other'].includes(props.user.gender as 'Male' | 'Female' | 'Other') 
            ? props.user.gender as 'Male' | 'Female' | 'Other'
            : 'Other',
  }
})

const onSubmit = form.handleSubmit( (values) => {
  emits('updateProfile', {...values, birthDate: values.birthDate ? new Date(values.birthDate.toString()) : null})
})
</script>
<style lang="">
</style>