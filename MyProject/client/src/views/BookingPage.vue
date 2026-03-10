<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const isLoggedIn = ref(false)
const memberProfile = ref(null)
const autoFill = ref(false)

const form = reactive({
  name: '',
  people: '',
  email: '',
  phone: '',
  resvDate: '',
  timeSlot: '',
  message: ''
})

onMounted(async () => {
  try {
    const res = await request.get('/api/profile')
    isLoggedIn.value = true
    memberProfile.value = res
  } catch (err) {
    alert('請先登入'+ err)
    router.push('/login')
    
  }
})

watch(autoFill, (checked) => {
  if (checked && memberProfile.value) {
    form.name  = memberProfile.value.username || ''
    form.phone = memberProfile.value.phone || ''
    form.email = memberProfile.value.email || ''
  } else {
    form.name  = ''
    form.phone = ''
    form.email = ''
  }
})

const submitReservation = async () => {
  try {
    await request.post('/api/reservation/make', { ...form })
    router.push('/bookingsuccess')
  } catch (error) {
    const msg = error.response?.data?.error || '訂位失敗'
    alert(`❗ ${msg}`)
  }
}

const handleLogout = () => {
  localStorage.removeItem('userCert')
  router.push('/login')
}
</script>
<template>
  <div class="reservation-page">
    <nav class="navbar">
      <router-link to="/homepage">🏠 回到首頁</router-link>
      <router-link to="/userpage">👤 會員中心</router-link>
      <router-link to="/bookinghistory">📜 訂位資料</router-link>
      <a href="#" @click.prevent="handleLogout">🚪 登出</a>
    </nav>

    <div class="container">
      <h1>🍽️ 喵喵貓咖線上訂位</h1>

      <form v-if="isLoggedIn" @submit.prevent="submitReservation">

        <!-- 自動帶入會員資料 -->
        <div class="form-group autofill">
          <input
            type="checkbox"
            id="autoFill"
            v-model="autoFill"
          />
          <label for="autoFill">自動帶入會員資料</label>
        </div>

        <div class="form-group">
          <label>👥 用餐人數:</label>
          <select v-model="form.people" required>
            <option value="">請選擇</option>
            <option v-for="n in 5" :key="n+1" :value="n+1">{{ n+1 }} 位</option>
          </select>
          <div class="notes">備註：七位(含)以上的客人請打電話預約，謝謝🙏 <br> 聯絡專線：(02)2345-6789</div>
        </div>

        <div class="form-group">
          <label>📅 可預約日期:</label>
          <input
            type="date"
            v-model="form.resvDate"
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
            <option value="16:00">16:00</option>
            <option value="17:00">17:00</option>
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
          <label>📝 備註:</label>
          <textarea v-model="form.message" rows="4"></textarea>
        </div>

        <button class="submit-button">✅ 送出訂位</button>
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
  
    <div class="footer">
      <p>☎️ 連絡電話：(02)2345-6789</p>
      <p>✉️ 電子信箱：meowmeowcafe@gmail.com</p>
      <p>🏠 地址：台北市大安區咖啡街123號(捷運大安站2號出口站步行5分鐘)</p>
    </div>
  </div>
</template>

<style scoped>
/* 你的原本樣式保留 */
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

.checkbox-label {
  display: flex;
  align-items: center;
  width: 100%; 
  gap: 8px;
  margin: 0;      
  padding: 0;
  cursor: pointer;
} 

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

.notes{
  font-size: 13px;
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

.autofill {
  width: 500px;
  display: flex;
  align-items: center;
  justify-content: flex-start; 
  padding: 0;               
  margin-bottom: 20px;
  gap: 6px;          
  color: #6b3e07;
}

.autofill input[type="checkbox"] {
  margin: 0;       
  padding: 0;
  width: auto;
  height: auto;
}

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
