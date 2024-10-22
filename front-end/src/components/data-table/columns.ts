import type { ColumnDef } from '@tanstack/vue-table'
import { h } from 'vue'
export interface Payment {
  id: string
  amount: number
  status: 'pending' | 'processing' | 'success' | 'failed'
  email: string
}

export const payments: Payment[] = [
  {
    id: '728ed52f',
    amount: 100,
    status: 'pending',
    email: 'm@example.com'
  },
  {
    id: '489e1d42',
    amount: 125,
    status: 'processing',
    email: 'example@gmail.com'
  }
]

export const columns: ColumnDef<Payment>[] = [
  {
    accessorKey: 'id',
    header: () => h('div', { class: 'text-left' }, 'ID'),
    cell: ({ row }) => {
      const amount = row.getValue('id') as string
      return h('div', { class: 'text-left font-medium' }, amount)
    }
  },
  {
    accessorKey: 'amount',
    header: () => h('div', { class: 'text-left' }, 'Amount'),
    cell: ({ row }) => {
      const amount = Number.parseFloat(row.getValue('amount'))
      const formatted = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
      }).format(amount)

      return h('div', { class: 'text-left font-medium' }, formatted)
    }
  },
  {
    accessorKey: 'status',
    header: () => h('div', { class: 'text-left' }, 'Status'),
    cell: ({ row }) => {
        const amount = row.getValue('status') as string
      

        return h('div', { class: 'text-left font-medium' }, amount)
    }
  },
  {
    accessorKey: 'email',
    header: () => h('div', { class: 'text-left' }, 'Email'),
    cell: ({ row }) => {
      const amount = row.getValue('email') as string
      

      return h('div', { class: 'text-left font-medium' }, amount)
    }
  }
]
