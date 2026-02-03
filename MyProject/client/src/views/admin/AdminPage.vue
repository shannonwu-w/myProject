<template>
  <div class="admin-dashboard">
    <div class="container">
      <h1>喵喵貓咖管理系統 🐱☕</h1>

      <div>
        <router-link to="/admin/usermanagement" class="button">👤 使用者管理</router-link>
        <router-link to="/admin/reservations" class="button">🧾 訂位管理</router-link>
        <router-link to="/homepage" class="button">🏠 喵喵貓咖訂位系統</router-link>
      </div>

      <button @click="handleLogout" class="button logout-btn">🚪 登出</button>
    </div>

    <div class="footer">
      <p>🐾 本系統僅限喵喵貓咖管理員使用！</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted,ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const userrole = ref();

onMounted(async () => {
      const storedToken = localStorage.getItem('userCert');
      

      if (!storedToken) {
          alert('請先登入');
          router.push('/login');       
      }
      const userCert = JSON.parse(storedToken);
      userrole.value = userCert.role;
      
         
          if(userrole.value!== 'ADMIN'){
              alert('您沒有權限');
              router.push('/homepage');
                    }      
         
 
 
});

const handleLogout = async () => {
  try{
    localStorage.removeItem('userCert');
    alert("🐾 登出成功");
    router.push('/homepage');

  }catch(error){
    console.error("登出請求失敗:", error);
    localStorage.clear();
    router.push('/homepage');
   
  }
 
};
</script>

<style scoped>
/* 這裡封裝原本的 CSS 樣式 */
.admin-dashboard {
  font-family: 'Segoe UI', Tahoma, sans-serif;
  margin: 0;
  padding: 0;
  background: linear-gradient(135deg, #D4A574 0%, #8B4513 100%);
  color: #4A2C15;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.container {
  background: #FFF8E7;
  margin: 5rem auto;
  padding: 3rem 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(139,69,19,0.3);
  text-align: center;
  width: 90%;
  max-width: 600px;
  animation: fadeInUp 1s ease forwards;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #8B4513;
  text-shadow: 1px 1px 4px #D4A574;
}

.button {
  display: inline-block;
  margin: 12px;
  padding: 15px ;
  font-size: 1.2rem;
  font-weight: bold;
  color: #FFF8E7;
  background-color: #8B4513;
  border: none;
  border-radius: 40px;
  text-decoration: none;
  box-shadow: 0 5px 12px rgba(0,0,0,0.2);
  transition: background-color 0.3s ease, transform 0.3s ease;
  cursor: pointer;
}

.button:hover {
  background-color: #A0522D;
  transform: scale(1.05);
}

.logout-btn {
  background-color: #5c3010; /* 深色一點作為區隔 */
}

@keyframes fadeInUp {
  0% { opacity: 0; transform: translateY(30px); }
  100% { opacity: 1; transform: translateY(0); }
}

@media (max-width: 480px) {
  .button {
    width: 80%;
    font-size: 1rem;
    padding: 0.8rem 1rem;
  }
}

.footer {
  text-align: center;
  color: #E8D3B2;
  margin-top: auto;
  padding: 2rem 1rem;
  font-size: 0.9rem;
}
</style>