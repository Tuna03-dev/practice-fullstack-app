<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import {Toaster} from 'vue-sonner'
</script>

<template>
  
  <Toaster richColors  :expand="true" position="top-center"/>
  <RouterView />
</template>

<style scoped>

</style>
x