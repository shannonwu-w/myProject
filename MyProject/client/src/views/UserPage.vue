<template>
  <div class="user-page">
    <nav class="navbar">
      <div class="user-info">
        <a>👤 {{ user.username }} 您好</a>
      </div>
      <div class="nav-links">
        <router-link to="/homepage">🏠 回首頁</router-link>
        <a href="#" @click.prevent="handleLogout">🚪 登出</a>
      </div>
    </nav>

    <main class="container">
      <h1>🌟 歡迎來到會員中心 🌟</h1>
      <div class="button-group">
        <button @click="navigateTo('BookingPage')">🍽️ 來去訂位</button>
        <button @click="navigateTo('bookinghistory')">📖 歷史訂位查詢</button>
        <button @click="navigateTo('profile')">📝 查看會員資料</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted,ref } from 'vue'

import { useRouter } from 'vue-router';
import axios from 'axios'

const userDto = ref({ role: '' })
const user = ref ({
  username:''
})


onMounted(async () => {
  try {
    // 1. 向後端詢問目前的 Session 狀態
    const response = await axios.get('/api/status');

    // 2. 嚴格檢查：必須有資料且 role 不為空
    if (response.data && response.data.role) {
      userDto.value = response.data;
      console.log("驗證成功，歡迎進入");
      user.value ={
        username: response.data.username
}
    } else {
      // 如果後端回傳 null 或空物件，手動丟出錯誤進入 catch
      throw Error("No Session");
    }
  } catch (error) {
    // 3. 只要失敗（沒登入、過期、或是連線錯誤）就執行這裡
    console.error("狀態檢查：未登入", error);
    
    // 顯示警示視窗
    alert("⚠️ 請先登入系統！");
    
    // 強制跳轉回登入頁
    router.push('/login');
  }
});

const router = useRouter();


// 導向功能
const navigateTo = (path) => {
  router.push(`/${path}`);
};



// 登出功能
const handleLogout = async () => {
  try{
    const response = await axios.get('api/logout');
    localStorage.removeItem('userCert');
    alert("🐾 登出成功，期待下次見面！");
    router.push('/login');

    console(response);


  }catch(error){
    console.error("登出請求失敗:", error);
    localStorage.clear();
    router.push('/login');
  }
 
};
</script>

<style scoped>
/* 背景與整體字型 */
.user-page {
  font-family: 'Segoe UI', '微軟正黑體', sans-serif;
  min-height: 100vh;
  width: 100vw;
  margin: 0,auto;
  position: relative;
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
  max-width: 80%;
  margin: 20px auto;
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

@media (max-width:  600px) {
  .button-group {
    flex-direction: column;
    align-items: center;
  }
  .button-group button {
    width: 100%;
    font-size: 16px;
  }

}
</style>