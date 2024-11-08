<template lang="">

    <div class="flex flex-col min-h-screen">
        <Sheet>
            <SheetTrigger class="fixed top-14 md:top-3 right-0 bottom-0 z-50" as-child>
                <Button class="bg-red-400  hover:bg-red-500">
                    <ChevronsLeft class="h-4 w-4" />
                </Button>
            </SheetTrigger>
            <SheetContent>
                <SheetHeader>
                    <SheetTitle>Manual Using Website</SheetTitle>
                    <SheetDescription>
                        Basic information about using the website.
                    </SheetDescription>
                </SheetHeader>
                <div class="grid gap-4 py-4">
                    <p class="text-lg font-semibold">Example Accounts</p>
                    <ul class="list-disc pl-5">
                        <li>Customer: Username - <strong>Customer123</strong>, Password - <strong>@Customer123</strong></li>
                        <li>Shop: Username - <strong>Shop1234</strong>, Password - <strong>@Shop123</strong></li>
                        <li>Admin (default): Username - <strong>Admin123</strong>, Password - <strong>@Admin123</strong></li>
                    </ul>
                    <p class="mt-4 text-red-500">
                        Note: The shop currently has limited functionality, and certain Admin features are still under development.
                    </p>
                </div>
                <SheetFooter>
                    <SheetDescription>
                        Thank you for using our website.
                    </SheetDescription>
                </SheetFooter>
            </SheetContent>
        </Sheet>
        <header-component></header-component>
        <div class="container my-10 flex-1">
            <router-view></router-view>        
        </div>
        <Footer></Footer>
    </div>
</template>

<script lang="ts" setup>
import HeaderComponent from '../components/HeaderComponent.vue'
import Footer from '@/components/Footer.vue';
import { RouterView } from 'vue-router'
import { Button } from '@/components/ui/button'
import { ChevronsLeft } from 'lucide-vue-next';
import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetFooter,
    SheetHeader,
    SheetTitle,
    SheetTrigger,
} from '@/components/ui/sheet'
</script>

<style lang="scss"></style>
