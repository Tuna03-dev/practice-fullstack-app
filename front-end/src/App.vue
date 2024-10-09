<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import {Toaster} from 'vue-sonner'
import { useAuthStore } from '@/stores/authStore'
import { onMounted } from 'vue';
const authStore = useAuthStore();

onMounted(() => {
  authStore.restoreSession();
  
})

</script>

<template>
  <div class="font-sans">

    <Toaster richColors  :expand="true" position="top-center"/>
    <RouterView />
  </div>
</template>

<style scoped>

</style>
x