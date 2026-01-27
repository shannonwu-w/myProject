<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

axios.defaults.withCredentials = true;

const router = useRouter();
const isLoggedIn = ref(true); // 預設先給 true 方便測試畫面

const form = reactive({
  name: '',
  people: '',
  email: '',
  phone: '',
  date: '',
  timeSlot: '', // 保持扁平結構
  message: ''
});

const today = new Date();
const nextMonth = new Date();
nextMonth.setMonth(today.getMonth() + 1);
const minDate = today.toISOString().split('T')[0];
const maxDate = nextMonth.toISOString().split('T')[0];

// 補上遺失的 function
const onDateChange = () => {
  console.log("日期變更為:", form.date);
};

onMounted(async () => {
  try {
    const response = await axios.get('/api/status');
    if (response.data && response.data.role) {
      isLoggedIn.value = true;
    } else {
      isLoggedIn.value = false;
      router.push('/login');
    }
  } catch (error) {
    console.error("驗證失敗:", error);
    // 測試階段可以先註解下一行，避免看不到畫面
    // router.push('/login'); 
  }
});

const submitReservation = async () => {
  try {
    const response = await axios.post('/api/reservation/make', form);
    if (response.status === 200) {
      alert("✅ " + (response.data.message || "訂位成功！"));
      router.push('/userpage');
    }
  } catch (error) {
    const errorMsg = error.response?.data?.error || "訂位失敗";
    alert(`❗ ${errorMsg}`);
  }
};

const handleLogout = async () => {
  try {
    await axios.get('/api/logout');
    router.push('/login');
  } catch (error) {
    router.push('/login');
    console.log(error);
  }
};
</script>

<template>
  <div class="reservation-page">
    <nav class="navbar">
      <router-link to="/homepage">🏠 回到首頁</router-link>
      <router-link to="/userpage">👤 個人頁面</router-link>
      <router-link to="/reservation/history">📜 訂位資料</router-link>
      <router-link to="/uploadmenu">📋 查看菜單</router-link>
      <a href="#" @click.prevent="handleLogout">🚪 登出</a>
    </nav>

    <div class="container">
      <h1>🍽️ 餐廳線上訂位</h1>

      <form @submit.prevent="submitReservation">
        <div class="form-group">
          <label for="people">👥 用餐人數:</label>
          <select v-model="form.people" id="people" required>
            <option value="">請選擇</option>
            <option v-for="n in 6" :key="n" :value="n">{{ n }}位</option>
          </select>
        </div>

        <div class="form-group">
          <label for="date">📅 可預約日期:</label>
          <input type="date" v-model="form.date" id="date" :min="minDate" :max="maxDate" required @change="onDateChange" />
        </div>

        <div class="form-group">
          <label for="timeId">⏰ 可選擇時段:</label>
          <select v-model="form.timeSlot" id="timeId" required>
            <option value="">請選擇時段</option>
            <option value="11:00">11:00</option>
            <option value="12:00">12:00</option>
            <option value="13:00">13:00</option>
            <option value="14:00">14:00</option>
            <option value="15:00">15:00</option>
            <option value="18:00">18:00</option>
          </select>
        </div>

        <div class="form-group">
          <label for="name">📝 預約人姓名:</label>
          <input type="text" v-model="form.name" id="name" required />
        </div>
        <div class="form-group">
          <label for="phone">📞 預約人電話:</label>
          <input type="text" v-model="form.phone" id="phone" required />
        </div>
        <div class="form-group">
          <label for="email">📧 預約人郵件:</label>
          <input type="email" v-model="form.email" id="email" required />
        </div>
        <div class="form-group">
          <label for="message">📝 特殊需求:</label>
          <textarea v-model="form.message" id="message" rows="4"></textarea>
        </div>

        <button type="submit" class="submit-button" >✅ 送出訂位</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* 這裡直接貼上你原本的 CSS，並做微調 */
.reservation-page {
  background: linear-gradient(135deg, #D4A574, #8B4513);
  min-height: 100vh;
}
.navbar {
  background: linear-gradient(90deg, #6B3E07, #A97447);
  padding: 12px 0;
  text-align: center;
}
.navbar a {
  color: #fff8e7;
  text-decoration: none;
  padding: 14px 22px;
  font-weight: 600;
  cursor: pointer;
}
.container {
  background: #FFF9F0;
  max-width: 720px;
  width: 90%;
  margin: 30px auto;
  padding: 30px 40px;
  border-radius: 20px;
}
h1 {
  text-align: center;
  color: #6B3E07;
}
.form-group { margin-bottom: 22px; }
label { display: block; font-weight: 700; color: #8B4513; }
input, select, textarea {
  width: 100%;
  padding: 12px;
  border-radius: 12px;
  border: 2px solid #D4A574;
}
.submit-button {
  width: 100%;
  background: linear-gradient(90deg, #8B4513, #D4A574);
  color: white;
  padding: 14px;
  border: none;
  border-radius: 18px;
  font-size: 1.3rem;
  cursor: pointer;
}
.notice {
  background: #FDF6E3;
  border-left: 8px solid #8B4513;
  padding: 20px;
  margin-top: 30px;
}
.footer {
  background: #6B3E07;
  color: white;
  text-align: center;
  padding: 16px;
}
</style>