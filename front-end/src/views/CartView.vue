<template>
  <Container>
    <div v-if="cartReponses.length">
      <Card class="mb-6">
        <CardHeader class="ml-2">
          <CardTitle class="flex items-center">
            <Checkbox
              class="mr-2"
              @update:checked="toggleAllProducts"
              :checked="areAllProductsSelected"
            />
            <span>All products</span>
          </CardTitle>
        </CardHeader>
      </Card>

      <Card v-for="shop in cartReponses" :key="shop.shopId" class="mb-6">
        <CardHeader class="ml-2">
          <CardTitle class="flex items-center">
            <Checkbox
              class="mr-2"
              @update:checked="toggleShop(shop.shopId)"
              :checked="isShopSelected(shop.shopId)"
            />
            {{ shop.shopName }}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead class="w-12"></TableHead>
                <TableHead class="w-96 text-left">Product</TableHead>
                <TableHead class="w-32 text-center">Unit Price</TableHead>
                <TableHead class="w-24 text-center">Quantity</TableHead>
                <TableHead class="w-32 text-center">Total Price</TableHead>
                <TableHead class="w-24 text-center">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <TableRow v-for="product in shop.cartItemResponses" :key="product.productId">
                <TableCell>
                  <Checkbox
                    @update:checked="toggleProduct(product.productId)"
                    :checked="selectedProducts.includes(product.productId)"
                  />
                </TableCell>
                <TableCell class="w-96">
                  <div
                    class="flex items-center w-fit gap-1 hover:cursor-pointer"
                    @click="handleShowProductDetail(product.productId)"
                  >
                    <img class="w-16" :src="product.productImage" :alt="product.productName" />
                    <div>{{ product.productName }}</div>
                  </div>
                </TableCell>
                <TableCell class="text-center"
                  >₫{{ product.pricePerProduct.toLocaleString() }}</TableCell
                >
                <TableCell class="text-center">
                  <NumberField
                    id="quantity"
                    :default-value="18"
                    v-model="product.quantity"
                    :min="1"
                    class="w-24"
                    :disabled="disableFields.includes(product.productId)"
                    @update:model-value="(value) => handleChangeQuantity(product.productId, value)"
                  >
                    <NumberFieldContent>
                      <NumberFieldDecrement />
                      <NumberFieldInput />
                      <NumberFieldIncrement />
                    </NumberFieldContent>
                  </NumberField>
                </TableCell>
                <TableCell class="text-center"
                  >₫{{ product.cartItemAmount.toLocaleString() }}</TableCell
                >
                <TableCell class="text-center">
                  <Button
                    variant="outline"
                    size="sm"
                    @click="handleRemoveProduct(product.productId)"
                  >
                    Delete
                  </Button>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <div class="mt-4 flex justify-between items-center">
        <AlertDialog>
          <AlertDialogTrigger as-child>
            <Button variant="destructive">Delete</Button>
          </AlertDialogTrigger>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>Do you want to delete all selected products?</AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancel</AlertDialogCancel>
              <AlertDialogAction class="bg-red-400 hover:bg-red-500" @click="removeSelectedProducts">Delete</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>

        <div class="text-xl font-bold ml-auto mr-3">Total: ₫{{ totalPrice.toLocaleString() }}</div>
        <Button class="w-1/6" @click="checkout">Checkout</Button>
      </div>
    </div>

    <div v-else class="text-center flex flex-col items-center justify-center">
      <label class="text-3xl font-medium text-gray-500 m-2"> No products in cart </label>
      <img class="w-1/3" src="../../public/images/cart/image.webp" alt="" />
      <Button class="bg-orange-400 hover:bg-orange-500 mt-4 text-3xl p-5" @click="handleGoShopping">
        Go shopping now
      </Button>
    </div>
  </Container>
</template>
  
  <script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'
import { Button } from '@/components/ui/button'
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput
} from '@/components/ui/number-field'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import { Input } from '@/components/ui/input'
import { Checkbox } from '@/components/ui/checkbox'
import Container from '@/components/Container.vue'
import CartApi from '@/api/CartApi'
import type { CartResponse } from '@/apiTypes'
import router from '@/router'
import { List } from 'lucide-vue-next'
import { toast } from 'vue-sonner'
import { useQueryClient } from '@tanstack/vue-query'

