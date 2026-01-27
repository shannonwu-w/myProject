<template>
  <div class="reservation-page">
    <nav class="navbar">
      <router-link to="/homepage">🏠 回到首頁</router-link>
      <router-link to="/userpage">👤 個人頁面</router-link>
      <router-link to="/reservation/history">📜 訂位資料</router-link>
      <router-link to="/uploadmenu">📋 查看菜單</router-link>
      <a href="#" @click.prevent="handleLogout">🚪 登出</a>
    </nav>

    <div v-if="isLoggedIn" class="container">
      <h1>🍽️ 餐廳線上訂位</h1>

      <form @submit.prevent="submitReservation">
        <div class="form-group">
          <label for="people">👥 用餐人數:</label>
          <select v-model="form.people" id="people" required>
            <option value="">請選擇</option>
            <option v-for="n in 6" :key="n" :value="n">{{ n }}位</option>
          </select>
          <div class="notes">備註：七位(含)以上的客人請打電話預約，謝謝🙏。</div>
        </div>

        <div class="form-group">
          <label for="date">📅 可預約日期:</label>
          <input 
            type="date" 
            v-model="form.date" 
            id="date" 
            :min="minDate" 
            :max="maxDate" 
            required
            @change="onDateChange"
          />
        </div>

        <div class="form-group">
          <label for="timeId">⏰ 可選擇時段:</label>
          <select v-model="form.timeSlots.timeId" id="timeId" required>
            <option value="">請選擇時段</option>
            <option value="1">10:00 - 11:30</option>
            <option value="2">12:00 - 13:30</option>
            <option value="3">14:00 - 15:30</option>
            <option value="4">16:00 - 17:30</option>
            <option value="5">18:00 - 19:30</option>
          </select>
        </div>

        <div class="form-group">
          <label for="name">📝 預約人姓名:</label>
          <input type="text" v-model="form.name" id="name" placeholder="請填寫真實名稱" required />
        </div>

        <div class="form-group">
          <label for="phone">📞 預約人電話:</label>
          <input type="text" v-model="form.phone" id="phone" placeholder="請填寫聯絡電話" required />
        </div>

        <div class="form-group">
          <label for="email">📧 預約人郵件:</label>
          <input type="email" v-model="form.email" id="email" placeholder="請填寫電子郵件" required />
        </div>

        <div class="form-group">
          <label for="message">📝 特殊需求:</label>
          <textarea v-model="form.message" id="message" rows="4" placeholder="例如：慶生、過敏資訊、指定座位區等"></textarea>
        </div>

        <button type="submit" class="submit-button">✅ 送出訂位</button>
      </form>

      <div class="notice">
        <h2>🛎️ 用餐注意事項</h2>
        <ul>
          <li>⏰ 預約保留 10 分鐘，逾時將取消訂位。</li>
          <li>⏳ 為確保用餐品質，請準時抵達。</li>
          <li>📞 若需更改或取消訂位，請提前一天通知。</li>
          <li>🍾 自備酒水將酌收開瓶費。</li>
          <li>📝 如有其他特殊需求，請於備註欄位填寫或來電洽詢。</li>
        </ul>
      </div>
    </div>

    <footer class="footer">
      <p>🏠 餐廳地址：台北市大安區咖啡街123號</p>
      <p>☎️ 連絡電話：(02)2345-6789</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const isLoggedIn = ref(false);

// 表單資料結構
const form = reactive({
  people: '',
  date: '',
  timeSlots: {
    timeId: ''
  },
  name: '',
  phone: '',
  email: '',
  message: ''
});

// 日期限制設定
const today = new Date();
const nextMonth = new Date();
nextMonth.setMonth(today.getMonth() + 1);

const minDate = today.toISOString().split('T')[0];
const maxDate = nextMonth.toISOString().split('T')[0];

onMounted(async () => {
  try {
    const response = await axios.get('/api/status');
    if (response.data && response.data.role) {
      isLoggedIn.value = true;
    } else {
      throw new Error();
    }
  } catch (error) {
    alert("⚠️ 請先登入！"+error);
    router.push('/login');
  }
});

const onDateChange = () => {
  if (form.date) {
    alert(`📅 您選擇了日期：${form.date}，請接著選擇時段。`);
  }
};

const submitReservation = async () => {
  try {
    const response = await axios.post('/api/reservation/make', form);
    if (response.data.success || response.status === 200) {
      alert("✅ 訂位成功！");
      router.push('/reservation/history');
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || "訂位失敗，請稍後再試";
    alert(`❗ ${errorMsg}`);
  }
};

const handleLogout = async () => {
  await axios.get('/api/logout');
  router.push('/login');
};
</script>

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