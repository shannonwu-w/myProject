<template>
  <div class="main-layout">
    <div class="container">
      <h1>喵喵貓咖使用者管理系統 🐱☕</h1>


      <div v-if="currentUser.role === 'admin'" class="nav-bar">
        <button @click="goTo('adminpage')" class="button">👤 回管理員首頁</button>
        <button @click="goTo('allbooking')" class="button">🧾 訂位管理</button>
        <button @click="goTo('homepage')" class="button">🐱 喵喵貓咖訂位系統</button>
        <button @click="handleLogout" class="button">🚪 登出</button>
      </div>


      <p v-if="message" class="status-message">{{ message }}</p>


      <table>
        <thead>
          <tr>
            <th>使用者名稱</th>
            <th>帳號(電子信箱)</th>
            <th>電話</th>
            <th>身分</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.userId">
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{user.phone}}</td>
            <td>{{ user.role }}</td>
            <td>
              <button @click="editUser(user)" class="button">✏️ 修改</button>
              <button @click="deleteUser(user)" class="button delete">🗑️ 刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>


    <div v-if="selectedUser" class="container">
      <h2>🔧 修改使用者資料</h2>
      <div class="form-group">
        <label>帳號：</label>
        <input v-model="selectedUser.username" type="text" required />
      </div>
      <div class="form-group">
        <label>電子信箱：</label>
        <input v-model="selectedUser.email" type="email" required />
      </div>
      <div class="form-group">
        <label>身分：</label>
        <select v-model="selectedUser.role">
          <option value="USER">一般使用者</option>
          <option value="ADMIN">管理員</option>
        </select>
      </div>
      <div class="form-group">
        <label>新密碼（留空不修改）：</label>
        <input v-model="selectedUser.password" type="password" />
        <span class="hint">若未輸入，將保留原密碼</span>
      </div>
      <button @click="saveUpdate" class="button">💾 儲存修改</button>
      <button @click="selectedUser = null" class="button">取消</button>
    </div>


    <div class="container">
      <h2>➕ 新增使用者</h2>
      <div class="form-group">
        <div class="form-group">
        <label>使用者名稱：</label>
        <input v-model="newUser.username" type="text" required />
      </div>
      <div class="form-group">
        <label>帳號(email)：</label>
        <input v-model="newUser.email" type="email" required />
      </div>      
      <div class="form-group">
        <label>密碼：</label>
        <input v-model="newUser.password" type="password" required />
      </div>
      <div class="form-group">
      <label>電話：</label>
        <input v-model="newUser.phone" type="phone" required />
      </div>
      <div class="form-group">
        <label>身分：</label>
        <select v-model="newUser.role">
          <option value="user">一般使用者</option>
          <option value="admin">管理員</option>
        
        </select>
      </div>
      <button @click="addUser" class="button">➕ 建立帳號</button>
    </div>
</div>

    <div class="footer">
      <p>🐾 本系統僅限喵喵貓咖管理員使用！</p>
    </div>
  </div>

</template>


<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';


// --- 資料狀態 (State) ---
const currentUser = ref({ role: 'admin' }); // 應從 API 或 Store 取得
const userList = ref([]);
const message = ref('');
const selectedUser = ref(null);
const newUser = ref({
  username: '',
  email: '',
  password: '',
  role: 'user'
});


// --- 生命週期 ---
onMounted(() => {
  checkAuth();
  fetchUsers();
  window.scrollTo({ top: 0, behavior: 'smooth' });
});


// --- 方法 (Methods) ---
const checkAuth = () => {
  if (currentUser.value.role !== 'admin') {
    alert("⚠️ 您沒有管理員權限，將返回使用者首頁");
    // 這裡通常使用 router.push('/userpage')
  }
};


const fetchUsers = async () => {
  // 模擬 API 取得資料
  // const res = await axios.get('/api/admin/users');
  // userList.value = res.data;
  userList.value = [
    { userId: 1, username: '喵喵', email: 'cat@meow.com',phone: '0912345678', role: 'user' }
  ];
};


const editUser = (user) => {
  // 深拷貝一份資料出來修改，避免直接影響表格
  selectedUser.value = { ...user, password: '' };
};


const saveUpdate = async () => {
  console.log('更新使用者:', selectedUser.value);
  message.value = '更新成功！';
  selectedUser.value = null;
  fetchUsers();
};


const deleteUser = async (user) => {
  if (confirm(`確定要刪除使用者「${user.username}」嗎？`)) {
    console.log('刪除 ID:', user.userId);
    message.value = '使用者已刪除';
    fetchUsers();
  }
};


const addUser = async () => {
  console.log('新增使用者:', newUser.value);
  message.value = '帳號建立成功！';
  // 重置表單
  newUser.value = { username: '', email: '', password: '', role: 'user' };
  fetchUsers();
};


const handleLogout = () => {
  console.log('執行登出邏輯');
};


const goTo = (path) => {
 router.push(`/${path}`);
};
</script>


<style scoped>
.main-layout {
  font-family: 'Segoe UI', Tahoma, sans-serif;
  background: linear-gradient(135deg, #D4A574 0%, #8B4513 100%);
  color: #4A2C15;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}
.container {
  background: #FFF8E7;
  margin: 1rem auto;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(139,69,19,0.3);
  width: 90%;
  max-width: 800px;
}
h1, h2 {
  text-align: center;
  color: #8B4513;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1.5rem;
}
th, td {
  border: 1px solid #8B4513;
  padding: 10px;
  text-align: center;
}
th {
  background-color: #D4A574;
}
.button {
  display: inline-block;
  margin: 0.5rem 0.2rem;
  padding: 0.5rem 1rem;
  font-weight: bold;
  color: #FFF8E7;
  background-color: #8B4513;
  border: none;
  border-radius: 40px;
  cursor: pointer;
  transition: transform 0.3s;
}
.button:hover { transform: scale(1.05); }
.button.delete { background-color: #B22222; }
.form-group { margin-bottom: 1rem; }
input, select {
  padding: 0.5rem;
  border-radius: 8px;
  border: 1px solid #ccc;
  width: 100%;
}
.hint { font-size: 0.9rem; color: #555; }
.footer { color: #E8D3B2; padding: 2rem; }
</style>

