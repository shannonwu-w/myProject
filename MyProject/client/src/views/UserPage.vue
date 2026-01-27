<template>
  <div class="user-page">
    <nav class="navbar">
      <div class="user-info">
        <a href="#">👤 {{ username }}</a>
      </div>
      <div class="nav-links">
        <router-link to="/homepage">🏠 回首頁</router-link>
        <a href="#" @click.prevent="handleLogout">🚪 登出</a>
      </div>
    </nav>

    <main class="container">
      <h1>🌟 歡迎來到使用者頁面 🌟</h1>
      <div class="button-group">
        <button @click="navigateTo('reservation')">🍽️ 來去訂位</button>
        <button @click="navigateTo('reservation/history')">📖 歷史訂位查詢</button>
        <button @click="navigateTo('profile')">📝 查看會員資料</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

// 假設這是在 Vuex 或 Pinia 中的使用者資料
const userCert = ref({
  username: '測試使用者' // 預設值，實際應從 API 或 Store 獲取
});

const router = useRouter();

// 計算顯示的用戶名
const username = computed(() => {
  return userCert.value ? userCert.value.username : "尚未登入";
});

// 導向功能
const navigateTo = (path) => {
  router.push(`/${path}`);
};

// 登出功能
const handleLogout = () => {
  console.log('執行登出邏輯...');
  // 清除 Token 或 Session，然後跳轉
  router.push('/login');
};
</script>

<style scoped>
/* 背景與整體字型 */
.user-page {
  font-family: 'Segoe UI', '微軟正黑體', sans-serif;
  min-height: 100vh;
  margin: 0;
  background: linear-gradient(135deg, #fffaf4, #f5e8d3);
}

/* 導覽列 */
.navbar {
  background-color: #8B4513;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.navbar a {
  color: #fff;
  text-decoration: none;
  padding: 10px 18px;
  font-weight: bold;
  border-radius: 8px;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.navbar a:hover {
  background-color: #D4A574;
  color: #4b2b0a;
}

/* 內容容器 */
.container {
  max-width: 900px;
  margin: 50px auto;
  padding: 30px;
  background-color: #fff8ee;
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
  text-align: center;
  animation: fadeIn 0.8s ease-in-out;
}

h1 {
  color: #8B4513;
  font-size: 32px;
  margin-bottom: 30px;
}

/* 按鈕區域 */
.button-group {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.button-group button {
  background: linear-gradient(to right, #D4A574, #8B4513);
  border: none;
  color: white;
  padding: 15px 30px;
  font-size: 18px;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
  transition: transform 0.2s ease, background-color 0.3s ease;
}

.button-group button:hover {
  transform: translateY(-4px);
  background: linear-gradient(to right, #c69559, #5a3413);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 600px) {
  .button-group {
    flex-direction: column;
    align-items: center;
  }
  .button-group button {
    width: 80%;
    font-size: 16px;
  }
}
</style>