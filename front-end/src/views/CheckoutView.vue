<template>
    <Container class="flex flex-col  items-center justify-center">
        <div class="w-8/12" ref="address">
            <div class="w-full">
                <Stepper v-model="stepIndex">
                    <StepperItem v-for="item in steps" :key="item.step" class="basis-1/4 " :step="item.step">
                        <StepperTrigger>
                            <StepperIndicator
                                class="group-data-[state=completed]:bg-orange-400 group-data-[state=completed]:text-white group-data-[state=active]:bg-orange-300">
                                <component :is="item.icon" class="w-4 h-4 " />
                            </StepperIndicator>
                            <div class="flex flex-col">
                                <StepperTitle
                                    class="group-data-[state=completed]:text-orange-500 group-data-[state=active]:text-orange-300">
                                    {{ item.title }}
                                </StepperTitle>
                                <StepperDescription>
                                    {{ item.description }}
                                </StepperDescription>
                            </div>
                        </StepperTrigger>
                        <StepperSeparator v-if="item.step !== steps[steps.length - 1].step" class="w-full h-px" />
                    </StepperItem>
                </Stepper>
            </div>
            <div class="w-full">
                <div class="border-2 border-gray-300 rounded-md p-4 mb-4 flex justify-between items-center cursor-pointer hover:border-orange-500"
                    @click="toggleAddressModal">
                    <div>
                        <div class="flex items-start gap-2 ">
                            <MapPin class="text-orange-500"></MapPin>
                            <p class="font-semibold mb-2">Delivery Address</p>
                        </div>
                        <div v-if="Object.keys(selectedAddress).length > 0">
                            <div class="flex items-center gap-2">
                                <UserRoundPen class="text-gray-600 w-4 h-4" />
                                <p class="text-gray-600">{{ selectedAddress.name }}</p>
                            </div>
                            <div class="flex items-center gap-2">
                                <Phone class="text-gray-600 w-4 h-4"></Phone>
                                <p class="text-gray-600">(+84) {{ selectedAddress.phone?.substring(1) }}</p>
                            </div>
                            <div class="flex items-center gap-2">
                                <BookUserIcon class="text-gray-600 w-4 h-4"></BookUserIcon>
                                <p class="text-gray-600">{{ selectedAddress.street }}, {{ selectedAddress.ward }}, {{
                                    selectedAddress.district }}, {{ selectedAddress.province }}</p>
                            </div>
                        </div>
                        <div v-else>
                            <p class="text-gray-600">Select Address</p>
                        </div>
                    </div>
                    <ChevronRight class="text-gray-400" />
                </div>
                <div v-if="showAddressModal"
                    class="fixed  inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div class="bg-white p-6 rounded-lg w-full max-w-3xl">
                        <h3 class="text-lg font-semibold mb-4">Select Address</h3>
                        <Select v-model="selectedAddressId">
                            <SelectTrigger class="w-full h-10 mb-4">
                                <SelectValue placeholder="Select a address" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectGroup>
                                    <SelectItem v-for="address in addresses" :key="address.id" :value="address.id">
                                        <p>{{ address.name }} - {{ address.phone }}</p>
                                        <p>{{ address.street }}, {{ address.ward }}, {{ address.district }}, {{
                                            address.province }}</p>
                                    </SelectItem>
                                </SelectGroup>
                            </SelectContent>
                        </Select>
                        <div class="mb-4">
                            <p class="font-semibold">Add New Address</p>
                            <form @submit="onSubmit"
                                class="grid grid-cols-2 gap-x-4 gap-y-2 mb-6 p-4 bg-secondary rounded-lg">
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
                                                    <SelectItem v-for="province in provinces" :key="province.name"
                                                        :value="province.name">{{ province.name }}</SelectItem>
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
                                                    <SelectItem v-for="district in districts" :key="district.name"
                                                        :value="district.name">{{ district.name }}</SelectItem>
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
                                                    <SelectItem v-for="ward in wards" :key="ward.name"
                                                        :value="ward.name">{{
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

                                <div class="flex justify-center items-center col-span-2 gap-4">
                                    <Button class="w-[40%] bg-red-400" @click="showAddressModal = false">Cancel</Button>

                                    <Button class="w-[40%] bg-emerald-300">Add Address</Button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="w-8/12" ref="product">

            <list-item>
            </list-item>
        </div>
        <div class="w-8/12 mt-4" ref="shipping">

            <shipping-component
                @method-selected="(method: ShippingMethod) => updateStepIndex(3, method)"></shipping-component>
        </div>
        <div class="w-8/12 mt-4" ref="payment">
            <Card>
                <CardHeader>
                    <CardTitle class="text-orange-500 flex items-center gap-2">
                        <CircleDollarSign></CircleDollarSign>
                        <p class="font-semibold text-lg">
                            Choose payment method</p>
                    </CardTitle>
                </CardHeader>
                <CardContent>

                    <Select v-model="selectedPaymentMethod" :default-value="selectedPaymentMethod">
                        <SelectTrigger>
                            <SelectValue placeholder="Choose payment method" />
                        </SelectTrigger>
                        <SelectContent>

                            <SelectItem v-for="method in paymentMethods" :key="method.id" :value="method.id">
                                <div className="flex items-center gap-2">
                                    <component :is="method.icon" className="h-5 w-5 text-primary mr-2" />
                                    <span>{{ method.name }}</span>
                                </div>
                            </SelectItem>

                        </SelectContent>
                    </Select>
                    <Separator class="my-6"></Separator>
                    <div class="flex justify-between items-center  gap-4 text-gray-500">
                        <p>Total product cost: </p>
                        <p>₫ {{ cartStore.total.toLocaleString() }}</p>
                    </div>
                    <div class="flex justify-between items-center  gap-4 text-gray-500">
                        <p>Total shipping fee: </p>
                        <p>₫ {{ shippingMethod && shippingMethod.cost ? shippingMethod.cost.toLocaleString() : 0 }}</p>
                    </div>
                    <div class="flex justify-between items-center  gap-4 text-xl ">
                        <p>Total payment: </p>
                        <p class="text-orange-500">₫ {{ totalPayment.toLocaleString() }}</p>
                    </div>
                </CardContent>
                <Separator class="my-6"></Separator>

                <CardFooter>
                    <div class="ml-auto">
                        <p class="text-2xl font-bold">Total Payment</p>
                        <div class="flex items-center gap-2">
                            <span class="text-orange-500 font-bold text-2xl mr-2">
                                ₫ {{ totalPayment.toLocaleString() }}
                            </span>
                            <Button @click="handleOrder" size="lg"
                                class="bg-orange-400 text-lg hover:bg-orange-500">Order
                            </Button>
                        </div>
                    </div>
                </CardFooter>
            </Card>
        </div>
    </Container>
</template>
<script lang="ts" setup>
import ShippingComponent from '@/components/checkout/ShippingComponent.vue'
import { Separator } from '@/components/ui/separator'
import { Button } from '@/components/ui/button'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'
import { Stepper, StepperDescription, StepperIndicator, StepperItem, StepperSeparator, StepperTitle, StepperTrigger } from '@/components/ui/stepper'
import { computed, onMounted, ref, watch } from 'vue'
import { BookUser, CircleDollarSign, Check, CreditCard, Truck, ChevronRight, MapPin, Phone, UserRoundPen, BookUserIcon, Wallet, Banknote } from 'lucide-vue-next'
import Container from '@/components/Container.vue'
import type { Address, OrderCreationRequest, ShippingMethod } from '@/apiTypes'
import type { District, Province, Ward } from './UserAddressView.vue'
import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import { useForm } from 'vee-validate'
import AddressApi from '@/api/AddressApi'
import { toast } from 'vue-sonner'
import { useAuthStore } from '@/stores/authStore'
import ListItem from '@/components/checkout/ListItem.vue'
import Card from '@/components/ui/card/Card.vue'
import CardContent from '@/components/ui/card/CardContent.vue'
import CardHeader from '@/components/ui/card/CardHeader.vue'
import { useCartStore } from '@/stores/cartStore'
import CardFooter from '@/components/ui/card/CardFooter.vue'
import { useToast } from '@/components/ui/toast'
import { useRouter } from 'vue-router'
import OrderApi from '@/api/OrderApi'

const { toast: showToast } = useToast();
const paymentMethods = [
    {
        id: 'credit-card',
        name: 'Credit/Debit Cards',
        icon: CreditCard,
    },
    {
        id: 'cod',
        name: 'Cash on delivery',
        icon: Banknote,
    },
]

const cartStore = useCartStore()
const selectedPaymentMethod = ref('');
const payment = ref<HTMLElement | null>(null)
const address = ref<HTMLElement | null>(null)
const shipping = ref<HTMLElement | null>(null)
const authStore = useAuthStore()
const stepIndex = ref(1)
const steps = [{
    step: 1,
    title: 'Address',
    description: 'Add your address here',
    icon: BookUser,
}, {
    step: 2,
    title: 'Shipping',
    description: 'Set your shipping method',
    icon: Truck,
}, {
    step: 3,
    title: 'Payment',
    description: 'Add any payment information',
    icon: CreditCard,
}, {
    step: 4,
    title: 'Checkout',
    description: 'Confirm your order',
    icon: Check,
}]
const router = useRouter()
const showAddressModal = ref(false)
const addresses = ref<Address[]>([])
const provinces = ref<Province[]>([])
const districts = ref<District[]>([])
const wards = ref<Ward[]>([])
const selectedAddress = ref<Address>({} as Address)
const selectedAddressId = ref('')
const shippingMethod = ref<ShippingMethod>({} as ShippingMethod)
const orderCreation = ref<OrderCreationRequest>({} as OrderCreationRequest)
const totalPayment = computed(() => {
    if (shippingMethod.value && shippingMethod.value.cost) {
        return cartStore.total + shippingMethod.value.cost
    } else {
        return cartStore.total
    }
})

function toggleAddressModal() {
    showAddressModal.value = !showAddressModal.value
}

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
            await fetchAddresses()
            toggleAddressModal()
            form.resetForm()
            selectedAddress.value = response.data
            toast.success('Add address successful!')
        } else {
            toast.warning('You can only have 3 addresses')
        }

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

const updateStepIndex = (index: number, method: ShippingMethod) => {
    stepIndex.value = index
    shippingMethod.value = method

}

const handleOrder = async () => {
    console.log(selectedAddress.value, selectedPaymentMethod.value, shippingMethod.value)
    if (selectedAddress.value && selectedPaymentMethod.value && shippingMethod.value) {
        if (selectedPaymentMethod.value === 'credit-card') {
            showToast({
                title: 'Error',
                description: 'We currently do not support this payment method',
                variant: 'destructive',
            })
            return;
        }
        const itemIds = cartStore.items.map((item) => item.cartItemResponses.map((cartItem) => cartItem.id)).flat();

        orderCreation.value = {
            addressId: selectedAddress.value.id,
            shippingMethodId: shippingMethod.value.id,
            timeDelivery: shippingMethod.value.estimatedDays,
            totalAmount: cartStore.total,
            totalAmountPaid: Number(totalPayment.value),
            cartItemIds: itemIds,
        }

        try {
            const response = await OrderApi.createOrder(orderCreation.value);
            if (response.code === 200) {
                cartStore.clearItems();
                router.push('/order-success')
            }
        } catch (error: any) {
            console.error(error.data);
            const errorData = error.data as Record<string, string>;

            const errorMessage = Object.entries(errorData)
                .map(([key, value]) => `${value}`)
                .join('\n');
            showToast({
                title: 'Error',
                description: errorMessage,
                variant: 'destructive',

            })
        }



    } else {
        showToast({
            title: 'Error',
            description: 'Please select address, payment method and shipping method',
            variant: 'destructive',

        })
    }
}

watch(stepIndex, (newIndex) => {
    const contentRefs: { [key: number]: HTMLElement | null } = {
        1: address.value,
        2: shipping.value,
        3: payment.value
    }

    const sectionToScroll = contentRefs[newIndex];

    if (sectionToScroll) {
        sectionToScroll.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }
});
watch(selectedAddress, () => {
    if (selectedAddress.value) {
        stepIndex.value = 2
    }
})

watch(selectedPaymentMethod, () => {
    if (selectedPaymentMethod.value) {
        stepIndex.value = 4
    }
})
watch(() => form.values.province, loadDistricts)
watch(() => form.values.district, loadWards)
watch(selectedAddressId, () => {
    selectedAddress.value = addresses.value.find((address) => address.id === selectedAddressId.value) || {} as Address
    toggleAddressModal();
})

onMounted(async () => {
    fetchProvinces()
    await fetchAddresses()
    if (addresses.value.length > 0) {
        addresses.value.forEach((address) => {
            if (address.defaultAddress) {
                selectedAddress.value = address
            }
        })
    }
})
</script>

<style lang="">

</style>