<template>
  <div class="edit-container">
    <div class="container">
      <h1>編輯訂位資料</h1>
      

      <form @submit.prevent="updateReservation" id="reservationForm">
        <label for="name">預約人姓名</label>
        <input v-model="form.name" type="text" id="name" required />

        <label for="phone">預約人電話</label>
        <input v-model="form.phone" type="text" id="phone" required />

        <label for="email">預約人郵件</label>
        <input v-model="form.email" type="email" id="email" required />

        <label for="date">可預約日期</label>
        <input 
          v-model="form.resvDate" 
          type="date" 
          id="date" 
          required 
          :min="minDate" 
          :max="maxDate" 
        />

        <label for="timeSlot">可選擇時段</label>
        <select v-model="form.timeId" id="timeSlot" required>
         <option value="">請選擇時段</option>
          <option value="11:00">11:00</option>
          <option value="12:00">12:00</option>
          <option value="13:00">13:00</option>
          <option value="14:00">14:00</option>
          <option value="15:00">15:00</option>
          <option value="16:00">16:00</option>
          <option value="17:00">17:00</option>
          <option value="18:00">18:00</option>
        </select>

        <label for="people">用餐人數</label>
        <select v-model="form.people" id="people" required>
          <option value="">請選擇</option>
          <option v-for="n in [2, 3, 4, 5, 6]" :key="n" :value="n">{{ n }}位</option>
        </select>

        <label for="message">特殊需求備註</label>
        <textarea v-model="form.message" id="message" rows="4" placeholder="例如：慶生、過敏資訊、指定座位區等"></textarea>

        <div v-if="statusMsg" :class="['message', isSuccess ? 'success' : 'error']">
        {{ statusMsg }}
        </div>

        <button type="submit" class="btn-submit" :disabled="loading">
          {{ loading ? '更新中...' : '✅ 更新訂位' }}
        </button>
      </form>

      <div class="btn-group">
        <button type="button" class="btn-secondary" @click="router.back()">← 回上一頁</button>
        <router-link to="/adminpage" class="btn-secondary">🏠 回管理員首頁</router-link>
      </div>

    
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

// 1. 狀態管理
const loading = ref(false);
const statusMsg = ref('');
const isSuccess = ref(true);

// 2. 表單資料 (對應你的 API 格式)
const form = reactive({
  reservationId: '',
  name: '',
  people: '',
  email: '',
  phone: '',
  resvDate: '',
  timeSlot: '',
  message: ''
});

// 3. 計算日期限制 (今天 ~ 一個月後)
const today = new Date();
const nextMonth = new Date();
nextMonth.setMonth(today.getMonth() + 1);

const formatDate = (d) => d.toISOString().split('T')[0];
const minDate = formatDate(today);
const maxDate = formatDate(nextMonth);

// 4. 初始化：取得單筆訂位資料
onMounted(async () => {
  // 獲取路徑參數或查詢參數中的 ID
  const reservationId = route.query.id || route.params.id;
  if (!reservationId) {
    statusMsg.value = "無效的訂位編號";
    isSuccess.value = false;
    return;
  }

  try {
    const response = await axios.get(`/api/reservation/edit/${reservationId}`);
    const data = response.data[0];
    Object.assign(form, data);
    if (data.timeSlot) {
      // 假設後端回傳是 "11:00" 或 "[11:00]"
      // 如果有中括號，可能需要處理：data.timeSlot.replace(/[\[\]]/g, '')
      form.timeId = data.timeSlot; 
    }
  } catch (error) {
    console.error("載入失敗", error);
    statusMsg.value = "無法取得訂位資料";
    isSuccess.value = false;
  }
});

const updateReservation = async () => {
  // 基本驗證
  if (!form.resvDate || !form.timeId) {
    statusMsg.value = "請填寫完整日期與時段";
    isSuccess.value = false;
    return;
  }

  loading.value = true;
  statusMsg.value = '';

  try {
    // 準備要送出的資料 (確保 DTO 欄位名稱正確)
    // 這裡我們把 timeId 塞回 timeSlot 送出，以符合你後端的存儲習慣
    const payload = {
      ...form,
      timeSlot: form.timeId  // 確保後端收到的是 "11:00" 這種格式
    };

    const response = await axios.post('/api/reservation/make', payload, {
      withCredentials: true // 重要：帶上 Session Cookie
    });

    if (response.status === 200) {
      statusMsg.value = "✅ " + (response.data.message || "更新成功！");
      isSuccess.value = true;
      
      // 成功後 1.5 秒自動跳轉回列表頁
      setTimeout(() => {
        router.push('/all-bookings');
      }, 1500);
    }
  } catch (error) {
    // const msg = error.response?.data?.error || '訂位失敗'
    isSuccess.value = false;
      // alert(`❗ ${msg}`)
    if (error.response) {
      // 處理 401 (未登入) 或 400 (錯誤)
      statusMsg.value = "❌ " + (error.response.data.error || "更新失敗");
      if (error.response.status === 401) {
        alert("登入逾時，請重新登入");
        router.push('/login');
      }
    } else {
      statusMsg.value = "❌ 網路連線失敗，請檢查伺服器";
    }
  } finally {
    loading.value = false;
  }
};


</script>

<style scoped>
/* 繼承你提供的 CSS 樣式 */
.edit-container {
  background: linear-gradient(135deg, #D4A574, #8B4513);
  min-height: 100vh;
  padding: 2rem;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.container {
  background: #FFF9F0;
  max-width: 600px;
  width: 100%;
  padding: 2rem 3rem;
  border-radius: 15px;
  box-shadow: 0 12px 25px rgba(139,69,19,0.25);
}
h1 { text-align: center; margin-bottom: 1.5rem; color: #4B2E05; }
label { display: block; margin: 1rem 0 0.3rem; font-weight: bold; }
input, select, textarea {
  width: 100%;
  padding: 0.5rem 1rem;
  border-radius: 10px;
  border: 2px solid #D4A574;
  font-size: 1rem;
  box-sizing: border-box;
}
.btn-submit {
  margin-top: 2rem;
  background: linear-gradient(90deg, #8B4513, #D4A574);
  color: #FFF9F0;
  padding: 1rem;
  font-size: 1.2rem;
  font-weight: 700;
  border: none;
  border-radius: 18px;
  cursor: pointer;
  width: 100%;
}
.btn-submit:disabled { opacity: 0.7; cursor: not-allowed; }
.message { margin-top: 1rem; margin-bottom: 1rem; padding: 1rem; border-radius: 12px; text-align: center; }
.success { background-color: #d4edda; color: #155724; }
.error { background-color: #f8d7da; color: #721c24; }
.btn-group { margin-top: 1.5rem; display: flex; justify-content: space-between; gap: 10px; }
.btn-secondary {
  background: linear-gradient(90deg, #A0522D, #D2B48C);
  color: #FFF9F0;
  padding: 0.75rem 1rem;
  font-weight: 600;
  border: none;
  border-radius: 18px;
  cursor: pointer;
  text-decoration: none;
  flex: 1;
  text-align: center;
}
</style>