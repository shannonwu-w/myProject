// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router' 
import { Quasar,Dialog, Notify } from 'quasar'

// 引入樣式
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/dist/quasar.css'


// 1. 建立 Vue 實例，命名為小寫 app
const app = createApp(App)

// 2. 告訴 Vue 啟用路由功能 (這行必須在 mount 之前！)
app.use(router)

app.use(Quasar, {
      plugins: { Dialog, Notify }
})

// 3. 最後才掛載到網頁標籤 #app 上
app.mount('#app')
