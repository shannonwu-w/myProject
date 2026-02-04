<template>
  <div class="container">
    <h1>📋 顧客訂位總覽</h1>
    
    <div class="btn-group">
      <router-link to="/adminpage" class="btn">🏠 回管理員首頁</router-link>
      <router-link to="/all-users" class="btn">👤 使用者管理</router-link>
      <router-link to="/bookingpage" class="btn">🖊️ 訂位頁面</router-link>
      <router-link to="/homepage" class="btn">🐱 喵喵貓咖首頁</router-link>
      <button @click="handleLogout" class="btn">🚪 登出</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>編號</th>
          <th>日期</th>
          <th>時段</th>
          <th>姓名</th>
          <th>Email</th>
          <th>電話</th>
          <th>人數</th>
          <th>備註</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reservation in reservationList" :key="reservation.reservationId">
          <td>{{ reservation.reservationId }}</td>
          <td>{{ reservation.resvDate }}</td>
          <td>
            <span v-if="reservation.timeSlot">
              {{ reservation.timeSlot}}
            </span>
            <span v-else>無時段</span>
          </td>
          <td>{{ reservation.name }}</td>
          <td>{{ reservation.email }}</td>
          <td>{{ reservation.phone }}</td>
          <td>{{ reservation.people }}</td>
          <td>{{ reservation.message }}</td>
          <td>
            <div class="action-buttons">
              <button @click="editReservation(reservation.reservationId)" class="edit-btn">
                ✏️ 修改
              </button>
              <button @click="deleteReservation(reservation.reservationId)" class="delete-btn">
                🗑️ 刪除
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const userrole = ref();

// 2. 訂位資料列表 (模擬從 API 獲取)
const reservationList = ref([]);


// 3. 模擬獲取資料
const fetchReservations = async () => {
  try {
    const response = await axios.get('/api/reservation/all-reservations');
    if (response && response.data) {
      reservationList.value = response.data;
    }
  } catch (error) {
    console.error("API 請求失敗：", error.message);
    if (error.response) {
      console.error("狀態碼：", error.response.status);
    }
  }
};

onMounted(async () => {
  // 1. 權限檢查
  const storedToken = localStorage.getItem('userCert');
  if (!storedToken) {
    alert('請先登入');
    router.push('/login');
    return;
  }

  const userCert = JSON.parse(storedToken);
  userrole.value = userCert.role;

  if (userrole.value !== 'ADMIN') {
    alert('您沒有權限');
    router.push('/homepage');
    return;
  }
  await fetchReservations();
});

// 4. 操作邏輯
const editReservation = (id) => {
  // router.push(`/admin/edit-booking?id=${id}`);
  router.push({ name: 'edit-booking', params: { id: id } });
};

const deleteReservation = (id) => {
  if (confirm('確定要刪除此筆訂位嗎？')) {
    console.log(`執行刪除 ID: ${id}`);
    // 這裡應呼叫刪除 API
  }
};

const handleLogout = () => {
  console.log("執行登出");
  // 呼叫登出 API 並導向登入頁
};


</script>

<style scoped>
body {
   font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
          margin: 0;
          padding: 0;
          background: linear-gradient(135deg, #D4A574 0%, #8B4513 100%);
          color: #4A2C15;
          display: flex;
          flex-direction: column;
          align-items: center;
          min-height: 100vh;
}
.container {
  background: #FFF8E7;
  margin: 3rem auto;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 25px rgba(139, 69, 19, 0.3);
  width: 95%;
  max-width: 1000px;
}
h1 {
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 1rem;
  color: #8B4513;
  text-shadow: 1px 1px 4px #D4A574;
}
.btn-group {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}
.btn {
  padding: 0.6rem 1.2rem;
  background-color: #8B4513;
  color: #FFF8E7;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  text-decoration: none;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(139, 69, 19, 0.4);
  transition: background-color 0.3s ease;
  font-size: 1rem;
}
.btn:hover {
  background-color: #D4A574;
  color: #4A2C15;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #D4A574;
  padding: 12px;
  text-align: center;
}
th {
  background-color: #D4A574;
  color: #4A2C15;
}
tr:hover {
  background-color: #fdf5e6;
  transition: background-color 0.3s ease;
}
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 6px;
}
.edit-btn, .delete-btn {
  min-width: 70px;
  padding: 6px 0;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  text-align: center;
  font-size: 0.9rem;
}
.edit-btn {
  background-color: #8B4513;
  color: #FFF8E7;
}
.delete-btn {
  background-color: #a52a2a;
  color: #fff0e6;
}
</style>