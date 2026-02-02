<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// 定義表單資料（使用 email 登入）
const loginForm = ref({
  email: '',      // 改成 email
  password: '',
  authcode: ''
})

// 錯誤訊息與驗證碼圖片路徑
const errorMsg = ref('')
const captchaUrl = ref('/api/authcode') // 加上 /api 前綴

// 重新整理驗證碼
const refreshAuthcode = () => {
  captchaUrl.value = `/api/authcode?t=${new Date().getTime()}`
}

// 處理登入提交
const handleLogin = async () => {
    try {
        // 發送 JSON 格式的 LoginDto
        const response = await axios.post('/api/login', {
            email: loginForm.value.email,   // 使用 email
            password: loginForm.value.password,
            authcode: loginForm.value.authcode
        });

        // 登入成功，取得 UserCert
        const cert = response.data; 
        // console.log("歡迎！您的角色是：" + cert.role);
        localStorage.setItem('userCert', JSON.stringify(cert));
        
        // 根據角色跳轉頁面
        if (cert.role === 'admin') router.push('/adminpage');
        else router.push('/homepage');

    } catch (error) {
        // 失敗時顯示後端錯誤訊息
        errorMsg.value = error.response?.data || '登入失敗，請稍後再試';
        // 同步刷新驗證碼
        refreshAuthcode();
    }

    
}


// 重置表單
const handleReset = () => {
  loginForm.value = { email: '', password: '', authcode: '' }
  errorMsg.value = ''
  refreshAuthcode()
}
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <h2>📇 歡迎登入</h2>

      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

      <form @submit.prevent="handleLogin">
        <label for="email">📧 帳號 Email：</label>
        <input 
          type="email" 
          id="email" 
          v-model="loginForm.email" 
          placeholder="請輸入 Email" 
          required 
        />

        <label for="password">🔒 密碼：</label>
        <input 
          type="password" 
          id="password" 
          v-model="loginForm.password" 
          placeholder="請輸入密碼" 
          required 
        />

        <label for="authcode">📝 驗證碼：</label>
        <div class="captcha-group">
          <input 
            type="text" 
            id="authcode" 
            v-model="loginForm.authcode" 
            placeholder="請輸入驗證碼" 
            required 
          />
          <img 
            :src="captchaUrl" 
            alt="驗證碼" 
            title="點擊刷新驗證碼" 
            @click="refreshAuthcode" 
          />
        </div>

        <div class="button-group">
          <button type="button" @click="handleReset" class="reset-btn">重置</button>
          <button type="submit" class="submit-btn">登入</button>
        </div>
      </form>

      <div class="extra-links">     
        <router-link to="/register">🆕 尚未註冊？點我註冊</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  margin: 0;
  padding: 0;
  font-family: "Microsoft JhengHei", sans-serif;
  background: linear-gradient(135deg, #D4A574, #8B4513);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
}

.login-container {
  background: #FFF5EC;
  padding: 35px 40px;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 100%;
}

.login-container h2 {
  color: #8B4513;
  text-align: center;
  margin-bottom: 25px;
  font-size: 1.8em;
}

label {
  display: block;
  margin: 12px 0 6px;
  font-weight: bold;
  color: #5a4635;
  text-align: left;
}

input[type="text"],
input[type="password"],
input[type="email"] {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
  font-size: 16px;
}

.captcha-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-group input {
  flex: 1;
}

.captcha-group img {
  height: 40px;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  cursor: pointer;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

button {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 6px;
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.reset-btn {
  background: #D4A574;
  color: #fff;
}

.reset-btn:hover {
  background: #c08d54;
}

.submit-btn {
  background: #8B4513;
  color: #fff;
}

.submit-btn:hover {
  background: #5f3010;
}

.error-msg {
  background: #ffd9d9;
  color: #a94442;
  padding: 8px 12px;
  border-radius: 6px;
  margin-bottom: 15px;
  text-align: center;
  font-weight: bold;
}

.extra-links {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
}

.extra-links a {
  color: #8B4513;
  text-decoration: none;
  margin: 0 8px;
}

.extra-links a:hover {
  text-decoration: underline;
}
</style>
