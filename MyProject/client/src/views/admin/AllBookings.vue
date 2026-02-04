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
    <div class="search-container">
      <input 
        type="text" 
        v-model="searchQuery" 
        placeholder="請輸入姓名、電話或 Email 進行搜尋..."
        class="search-input"
      />
      <button class="search-btn">🔍 搜尋</button>
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
        <tr v-if="filteredReservations.length === 0">
          <td colspan="9" style="padding: 2rem; color: #8B4513;">找不到相符的訂位資料</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const userrole = ref();

// 2. 訂位資料列表 (模擬從 API 獲取)
const reservationList = ref([]);
const searchQuery = ref('');


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
  router.push({
    path:'/edit',
    query:{id:id}
  })
  // router.push({ name: 'edit-booking', params: { id: id } });
};

const deleteReservation = (id) => {
  if (confirm('確定要刪除此筆訂位嗎？')) {
    console.log(`執行刪除 ID: ${id}`);
    // 這裡應呼叫刪除 API
  }
};

const handleLogout = async () => {
  try {
    await axios.get('/api/logout')
  } catch(error) {
      console.log(error);
  }
  // 清除 localStorage
  localStorage.removeItem('userCert')
  router.push('/login')
}

const filteredReservations = computed(() => {
  const query = searchQuery.value.toLowerCase().trim();
  if (!query) {
    return reservationList.value;
  }
  return reservationList.value.filter(item => {
    return (
      item.name?.toLowerCase().includes(query) ||
      item.phone?.includes(query) ||
      item.email?.toLowerCase().includes(query)
    );
  });
});

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
.search-container {
  margin-bottom: 1.5rem;
  display: flex;
  justify-content: center;
}
.search-btn{
  margin-left: 10px;
  padding: 0.7rem 1.5rem;
  background-color: #ddc6af; 
  color: #4A2C15;
  border: none;
  border-radius: 50px; 
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
.search-btn:hover {
  background-color: #8B4513;
  color: #FFF8E7;
  transform: translateY(-2px); /* 輕微浮起效果 */
}

.search-input {
  width: 100%;
  max-width: 500px;
  padding: 0.8rem 1.2rem;
  border: 2px solid #D4A574;
  border-radius: 50px;
  font-size: 1rem;
  background-color: #fff;
  color: #4A2C15;
  outline: none;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  border-color: #8B4513;
  box-shadow: 0 0 8px rgba(139, 69, 19, 0.2);
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