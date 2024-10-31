<template>
    <div class="container mx-auto p-4">
        <Tabs default-value="all" class="w-full">
            <TabsList class="grid w-full grid-cols-5">
                <TabsTrigger value="all" class="px-4">All</TabsTrigger>
                <TabsTrigger value="pending" class="px-4">Pending</TabsTrigger>
                <TabsTrigger value="delivering" class="px-4">Delivering</TabsTrigger>
                <TabsTrigger value="completed" class="px-4">Completed</TabsTrigger>
                <TabsTrigger value="cancelled" class="px-4">Cancelled</TabsTrigger>
            </TabsList>

            <TabsContent value="all" class="mt-6 space-y-6">

                <div class="rounded-lg border">
                    <div class="border-b p-4">
                        <div class="flex items-center justify-between">
                            <div class="flex items-center space-x-2">
                                <span class="bg-red-100 text-red-600 text-xs px-2 py-1 rounded">FAVOURITE</span>
                                <span class="font-medium  text-lg">KenMobile Thai Binh</span>

                                <Button variant="ghost" size="sm" class="h-8">
                                    <Store class="h-4 w-4 mr-2" />
                                    View Shop
                                </Button>
                            </div>
                            <span class="text-orange-500">Delivering</span>
                        </div>
                    </div>

                    <div class="p-4">
                        <div class="flex gap-4">
                            <Image src="/placeholder.svg" alt="Product" width="80" height="80" class="rounded-md" />
                            <div class="flex-1">
                                <h3 class="font-medium">Nắp Lưng Thay Thế Cho Xiaomi Redmi K30 / K30 5G / K30i Loại Zin
                                </h3>
                                <p class="text-sm">x1</p>
                            </div>
                            <div class="text-right">
                                <p class="text-sm">₫70.000</p>
                            </div>
                        </div>
                    </div>

                    <div class="border-t p-4">
                        <div class="flex items-center justify-between">
                            <p class="text-sm text-muted-foreground">Đơn hàng sẽ được chuẩn bị và chuyển đi trước
                                12-04-2023</p>
                            <div class="flex items-center gap-4">
                                <div class="flex items-center">
                                    <span class="text-sm mr-2">Total amount:</span>
                                    <span class="text-lg font-medium text-orange-500">₫86.500</span>
                                </div>
                                <div class="space-x-2">
                                    <Button variant="destructive">Cancel Order</Button>
                                   
                                </div>
                            </div>
                        </div>
                    </div>
                </div>



            </TabsContent>
        </Tabs>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { MessageCircle, Store } from 'lucide-vue-next'
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Button } from "@/components/ui/button"


</script>

<style scoped>
/* Add any additional styles here */
</style>