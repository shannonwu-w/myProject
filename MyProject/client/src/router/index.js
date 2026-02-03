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
        },
        {
        path: '/adminpage',
        name: 'adminpage',
        component: () => import('../views/admin/AdminPage.vue')
        }, 
        {
        path: '/bookingpage',
        name: 'bookingpage',
        component: () => import('../views/BookingPage.vue')
        },
        {
        path: '/profile',
        name: 'profile',
        component: () => import('../views/ProfilePage.vue')
        },
        {
        path: '/bookinghistory',
        name: 'bookinghistory',
        component: () => import('../views/BookingHistory.vue')
        },
         {
        path: '/bookingsuccess',
        name: 'bookingsuccess',
        component: () => import('../views/BookingSuccess.vue')
        },
        {
        path: '/changepassword',
        name: 'changepassword',
        component: () => import('../views/ChangePassword.vue')
        }



        
        ]
        })

export default router