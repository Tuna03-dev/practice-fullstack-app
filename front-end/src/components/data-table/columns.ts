import type { ColumnDef } from '@tanstack/vue-table'
import { h } from 'vue'
import Button from '../ui/button/Button.vue'
import { ArrowUpDown } from 'lucide-vue-next'
import { Checkbox } from '@/components/ui/checkbox'
import type { ProductDetailResponse } from '@/apiTypes'
import DataTableDropDown from '@/components/data-table/DataTableDropDown.vue'
import { useRouter } from 'vue-router'

export const columns: ColumnDef<ProductDetailResponse>[] = [
  {
    id: 'select',
    header: ({ table }) =>
      h(Checkbox, {
        checked: table.getIsAllPageRowsSelected(),
        'onUpdate:checked': (value: boolean) => table.toggleAllPageRowsSelected(!!value),
        ariaLabel: 'Select all'
      }),
    cell: ({ row }) =>
      h(Checkbox, {
        checked: row.getIsSelected(),
        'onUpdate:checked': (value: boolean) => row.toggleSelected(!!value),
        ariaLabel: 'Select row'
      }),
    enableSorting: false,
    enableHiding: false,
    size: 60, 
  },
  {
    accessorKey: 'name',
    header: ({ column }) =>
      h('div', { class: 'flex justify-start items-center' }, [
        h(
          Button,
          {
            variant: 'ghost',
            onClick: () => column.toggleSorting(column.getIsSorted() === 'asc')
          },
          () => ['Name', h(ArrowUpDown, { class: ' h-4 w-4' })]
        )
      ]),
    cell: ({ row }) => {
      const name = row.getValue('name') as string
      return h('div', { class: 'text-left font-medium truncate' }, name) 
    },
    minSize: 300 
  },
  {
    accessorKey: 'image',
    header: () => h('div', { class: 'text-left' }, 'Image'),
    cell: ({ row }) => {
      const image = row.getValue('image') as string
      const productId = row.original.id 

      const router = useRouter() 

      return h('img', {
        src: image,
        class: 'w-16 h-16 object-cover cursor-pointer',
        onClick: () => router.push(`/product/${productId}`) 
      })
    },
    size: 100,
  },
  {
    accessorKey: 'categoryName',
    header: () => h('div', { class: 'text-left' }, 'Category'),
    cell: ({ row }) => {
      const category = row.getValue('categoryName') as string
      return h('div', { class: 'text-left font-medium truncate' }, category)
    },
    minSize: 120, 
  },
  {
    accessorKey: 'price',
    header: ({ column }) =>
      h('div', { class: 'flex justify-center items-center' }, [
        h(
          Button,
          {
            variant: 'ghost',
            onClick: () => column.toggleSorting(column.getIsSorted() === 'asc')
          },
          () => ['Price', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
        )
      ]),
    cell: ({ row }) => {
      const price = Number.parseFloat(row.getValue('price'))
      const formatted = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'VND'
      }).format(price)

      return h('div', { class: 'text-center font-medium' }, formatted)
    },
    minSize: 100,
    
  },
  {
    accessorKey: 'priceWithDiscount',
    header: ({ column }) =>
      h('div', { class: 'flex justify-center items-center' }, [
        h(
          Button,
          {
            variant: 'ghost',
            onClick: () => column.toggleSorting(column.getIsSorted() === 'asc')
          },
          () => ['Price With Discount', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
        )
      ]),
    cell: ({ row }) => {
      const price = Number.parseFloat(row.getValue('priceWithDiscount'))
      const formatted = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'VND'
      }).format(price)

      return h('div', { class: 'text-center font-medium' }, formatted)
    },
    minSize: 120, 
  },
  {
    accessorKey: 'stockQuantity',
    header: ({ column }) =>
      h('div', { class: 'flex justify-center items-center' }, [
        h(
          Button,
          {
            variant: 'ghost',
            onClick: () => column.toggleSorting(column.getIsSorted() === 'asc')
          },
          () => ['Stock Quantity', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
        )
      ]),
    cell: ({ row }) => {
      const stock = Number.parseFloat(row.getValue('stockQuantity'))
      return h('div', { class: 'text-center font-medium' }, stock)
    },
    minSize: 120, 
  },
  {
    accessorKey: 'soldQuantity',
    header: ({ column }) =>
      h('div', { class: 'flex justify-center items-center' }, [
        h(
          Button,
          {
            variant: 'ghost',
            onClick: () => column.toggleSorting(column.getIsSorted() === 'asc')
          },
          () => ['Sold Quantity', h(ArrowUpDown, { class: 'ml-2 h-4 w-4' })]
        )
      ]),
    cell: ({ row }) => {
      const sold = Number.parseFloat(row.getValue('soldQuantity'))
      return h('div', { class: 'text-center font-medium' }, sold)
    },
    minSize: 120, 
  },
  {
    id: 'actions',
    enableHiding: false,
    header: () => 'Actions',
    cell: ({ row }) => {
      const product = row.original

      return h('div', { class: 'relative' }, h(DataTableDropDown, {
        product,
        onExpand: row.toggleExpanded,
      }))
    },
  },
]
