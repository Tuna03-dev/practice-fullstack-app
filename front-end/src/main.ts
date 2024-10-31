import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'iconify-icon'
import App from './App.vue'
import router from './router'
import { VueQueryPlugin } from '@tanstack/vue-query'
import { MotionPlugin } from '@vueuse/motion'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(VueQueryPlugin)
app.use(pinia)
app.use(MotionPlugin)

app.use(router)

app.mount('#app')
