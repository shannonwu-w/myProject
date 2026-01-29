<template>
  <div class="register-page">
    <form class="register-container" @submit.prevent="handleRegister">
      <h2>📝 使用者註冊</h2>
      
      <div v-if="message" class="message">{{ message }}</div>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

       <!-- Email -->
      <div class="form-group">
        <label for="email">📧 電子信箱(帳號):</label>
        <input 
          type="email" 
          id="email" 
          v-model="registerForm.email" 
          required
        >
      </div>

      <!-- 使用者名稱 -->
      <div class="form-group">
        <label for="username">👤 使用者姓名:</label>
        <input 
          type="text" 
          id="username" 
          v-model="registerForm.username" 
          required
        >
      </div>

      <!-- 密碼 -->
      <div class="form-group">
        <label for="password">🔒 密碼（至少6碼）:</label>
        <input 
          type="password" 
          id="password" 
          v-model="registerForm.password" 
          required 
          minlength="6"
        >
      </div>

     

      <!-- 電話 -->
      <div class="form-group">
        <label for="phone">📞 電話:</label>
        <input 
          type="tel" 
          id="phone" 
          v-model="registerForm.phone"
          placeholder="09xx-xxx-xxx"
        >
      </div>

      <!-- 角色選擇（隱藏或預設 user） -->
      <input type="hidden" v-model="registerForm.role">

      <!-- 註冊按鈕 -->
      <div class="form-group">
        <button type="submit" class="submit-btn">✅ 註冊</button>
      </div>

      <div class="footer-link">
        <router-link to="/login">已有帳號？點我登入</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// 註冊表單資料
const registerForm = ref({
  username: '',
  password: '',
  email: '',
  phone: '',
  role: 'USER'
})

const message = ref('')
const errorMessage = ref('')

// 處理註冊邏輯
const handleRegister = async () => {
  try {
    message.value = ''
    errorMessage.value = ''
    
    // 發送 POST 請求到後端 API
    const response = await axios.post('/api/register', registerForm.value)
    
    if (response.data.success) {
      message.value = '🎉 註冊成功！即將跳轉至登入頁面...'
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      errorMessage.value = response.data.message || '註冊失敗，請稍後再試'
    }
  } catch (error) {
    console.error('Register Error:', error)
    errorMessage.value = '系統連線錯誤，或是帳號已存在'
  }
}
</script>

<style scoped>
.register-page {
  margin: 0;
  padding: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(to bottom right, #f3e9dd, #d4a574);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
}

.register-container {
  background-color: #fff;
  padding: 40px 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 420px;
  animation: fadeIn 0.8s ease;
}

h2 {
  text-align: center;
  color: #8B4513;
  margin-bottom: 25px;
  font-size: 1.8em;
}

.form-group {
  margin-bottom: 20px;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #5a4635;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  box-sizing: border-box;
  transition: box-shadow 0.3s ease;
}

.form-group input:focus {
  outline: none;
  box-shadow: 0 0 8px #d4a574;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #d4a574;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.submit-btn:hover {
  background-color: #b77d48;
  transform: scale(1.05);
}

.message {
  text-align: center;
  margin-bottom: 15px;
  color: #28a745;
  background: #e8f5e9;
  padding: 10px;
  border-radius: 8px;
}

.error-message {
  text-align: center;
  margin-bottom: 15px;
  color: #dc3545;
  background: #fdecea;
  padding: 10px;
  border-radius: 8px;
}

.footer-link {
  text-align: center;
  margin-top: 15px;
}

.footer-link a {
  color: #8B4513;
  text-decoration: none;
  font-size: 14px;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
