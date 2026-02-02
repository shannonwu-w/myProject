<template>
  <div class="page-wrapper">
    <div class="container">
      <h2>📅 {{ username }} 的歷史訂位紀錄</h2>
      <p>共 <strong>{{ reservations.length }}</strong> 筆訂位資料</p>
      <p class="notice-text">
        ※ 最晚請在前一天晚上 11:59 前取消訂位，當天取消請來電 📞
      </p>

      <div v-if="reservations.length === 0" class="alert-info">
        您尚無任何訂位紀錄。
      </div>

      <table v-else role="table">
        <thead>
          <tr>
            <th>訂位人</th>
            <th>日期</th>
            <th>時段</th>
            <th>備註</th>
            <th style="width:120px;">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in reservations" :key="r.reservationId">
            <td>{{ r.name }}</td>
            <td>{{ r.resvDate }}</td>
            <td>{{ r.timeSlot }}</td>
            <td>{{ r.message && r.message.trim() !== '' ? r.message : '無' }}</td>
            <td>
              <button 
                v-if="isTomorrowOrLater(r.resvDate)" 
                @click="deleteReservation(r.reservationId)" 
                class="btn-danger"
              >
                🗑 取消訂位
              </button>
              <span v-else class="text-muted">不可取消</span>
            </td>
          </tr>
        </tbody>
      </table>

      <div style="text-align: center;">
        <router-link to="/userpage" class="btn-secondary">🏠 回使用者頁</router-link>
      </div>
    </div>

    <footer>
      🏠 台北市大安區咖啡街123號 ｜📞 (02)2345-6789｜📧 meowmeowcafe@gmail.com
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import router from '@/router';


const username = ref('訪客');
const reservations = ref([]);
const userId = ref('');
onMounted(async () => {
  const storedToken = localStorage.getItem('userCert');

  if (!storedToken) {
    alert('請先登入');
    router.push('/login');
    return;
  }

  const userCert = JSON.parse(storedToken);
  username.value = userCert.username || '訪客';
  userId.value = userCert.userId;

  try {
    const res = await axios.get('/api/reservation/history', {
      params: { userId: userId.value }
    });
    reservations.value = res.data;
  } catch (e) {
    console.error(e);
    alert('取得訂位紀錄失敗');
  }
});
// 判斷日期是否為明天（含）以後
const isTomorrowOrLater = (dateStr) => {
  const reservationDate = new Date(dateStr);
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate());
  tomorrow.setHours(0, 0, 0, 0);
  
  return reservationDate >= tomorrow;
};

// 刪除訂位邏輯
const deleteReservation = async (id) => {
  if (confirm('❗️確定要取消這筆訂位紀錄嗎？')) {
    console.log(`正在刪除訂位 ID: ${id}`);
    // 實務上會呼叫 API: await axios.post(`/api/reservation/delete/${id}`)
    // 然後重新抓取資料或在前端過濾掉該筆
    reservations.value = reservations.value.filter(r => r.reservationId !== id);
    alert('已成功取消訂位');
  }
};
</script>

<style scoped>
:root {
  --main-dark: #8B4513;
  --main-light: #D4A574;
  --bg-color: #FFF9F3;
  --text-dark: #3E2F1C;
  --text-light: #6B4F3A;
}

.page-wrapper {
  background-color: #FFF9F3;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: "微軟正黑體", sans-serif;
}

.container {
  background: #fff;
  width: 95%;
  max-width: 960px;
  margin: 40px auto;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(139, 69, 19, 0.1);
}

h2 {
  text-align: center;
  color: #8B4513;
  font-size: 1.6rem;
  margin-bottom: 1rem;
}

p {
  text-align: center;
  margin-bottom: 1rem;
  color: #6B4F3A;
}

.notice-text {
  color: #8B4513;
  font-size: 0.95rem;
  font-weight: 500;
}

.alert-info {
  background-color: #fff3e5;
  border-left: 5px solid #D4A574;
  padding: 12px 16px;
  border-radius: 6px;
  color: #8B4513;
  text-align: center;
  font-weight: 500;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

thead {
  background-color: #8B4513;
  color: white;
}

thead th {
  padding: 12px;
  text-align: left;
}

tbody tr {
  background-color: #fffaf2;
  border-bottom: 1px solid #e3d1b6;
}

tbody tr:hover {
  background-color: #f9f0e4;
}

tbody td {
  padding: 12px;
  vertical-align: middle;
}

.btn-danger {
  background-color: #8B4513;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: 0.3s;
}

.btn-danger:hover {
  background-color: #6f3510;
}

.btn-secondary {
  display: inline-block;
  background-color: #D4A574;
  color: #3E2F1C;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: 600;
  margin-top: 30px;
  transition: 0.3s;
}

.btn-secondary:hover {
  background-color: #b9884d;
  color: white;
}

.text-muted {
  color: #aaa;
  font-style: italic;
  font-size: 0.9rem;
}

footer {
  background-color: #f5f0ea;
  color: #6b4f3a;
  font-size: 0.8rem;
  text-align: center;
  padding: 12px 0;
  border-top: 1px solid #e0d1c0;
  margin-top: auto;
}

@media (max-width: 768px) {
  .container { padding: 20px; }
  table, th, td { font-size: 0.9rem; }
}
</style>