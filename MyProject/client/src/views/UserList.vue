<template>
  <div class="user-container">
    <h2>使用者清單</h2>
    <table border="1" v-if="users.length > 0">
      <thead>
        <tr>
          <th>ID</th>
          <th>帳號</th>
          <th>Email</th>
          <th>角色</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.userId">
          <td>{{ user.userId }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else>載入中，或目前沒有資料...</p>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';


const users = ref([]);


// 當頁面掛載完成時，去抓後端資料
onMounted(async () => {
  try {
    // 加上 /api，這樣才會對接到你的 UsersController
    const response = await axios.get('/api/users'); 
    users.value = response.data; 
    console.log("資料抓取成功:", response.data);
  } catch (error) {
    console.error('抓取資料失敗:', error);
  }
});
</script>


<style scoped>
.user-container { padding: 20px; }
table { width: 100%; border-collapse: collapse; margin-top: 10px; }
th, td { padding: 10px; text-align: left; }
th { background-color: #f4f4f4; }
</style>
