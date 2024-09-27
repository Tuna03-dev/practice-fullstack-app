<template lang="">
    <div class="flex flex-col min-h-screen">
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
</script>
<style lang="scss">

</style>