<script lang="ts" setup>
import type { StepperIndicatorProps } from 'radix-vue'
import { cn } from '@/lib/utils'
import { StepperIndicator, useForwardProps } from 'radix-vue'

import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<StepperIndicatorProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardProps(delegatedProps)
</script>

<template>
  <StepperIndicator
    v-bind="forwarded"
    :class="cn(
      'inline-flex items-center justify-center rounded-full text-gray-500/50 w-8 h-8 dark:text-gray-400/50',
      // Disabled
      'group-data-[disabled]:text-gray-500 group-data-[disabled]:opacity-50 dark:group-data-[disabled]:text-gray-400',
      // Active
      'group-data-[state=active]:bg-gray-900 group-data-[state=active]:text-gray-50 dark:group-data-[state=active]:bg-gray-50 dark:group-data-[state=active]:text-gray-900',
      // Completed
      'group-data-[state=completed]:bg-gray-100 group-data-[state=completed]:text-gray-900 dark:group-data-[state=completed]:bg-gray-800 dark:group-data-[state=completed]:text-gray-50',
      props.class,
    )"
  >
    <slot />
  </StepperIndicator>
</template>
