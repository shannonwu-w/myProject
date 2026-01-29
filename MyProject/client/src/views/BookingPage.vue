<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

axios.defaults.withCredentials = true

const router = useRouter()
const isLoggedIn = ref(true)

// =======================
// 是否自動帶入會員資料
// =======================
const autoFill = ref(false)

// =======================
// 訂位表單
// =======================
const form = reactive({
  name: '',
  people: '',
  email: '',
  phone: '',
  date: '',
  timeSlot: '',
  message: ''
})

// =======================
// 日期限制
// =======================
const today = new Date()
const nextMonth = new Date()
nextMonth.setMonth(today.getMonth() + 1)

const minDate = today.toISOString().split('T')[0]
const maxDate = nextMonth.toISOString().split('T')[0]

// =======================
// 取得會員資料
// =======================
const fetchUserProfile = async () => {
  try {
    const res = await axios.get('/api/user/profile')
    if (res.data) {
      form.name = res.data.name || ''
      form.phone = res.data.phone || ''
      form.email = res.data.email || ''
    }
  } catch (error) {
    console.error('取得會員資料失敗', error)
  }
}

// =======================
// 監聽是否勾選自動帶入
// =======================
watch(autoFill, async (checked) => {
  if (checked) {
    await fetchUserProfile()
  } else {
    form.name = ''
    form.phone = ''
    form.email = ''
  }
})

// =======================
// 頁面初始化（只做登入驗證）
// =======================
onMounted(async () => {
  try {
    const response = await axios.get('/api/status')
    if (response.data && response.data.role) {
      isLoggedIn.value = true
    } else {
      isLoggedIn.value = false
      router.push('/login')
    }
  } catch (error) {
    console.error('驗證失敗:', error)
  }
})

// =======================
// 送出訂位
// =======================
const submitReservation = async () => {
  try {
    await axios.post('/api/reservation/make', form)
    router.push('/bookingsuccess')
  } catch (error) {
    const msg = error.response?.data?.error || '訂位失敗'
    alert(`❗ ${msg}`)
  }
}

// =======================
// 登出
// =======================
const handleLogout = async () => {
  try {
    await axios.get('/api/logout')
    router.push('/login')
  } catch {
    router.push('/login')
  }
}
</script>

<template>
  <div class="reservation-page">
    <nav class="navbar">
      <router-link to="/homepage">🏠 回到首頁</router-link>
      <router-link to="/userpage">👤 個人頁面</router-link>
      <router-link to="/bookinghistory">📜 訂位資料</router-link>
      <a href="#" @click.prevent="handleLogout">🚪 登出</a>
    </nav>

    <div class="container">
      <h1>🍽️ 餐廳線上訂位</h1>

      <form v-if="isLoggedIn" @submit.prevent="submitReservation">

        <!-- 自動帶入會員資料 -->
        <!-- <div class="form-group autofill">
          <input
            type="checkbox"
            id="autoFill"
            v-model="autoFill"
          />
          <label for="autoFill">自動帶入會員資料</label>
        </div> -->

        <div class="form-group">
          <label>👥 用餐人數:</label>
          <select v-model="form.people" required>
            <option value="">請選擇</option>
            <option v-for="n in 6" :key="n" :value="n">{{ n }} 位</option>
          </select>
        </div>

        <div class="form-group">
          <label>📅 可預約日期:</label>
          <input
            type="date"
            v-model="form.date"
            :min="minDate"
            :max="maxDate"
            required
          />
        </div>

        <div class="form-group">
          <label>⏰ 可選擇時段:</label>
          <select v-model="form.timeSlot" required>
            <option value="">請選擇</option>
            <option value="11:00">11:00</option>
            <option value="12:00">12:00</option>
            <option value="13:00">13:00</option>
            <option value="14:00">14:00</option>
            <option value="15:00">15:00</option>
            <option value="18:00">18:00</option>
          </select>
        </div>

        <div class="form-group">
          <label>📝 預約人姓名:</label>
          <input type="text" v-model="form.name" required />
        </div>

        <div class="form-group">
          <label>📞 預約人電話:</label>
          <input type="text" v-model="form.phone" required />
        </div>

        <div class="form-group">
          <label>📧 預約人 Email:</label>
          <input type="email" v-model="form.email" required />
        </div>

        <div class="form-group">
          <label>📝 特殊需求:</label>
          <textarea v-model="form.message" rows="4"></textarea>
        </div>

        <button class="submit-button">✅ 送出訂位</button>
      </form>

      <div class="notice">
        <h2>🛎️ 用餐注意事項</h2>
        <ul>
          <li>⏰ 訂位保留 10 分鐘</li>
          <li>📞 若需更改請提前一天通知</li>
          <li>🍾 自備酒水將酌收開瓶費</li>
        </ul>
      </div>

     </div>
      <div class="footer">
        <p>🏠 台北市大安區咖啡街123號</p>
        <p>☎️ (02)2345-6789</p>
      </div>
  </div>
</template>

<style scoped>
.reservation-page {
  background: linear-gradient(135deg, #d4a574, #8b4513);
  min-height: 100vh;
}

.navbar {
  background: linear-gradient(90deg, #6b3e07, #a97447);
  padding: 12px 0;
  text-align: center;
}

.navbar a {
  color: #fff8e7;
  text-decoration: none;
  padding: 14px 22px;
  font-weight: 600;
}

/* 
.checkbox-label {
  display: flex;
  align-items: center;
  width: 100%; 
  gap: 8px;
  margin: 0;      
  padding: 0;
  cursor: pointer;
 
   }  */

.container {
  background: #fff9f0;
  max-width: 720px;
  margin: 30px auto;
  padding: 30px 40px;
  border-radius: 20px;
}

h1 {
  text-align: center;
  color: #6b3e07;
}

.form-group {
  margin-bottom: 16px;
  width: 100%;


}

label {
  display: block;
  font-weight: bold;
  color: #8b4513;
}

input,
select,
textarea {
  width: 100%;
  padding: 12px;
  border-radius: 12px;
  border: 2px solid #d4a574;
  box-sizing: border-box;
}

/* .autofill {
  width: 500px;
  display: flex;
  align-items: center;
  justify-content: flex-start; 
  padding: 0;               
  margin-bottom: 20px;
  gap: 6px;          
  color: #6b3e07;
} */

/* .autofill input[type="checkbox"] {
  margin: 0;       
  padding: 0;
  width: auto;
  height: auto;
} */

.submit-button {
  width: 100%;
  background: linear-gradient(90deg, #8b4513, #d4a574);
  color: white;
  padding: 14px;
  border: none;
  border-radius: 18px;
  font-size: 1.2rem;
  cursor: pointer;
}

.notice {
  background: #fdf6e3;
  border-left: 8px solid #8b4513;
  padding: 20px;
  margin-top: 30px;
}

.footer {
  background: #8b4513;
  color: white;
  text-align: center;
  padding: 16px;
  margin-top: 30px;
}
</style>
