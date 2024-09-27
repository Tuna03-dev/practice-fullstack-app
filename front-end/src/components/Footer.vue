<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="bg-gray-200 border-t">
    <container>
      <div class="flex justify-between items-center lg:flex-row flex-col">
        <div class="max-w-96 py-6">
          <h3 class="text-gray-600 text-lg font-medium">Ecommerce Website From Tuna03-dev</h3>
          <p class="text-gray-600">
            Here is a simple ecommerce website, which Tuna03-dev created using Java Spring Boot and
            Vue3.
          </p>
        </div>
        

        <div>
          <a
            href="https://github.com/Tuna03-dev/practice-fullstack-app.git"
            aria-label="Github repository for Tuna03-dev"
            class="flex items-center space-x-2 text-gray-600 hover:underline"
          >
            <span>Check out code from my Github!</span>
            <iconify-icon icon="logos:github"></iconify-icon>
          </a>
        </div>
      </div>
    </container>
  </div>
</template>
<script setup lang="ts">
import Container from './Container.vue'
</script>
<style lang="">
</style>