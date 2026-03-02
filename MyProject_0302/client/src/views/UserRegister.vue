<template>
  <div class="register-page">
    <q-form @submit="handleRegister" class="register-container">
      <h2>📝 使用者註冊</h2>
      
      <transition name="fade">
        <div v-if="message" class="message">{{ message }}</div>
        <div v-else-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </transition>

      <div class="form-group">
        <label for="email">📧 電子信箱(帳號):</label>
        <q-input 
          id="email"
          v-model="registerForm.email" 
          type="email" 
          placeholder="請符合 email 格式"
          filled
          dense
          :rules="[
            val => !!val || '請輸入 Email',
            val => /.+@.+\..+/.test(val) || 'Email 格式不正確'
          ]"
        />
      </div>

      <div class="form-group">
        <label for="username">👤 使用者姓名:</label>
        <q-input 
          id="username"
          v-model="registerForm.username" 
          type="text" 
          placeholder="請輸入真實姓名"
          filled
          dense
          :rules="[val => !!val || '請輸入姓名']"
        />
      </div>

      <div class="form-group">
        <label for="password">🔒 密碼:</label>
        <q-input 
          id="password"
          v-model="registerForm.password" 
          type="password" 
          placeholder="至少六個字"
          filled
          dense
          :rules="[
            val => !!val || '請輸入密碼',
            val => val.length >= 6 || '密碼至少需要 6 個字'
          ]"
        />
      </div>

      <div class="form-group">
        <label for="phone">📞 電話:</label>
        <q-input 
          id="phone"
          v-model="registerForm.phone"
          type="tel" 
          filled 
          dense 
          mask="##########"
          placeholder="中間不要有「-」或「空格」"
          :rules="[
            val => !!val || '電話不能為空',
            val => val.length === 10 || '電話需為 10 碼'
          ]"
        />
      </div>

      <div class="form-group q-mt-md">
        <button type="submit" class="submit-btn">✅ 註冊</button>
      </div>

      <div class="footer-link">
        <router-link to="/login">已有帳號？點我登入</router-link>
      </div>
    </q-form>
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
  role: 'USER' // 隱藏欄位直接在資料中定義即可，不需 input 標籤
})

const message = ref('')
const errorMessage = ref('')

// 處理註冊邏輯
const handleRegister = async () => {
  // q-form 在 submit 時若驗證失敗是不會進到這裡的，所以很安全
  try {
    message.value = ''
    errorMessage.value = ''
    
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
/* 保持原有背景設計 */
.register-page {
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
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 420px;
}

h2 {
  text-align: center;
  color: #8B4513;
  margin-bottom: 20px;
  font-size: 1.6em;
}

.form-group {
  margin-bottom: 5px; /* 減少間距，因為 q-input 的錯誤訊息會佔用空間 */
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #5a4635;
  font-weight: bold;
}

/* 調整按鈕 */
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
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background-color: #b77d48;
  transform: translateY(-2px);
}

.message, .error-message {
  text-align: center;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 8px;
}

.message { color: #28a745; background: #e8f5e9; }
.error-message { color: #dc3545; background: #fdecea; }

.footer-link { text-align: center; margin-top: 15px; }
.footer-link a { color: #8B4513; text-decoration: none; }

/* 簡單的淡入淡出動畫 */
.fade-enter-active, .fade-leave-active { transition: opacity 0.5s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>