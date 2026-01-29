<template>
  <div class="profile-page">
    <nav>
      <div class="nav-left">🐱 喵喵貓咖</div>
      <div class="nav-links">
        <a href="/homepage">🏠 回首頁</a>
        <a href="/userpage">👤 回會員中心</a>
      </div>
    </nav>

    <!-- 只在登入成功才顯示內容 -->
    <div class="profile-container" v-if="isLoggedIn">
      <h2>👤 個人資料</h2>
      
      <div class="profile-field">
        <label>使用者名稱：</label>
        <span>{{ user.username }}</span>
      </div>

      <div class="profile-field">
        <label>帳號 (電子信箱)：</label>
        <span>{{ user.email }}</span>
      </div>

      <div class="profile-field">
        <label>電話：</label>
        <span>{{ user.phone }}</span>
      </div>

      <div class="profile-field">
        <label></label>
        <button @click="goToChangePassword">🔒 更改密碼</button>
      </div>
      
      <div class="info-text">
        如帳號有問題，請發 mail 至 
        <a href="mailto:meowmeowcafe@gmail.com">meowmeowcafe@gmail.com</a>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 使用者資料
const user = ref({
  username: '讀取中...',
  email: '讀取中...',
  phone: '讀取中...'
});

// 是否已登入
const isLoggedIn = ref(false);

// 取得使用者完整資料（同時驗證登入）
const fetchUserProfile = async () => {
  try {
    const res = await axios.get('/api/profile', { withCredentials: true });
    // 登入成功
    isLoggedIn.value = true;
    user.value = {
      username: res.data.username || '未提供',
      email: res.data.email || '未提供',
      phone: res.data.phone || '未提供'
    };
  } catch (err) {
    // 401 或未登入 → 跳轉登入頁
    alert("請先登入!");
    window.location.href = '/login';
    console.log(err);
  }
};

// 處理按鈕點擊
const goToChangePassword = () => {
  window.location.href = '/changePassword';
};

// 頁面載入時取得使用者資料
onMounted(() => {
  fetchUserProfile();
});
</script>


<style scoped>
/* Scoped 確保樣式只影響此元件 */
.profile-page {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #f7f0e8, #e0c3a8);
  scroll-behavior: smooth;
}

nav {
  background-color: #8B4513;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

nav .nav-left {
  font-size: 20px;
  font-weight: bold;
}

nav .nav-links a {
  color: white;
  text-decoration: none;
  margin-left: 20px;
  transition: color 0.3s ease;
}

nav .nav-links a:hover {
  color: #D4A574;
}

.profile-container {
  background-color: white;
  padding: 30px;
  margin: 40px auto;
  max-width: 600px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  animation: fadeIn 1s ease;
}

h2 {
  text-align: center;
  color: #8B4513;
  margin-bottom: 30px;
}

.profile-field {
  display: flex;
  margin: 15px 0;
  align-items: center;
}

.profile-field label {
  width: 150px;
  font-weight: bold;
  color: #8B4513;
}

.profile-field span {
  flex-grow: 1;
  color: #444;
}

button {
  background-color: #D4A574;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  color: #fff;
  font-weight: bold;
  transition: background-color 0.3s ease, transform 0.2s;
}

button:hover {
  background-color: #b07c47;
  transform: scale(1.05);
}

.info-text {
  margin-top: 25px;
  color: #8B4513;
  font-size: 14px;
  text-align: center;
  font-style: italic;
}

.info-text a {
  color: #8B4513;
  text-decoration: underline;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 600px) {
  .profile-field {
    flex-direction: column;
    align-items: flex-start;
  }
  .profile-field label {
    width: 100%;
    margin-bottom: 5px;
  }
}
</style>
