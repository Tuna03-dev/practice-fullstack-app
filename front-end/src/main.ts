import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'iconify-icon'
import App from './App.vue'
import router from './router'
import { VueQueryPlugin } from '@tanstack/vue-query'

const app = createApp(App)
app.use(VueQueryPlugin)

app.use(createPinia())
app.use(router)

app.mount('#app')
