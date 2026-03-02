
<template>
  <q-page class="admin-dashboard flex flex-center">
    
    <q-card class="container shadow-10">
      <q-card-section class="text-center">
        <h1 class="text-weight-bold">喵喵貓咖管理系統</h1>
      </q-card-section>

      <q-card-section class="q-pa-md row justify-center q-gutter-md">
        <q-btn
          to="/all-users"
          icon="person"
          label="使用者管理"
          class="admin-btn"
          rounded
          size="lg"
        />
        
        <q-btn
          to="/all-bookings"
          icon="receipt_long"
          label="訂位管理"
          class="admin-btn"
          rounded
          size="lg"
        />

        <q-btn
          to="/homepage"
          icon="home"
          label="喵喵貓咖訂位系統"
          class="admin-btn"
          rounded
          size="lg"
        />

        <q-btn
          @click="handleLogout"
          icon="logout"
          label="登出"
          class="admin-btn logout-btn"
          rounded
          size="lg"
        />
      </q-card-section>

      <q-card-section class="footer">
        <p>🐾 本系統僅限喵喵貓咖管理員使用！</p>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useQuasar } from 'quasar';

const router = useRouter();
const $q = useQuasar();
const userrole = ref('');

onMounted(() => {
  const storedToken = localStorage.getItem('userCert');

  if (!storedToken) {
    $q.notify({ 
      message: '請先登入',
      color: 'brown-6', 
      icon: 'pets',     
      position: 'top', 
      timeout: 1000
    });
    router.push('/login');
    return;
  }

  const userCert = JSON.parse(storedToken);
  userrole.value = userCert.role;

  if (userrole.value.toUpperCase() !== 'ADMIN') {
    $q.notify({ type: 'negative', message: '您沒有權限進入管理系統' });
    router.push('/homepage');
  }
});

const handleLogout = () => {
  $q.dialog({
    title: '確認登出',
    message: '您確定要離開喵喵貓咖管理系統嗎？',
    cancel: true,
    persistent: true,
    color: 'brown-6'
  }).onOk(async () => {
    localStorage.removeItem('userCert');
    $q.notify({
      color: 'brown-6',
      message: '🐾 登出成功',
      icon: 'door_open',
      position: 'top'
    });
    router.push('/homepage');
  });
};
</script>

<style scoped>
/* 背景與佈局 */
.admin-dashboard {
  background: linear-gradient(135deg, #D4A574 0%, #8B4513 100%);
  min-height: 100vh;
}

/* 容器樣式微調 */
.container {
  background: #FFF8E7;
  padding: 2rem;
  border-radius: 20px;
  width: 90%;
  max-width: 600px;
  /* 加入你原本的向上淡入動畫 */
  animation: fadeInUp 0.8s ease-out forwards;
}

h1 {
  font-size: 2.5rem;
  color: #8B4513;
  text-shadow: 1px 1px 4px rgba(212, 165, 116, 0.5);
  margin: 0.5rem 0 1.5rem 0;
}

/* 按鈕樣式：讓按鈕在小螢幕時能自適應 */
.admin-btn {
  width: 240px; 
  font-weight: bold;
  color: #FFF8E7;
  background-color: #8B4513;
}


.footer {
  text-align: center;
  color: #8B4513;
  opacity: 0.8;
  font-size: 1.2rem;
}

@keyframes fadeInUp {
  0% { opacity: 0; transform: translateY(30px); }
  100% { opacity: 1; transform: translateY(0); }
}

/* RWD 手機版調整 */
@media (max-width: 480px) {
  .admin-btn {
    width: 100%;
  }
  h1 {
    font-size: 1.8rem;
  }
}
</style>