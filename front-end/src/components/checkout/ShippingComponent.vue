<template>
    <div class="w-full ">
        <div class="flex mb-4 gap-2 text-orange-500">
            <Truck></Truck>
            <h2 class="text-lg font-semibold">
                Select shipping method
            </h2>
        </div>
        <RadioGroup v-model:model-value="selectedMethod" @update:model-value="emitSelectedMethod">
            <div v-for="method in shippingMethods" :key="method.id">
                <Label :for="method.id"
                    class="flex items-center space-x-2 border-2 p-4 rounded-md cursor-pointer  bg-cyan-50"
                    :class="{ 'border-teal-400': selectedMethod === method.id, 'border-gray-300': selectedMethod !== method.id }">
                    <RadioGroupItem class="bg-white w-[20px] h-[20px] rounded-full   hover:bg-green3 focus:shadow-[0_0_0_2px] focus:shadow-black outline-none cursor-default" :id="method.id" :value="method.id" />
                    <Label :for="method.id" class="flex items-center justify-between w-full">
                        <div class="flex items-center gap-4">
                            <img :src="method.logo" alt="Shipping method logo" class="w-16 h-16 object-cover">
                            <div>
                                <p class="font-medium text-lg ">{{ method.name }}</p>
                                <p class="font-medium text-gray-500">{{ method.provider }}</p>
                                <p class="text-sm text-gray-500">{{ formatEstimatesDay(method.estimatedDays) }}</p>
                            </div>
                        </div>
                        <span class="text-primary font-semibold">₫{{ method.cost.toLocaleString() }}</span>
                    </Label>
                </Label>
            </div>
        </RadioGroup>
    </div>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import moment from 'moment';
import type { ShippingMethod } from '@/apiTypes';
import ShippingMethodApi from '@/api/ShippingMethodApi';
import RadioGroup from '../ui/radio-group/RadioGroup.vue';
import RadioGroupItem from '../ui/radio-group/RadioGroupItem.vue';
import Label from '../ui/label/Label.vue';
import { Truck } from 'lucide-vue-next';

const shippingMethods = ref<ShippingMethod[]>([]);
const selectedMethod = ref('');

const emit = defineEmits(['method-selected'])
const emitSelectedMethod = () => {
    emit('method-selected', shippingMethods.value.find((method) => method.id === selectedMethod.value) || {} as ShippingMethod)
}

const fetchShippingMethod = async () => {
    try {
        const response = await ShippingMethodApi.getAllShippingMethods();
        if (response.code === 200) {
            shippingMethods.value = response.data

        }
    } catch (err) {
        console.log(err)
    }
}

const formatEstimatesDay = (estimatedays: number) => {
    const today = new Date();
    const startDate = new Date(today);
    const endDate = new Date(today);
    startDate.setDate(today.getDate() + estimatedays - 1);
    endDate.setDate(today.getDate() + estimatedays + 1);
    return `Delivery on ${moment(startDate).format('MMMM Do YYYY')} - ${moment(endDate).format('MMMM Do YYYY')}`
}

onMounted(() => {
    fetchShippingMethod()
})
</script>
<style scoped></style>