const queryClient = useQueryClient()
const cartReponses = ref<CartResponse[]>([])
const selectedProducts = ref<string[]>([])
const debounceTimout = ref<ReturnType<typeof setTimeout>>()
const disableFields = ref<string[]>([])
const showDialogDelete = ref(false)
const areAllProductsSelected = computed(() => {
  return cartReponses.value
    .flatMap((c) => c.cartItemResponses)
    .every((p) => selectedProducts.value.includes(p.productId))
})
const totalPrice = computed(() => {
  return cartReponses.value
    .flatMap((c) => c.cartItemResponses)
    .filter((p) => selectedProducts.value.includes(p.productId))
    .reduce((total, p) => total + Number(p.pricePerProduct) * p.quantity, 0)
})

const handleGoShopping = () => {
  router.push('/products')
}

const handleChangeQuantity = (productId: string, quantity: number) => {
  debounceUpdateCart(productId, quantity)
  console.log(productId, quantity)
}

const debounceUpdateCart = (productId: string, quantity: number) => {
  if (debounceTimout.value) {
    clearTimeout(debounceTimout.value)
  }
  debounceTimout.value = setTimeout(() => {
    updateCartNumber(productId, quantity)
  }, 1000)
}

const updateCartNumber = async (productId: string, quantity: number) => {
  try {
    disableFields.value.push(productId)
    const response = await CartApi.updateCartQuantity(productId, quantity)

    if (response.message) {
      toast.error(response.message)
      cartReponses.value = cartReponses.value.map((shop) => {
        return {
          ...shop,
          cartItemResponses: shop.cartItemResponses.map((product) => {
            if (product.productId === productId) {
              const maxQuantity = response.data || product.quantity
              return {
                ...product,
                quantity: maxQuantity
              }
            }
            return product
          })
        }
      })

      console.log(response.data)
      return
    }
    cartReponses.value = cartReponses.value.map((shop) => {
      return {
        ...shop,
        cartItemResponses: shop.cartItemResponses.map((product) => {
          if (product.productId === response.data.productId) {
            return {
              ...product,
              quantity: response.data.quantity,
              cartItemAmount: response.data.cartItemAmount
            }
          }
          return product
        })
      }
    })
  } catch (err: any) {
    console.log(err)
  } finally {
    queryClient.invalidateQueries({ queryKey: ['cartitems'] })
    disableFields.value = disableFields.value.filter((p) => p !== productId)
  }
}

const handleShowProductDetail = (productId: string) => {
  router.push(`/products/details/${productId}`)
}

const toggleAllProducts = () => {
  if (areAllProductsSelected.value) {
    selectedProducts.value = []
  } else {
    const allProductIds = cartReponses.value
      .flatMap((c) => c.cartItemResponses)
      .map((p) => p.productId)
    selectedProducts.value = Array.from(new Set([...selectedProducts.value, ...allProductIds]))
  }
}

const toggleProduct = (productId: string) => {
  if (selectedProducts.value.includes(productId)) {
    selectedProducts.value = selectedProducts.value.filter((p) => p !== productId)
  } else {
    selectedProducts.value.push(productId)
  }
  console.log('Selected products:', selectedProducts.value)
}

const toggleShop = (shopId: string) => {
  const shopProducts = cartReponses.value
    .find((c) => c.shopId === shopId)
    ?.cartItemResponses.map((p) => p.productId)
  const allSelecteds = shopProducts?.every((p) => selectedProducts.value.includes(p))
  if (allSelecteds) {
    selectedProducts.value = selectedProducts.value.filter((p) => !shopProducts?.includes(p))
  } else {
    selectedProducts.value = Array.from(
      new Set([...selectedProducts.value, ...(shopProducts || [])])
    )
  }
}

const isShopSelected = (shopId: string) => {
  const shopProducts = cartReponses.value
    .find((c) => c.shopId === shopId)
    ?.cartItemResponses.map((p) => p.productId)
  return shopProducts?.every((p) => selectedProducts.value.includes(p))
}

const handleRemoveProduct = async (productId: string) => {
  try {
    const response = await CartApi.deleteCart(productId)
  } catch (err: any) {
    toast.error(err.message)
  } finally {
    fetchListCart()
    queryClient.invalidateQueries({ queryKey: ['cartitems'] })
  }
}

const removeSelectedProducts = async () => {
  if (selectedProducts.value.length > 0) {
    try {
      await CartApi.clearCart(selectedProducts.value)
      fetchListCart()
      queryClient.invalidateQueries({ queryKey: ['cartitems'] })
    } catch (err: any) {
      toast.error(err.message)
    }
  } else {
    toast.error('Please select products to delete')
  }
}

const fetchListCart = async () => {
  try {
    const response = await CartApi.getListCartGroupByShop()
    cartReponses.value = response.data
    console.log(response.data)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchListCart()
})
</script>
  
<style>
</style>
  