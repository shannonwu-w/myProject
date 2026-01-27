import { createRouter, createWebHistory } from 'vue-router'
import UserList from '../views/UserList.vue' // 引入剛剛建立的檔案

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
   
    {
      path: '/users', // 瀏覽器輸入的路徑
      name: 'users',
      component: UserList
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/UserRegister.vue')
        },
     {
        path: '/login',
        name: 'login',
        component: () => import('../views/UserLogin.vue')
        },
      {
        path: '/homepage',
        name: 'homepage',
        component: () => import('../views/HomePage.vue')
        },
        {
        path: '/userpage',
        name: 'userpage',
        component: () => import('../views/UserPage.vue')
        }
        ]
        })

export default router