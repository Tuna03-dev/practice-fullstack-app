<template >
  <div
    class="flex flex-col bg-gradient-to-r to-gray-100 from-gray-300 rounded-lg items-start w-full"
  >
  <button
      v-for="button in buttons"
      :key="button.name"
      :class="{ active: activeButton === button.name }"
      @click="setActive(button.name)"
    >
      <iconify-icon :icon="button.icon" />
      {{ button.label }}
    </button>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';
import Container from './Container.vue'
const buttons = [
  { name: 'personal', label: 'Personal information', icon: 'ic:baseline-account-box' },
  { name: 'address', label: 'Address', icon: 'ic:round-format-indent-decrease' },
  { name: 'security', label: 'Security', icon: 'ic:baseline-security' },
  { name: 'signout', label: 'Sign out', icon: 'ic:outline-log-out' }
];

const activeButton = ref('personal');

const setActive = (buttonName: string) => {
  activeButton.value = buttonName;
};

</script>
<style lang="scss" scoped>
button {
  box-sizing: border-box;
  display: flex;
  text-align: left;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 20px;
  &:hover {
    background: #f1f1f1;
  }

  iconify-icon {
    font-size: 25px;
  }
}
.active {
  background: linear-gradient(to right, #f0f0f0, #f9f9f9);
  font-weight: 600;
  padding-left: 15px;
  border-left: 5px solid #30a5ec;
}
</style>