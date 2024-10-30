<template >
  <div
    class="flex flex-col rounded-lg items-start w-full"
  >
  <button
      v-for="button in buttons"
      :key="button.name"
      :class="{ active: activeButton === button.name }"
      @click="handleClick(button)"
    >
      <iconify-icon :icon="button.icon" />
      {{ button.label }}
    </button>
  </div>
</template>
<script lang="ts" setup>
import { useAuthStore } from '@/stores/authStore';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { toast } from 'vue-sonner';
const buttons = [
  { name: 'personal', label: 'Personal information', icon: 'ic:baseline-account-box', path: '/profile/personal-information' },
  { name: 'address', label: 'Address', icon: 'ic:round-format-indent-decrease', path: '/profile/address' },
  { name: 'security', label: 'Security', icon: 'ic:baseline-security' },
  { name: 'signout', label: 'Sign out', icon: 'ic:outline-log-out', path: '/logout' },
];

const activeButton = ref('personal');
const router = useRouter();
const authStore = useAuthStore();
const setActive = (buttonName: string) => {
  activeButton.value = buttonName;
};

const handleClick = (buttonName: {name: string, path?: string}) => {
  setActive(buttonName.name);

  if (buttonName.path && buttonName.path !== '/logout') {
    router.push(buttonName.path);
  }else if (buttonName.path === '/logout') {
    authStore.logout();
    router.push('/');
    toast.success('Logout successful!');
  }
}

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
    background: #f6f6f6;
    color: black;
    border: none;
  }

  iconify-icon {
    font-size: 25px;
  }
}
.active {
  color: white;
  background: linear-gradient(to right, #f03f2b, #e98410);
  font-weight: 600;
  padding-left: 15px;
  border-left: 5px solid #f1f094;
}
</style>