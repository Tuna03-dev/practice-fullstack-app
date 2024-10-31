<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { MoreHorizontal } from 'lucide-vue-next'
import { useRouter } from 'vue-router';

defineProps<{
  product: {
    id: string
  }
}>()

const router = useRouter();

function copy(id: string) {
  navigator.clipboard.writeText(id)
}
const editProduct = (id: string) => {
    router.push(`/management/shops/products/edit/${id}`)
}
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Button variant="ghost" class="w-8 h-8 p-0">
        <span class="sr-only">Open menu</span>
        <MoreHorizontal class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <DropdownMenuLabel>Actions</DropdownMenuLabel>
      
      <!-- <DropdownMenuItem>View detais</DropdownMenuItem> -->
      <DropdownMenuItem @click="editProduct(product.id)">Edit</DropdownMenuItem>
      <DropdownMenuSeparator />
      <DropdownMenuItem @click="copy(product.id)" class="text-red-500 hover:text-red-400">
        Delete
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>