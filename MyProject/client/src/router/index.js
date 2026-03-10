import { createRouter, createWebHistory } from 'vue-router'
import UserList from '../views/UserList.vue' // 引入剛剛建立的檔案

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
   
    {
      path: '/users',
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
        },
        {
        path: '/all-users',
        name: 'all-users',
        component: () => import('../views/admin/AllUsers.vue')
        },
        {
        path: '/all-bookings',
        name: 'all-bookings',
        component: () => import('../views/admin/AllBookings.vue')
        },
        {
        path: '/edit',
        name: 'editBooking',
        component: () => import('../views/admin/EditBooking.vue')
        },
         {
        path: '/test',
        name: 'testJwt',
        component: () => import('../views/TestJwt.vue')
        }, 
        {
        path: '/test_jwtpage',
        name: 'testjwtpage',
        component: () => import('../views/TestBookingPage.vue')
        }
        
        ]
        })


// --- 路由守衛邏輯 ---

router.beforeEach((to, from, next) => {

  // 1️⃣ 先拿資料
  const certStr = localStorage.getItem('userCert');
  const cert = certStr ? JSON.parse(certStr) : null;

  // 2️⃣ 再宣告你會用到的變數
  const isAuthenticated = !!cert;
  const userRole = cert?.role; // 🔥 一定要在使用前

  // 3️⃣ 再定義頁面類型
  const isPublicPage = to.path === '/homepage';
  const isAuthPage = ['/login', '/register'].includes(to.path);
  const isAdminPath =
    to.path.includes('adminpage') ||
    to.path.includes('all-users') ||
    to.path.includes('edit') ||
    to.path.includes('all-bookings');

  // ---- 開始邏輯 ----

  if (isAuthenticated && isAuthPage) {
    return next({ name: 'homepage' });
  }

  if (isPublicPage || isAuthPage) {
    return next();
  }

  if (!isAuthenticated) {
    alert('請先登入！');
    return next({ name: 'login' });
  }

  if (isAdminPath && userRole !== 'ADMIN') {
    alert('權限不足，僅限管理員進入');
    return next({ name: 'homepage' });
  }

  next();
});

export default router