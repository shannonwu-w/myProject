<template>
  <div class="page-wrapper">
     <div class="reset-container">
      <h2>🔁 重設密碼</h2>

      <div v-if="errorMsg" class="error-msg">❌ {{ errorMsg }}</div>
      <div v-if="successMsg" class="success-msg">✅ {{ successMsg }}</div>

      <form @submit.prevent="submitForm">
        <label for="newPassword">🔐 新密碼：</label>
        <input type="password" id="newPassword" v-model="newPassword" required />

        <label for="confirmPassword">✅ 確認新密碼：</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" required />

        <button class="btn" type="submit">💾 確認更新</button>
      </form>
      
      <a href="userpage">回會員中心</a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';
import request from '@/utils/request'


const newPassword = ref('');
const confirmPassword = ref('');
const errorMsg = ref('');
const successMsg = ref('');

// 只用 localStorage 判斷是否登入
onMounted(() => {
  const storedToken = localStorage.getItem('userCert');
  if (!storedToken) {
    alert('請先登入');
    router.push('/login');
  }
});

const submitForm = async () => {
  if (newPassword.value.length < 6) {
    errorMsg.value = '🔐 新密碼長度需至少 6 字元！';
    successMsg.value = '';
    return;
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMsg.value = '⚠️ 兩次輸入的新密碼不一致！';
    successMsg.value = '';
    return;
  }

  try {
    // 前端不用傳 userId，後端 session 會拿
    const res = await request.post(
      '/api/reset-password',
      { password: newPassword.value },
      { withCredentials: true } // 帶 cookie 給後端 session
    );

    successMsg.value = res.message || '密碼更新成功！';
    errorMsg.value = '';
    newPassword.value = '';
    confirmPassword.value = '';
  } catch (err) {
    errorMsg.value = err.response?.data?.message || '更新密碼失敗，請稍後再試。';
    successMsg.value = '';
  }
};
</script>

<style scoped>
.page-wrapper {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  max-width: 420px;
}

.reset-container {
  background-color: #fffaf4;
  padding: 40px 35px;
  border-radius: 18px;
  box-shadow: 0 12px 30px rgba(139, 69, 19, 0.2);
  animation: fadeIn 0.7s ease-in-out;
}

a {
  color: #000;
}

h2 {
  text-align: center;
  color: #8B4513;
  margin-bottom: 25px;
  font-size: 26px;
}

label {
  font-weight: bold;
  display: block;
  margin-top: 15px;
  color: #5a3410;
}

input[type="password"] {
  width: 90%;
  padding: 12px;
  margin-top: 8px;
  border-radius: 8px;
  border: 1px solid #caa17e;
  font-size: 16px;
  background-color: #fff9f3;
  transition: border-color 0.3s;
}

input[type="password"]:focus {
  border-color: #8B4513;
  outline: none;
}

.btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(to right, #d4a574, #8b4513);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 17px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 20px;
  margin-bottom: 10px;
  transition: background 0.3s ease, transform 0.2s;
}

.btn:hover {
  background: linear-gradient(to right, #bb8d57, #6e3a11);
  transform: translateY(-2px);
}

.error-msg,
.success-msg {
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 15px;
  text-align: center;
  font-weight: bold;
}

.error-msg {
  background: #ffe5e0;
  color: #a10000;
  border: 1px solid #ffbbbb;
}

.success-msg {
  background: #e6ffed;
  color: #006b3c;
  border: 1px solid #a2f0c2;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
