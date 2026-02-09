<template>
  <q-page class="main-layout q-pa-md">
    <div class="container q-mb-md">
      <h1 class="text-h4 text-weight-bold q-my-md">喵喵貓咖使用者管理系統</h1>
      <div v-if="userrole === 'ADMIN'" class="row justify-left q-gutter-sm">
        <q-btn label="回管理員首頁" color="brown-5" @click="goTo('adminpage')" rounded />
        <q-btn label="訂位管理" color="brown-5" @click="goTo('all-bookings')" rounded />
        <q-btn label="喵喵貓咖首頁" color="brown-5" @click="goTo('homepage')" rounded />
        <q-btn icon="logout" label="登出" color="brown-6" @click="handleLogout" rounded />
      </div>
    </div>

    <div class="container q-mb-lg">
      <q-table
        title="使用者列表"
        :rows="userList"
        :columns="columns"
        row-key="userId"
        :loading="loading"
        v-model:pagination="pagination"
        @request="onRequest"
        flat
      >
        <template v-slot:top-right>
          <q-input borderless dense debounce="300" v-model="searchKeyword" placeholder="搜尋..." @keyup.enter="fetchUsers">
            <template v-slot:append>
              <q-icon name="search" class="cursor-pointer" @click="fetchUsers" />
            </template>
          </q-input>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props" class="q-gutter-xs">
            <q-btn size="sm" color="blue-4" icon="edit" label="修改" @click="editUser(props.row)" />
            <q-btn size="sm" color="red-3" icon="delete" label="刪除" @click="deleteUser(props.row)" />
          </q-td>
        </template>
      </q-table>
    </div>

    <q-dialog v-model="showEditDialog" persistent>
      <q-card style="min-width: 350px" class="q-pa-md">
        <q-card-section>
          <div class="text-h6">🔧 修改使用者資料</div>
        </q-card-section>

        <q-form @submit="saveUpdate">
          <q-card-section class="q-gutter-md">
            <q-input v-model="selectedUser.username" label="使用者名稱" dense filled :rules="[val => !!val || '必填']" />
            <q-input 
              v-model="selectedUser.email" 
              label="帳號(Email)" 
              dense filled 
              :rules="[
                val => !!val || '必填',
                val => /.+@.+\..+/.test(val) || 'Email 格式不正確'
              ]" 
            />
            <q-input 
              v-model="selectedUser.phone" 
              label="電話" 
              dense filled 
              mask="##########"
              hint="請輸入 10 位數字"
              :rules="[val => val && val.length === 10 || '電話需為 10 碼']" 
            />
            <q-select v-model="selectedUser.role" :options="roleOptions" label="身分" dense filled emit-value map-options />
            <q-input 
              v-model="selectedUser.password" 
              type="password" 
              label="新密碼 (留空不改)" 
              dense filled 
              hint="若要修改請輸入 6 碼以上"
              :rules="[val => !val || val.length >= 6 || '密碼至少 6 碼']"
            />
          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="取消" v-close-popup />
            <q-btn label="儲存修改" color="brown-8" type="submit" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>

    <div class="container q-mt-lg">
      <h2 class="text-h5 text-center q-mb-md">➕ 新增使用者</h2>
      <q-form @submit="addUser" ref="newUserForm" class="q-gutter-md">
        <div class="row q-col-gutter-md">
          <q-input class="col-12 col-sm-6" v-model="newUser.username" label="名稱" dense filled :rules="[val => !!val || '必填']" />
          <q-input 
            class="col-12 col-sm-6" 
            v-model="newUser.email" 
            label="Email" 
            dense filled 
            :rules="[val => /.+@.+\..+/.test(val) || '格式錯誤']" 
          />
          <q-input 
            class="col-12 col-sm-6" 
            v-model="newUser.password" 
            type="password" 
            label="密碼" 
            dense filled 
            :rules="[val => val && val.length >= 6 || '密碼需 6 碼以上']" 
          />
          <q-input 
            class="col-12 col-sm-6" 
            v-model="newUser.phone" 
            label="電話" 
            dense filled 
            mask="##########"
            :rules="[val => val && val.length === 10 || '電話需 10 碼']" 
          />
          <q-select class="col-12 col-sm-6" v-model="newUser.role" :options="roleOptions" label="身分" dense filled emit-value map-options />
        </div>
        <div class="text-center">
          <q-btn label="建立帳號" color="brown-8" icon="add" type="submit" rounded size="lg" />
        </div>
      </q-form>
    </div>

    <div class="footer text-center q-mt-xl">
      <p>🐾 本系統僅限喵喵貓咖管理員使用！</p>
    </div>
  </q-page>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useQuasar } from 'quasar';

const $q = useQuasar();
const router = useRouter();
const userrole = ref('');
const loading = ref(false);
const userList = ref([]);
const searchKeyword = ref('');
const showEditDialog = ref(false);
const selectedUser = ref({});
const newUserForm = ref(null);
const newUser = ref({ username: '', email: '', password: '', phone: '', role: 'USER' });

const roleOptions = [
  { label: '一般使用者', value: 'USER' },
  { label: '管理員', value: 'ADMIN' }
];

// QTable 欄位定義
const columns = [
  { name: 'username', label: '使用者名稱', field: 'username', align: 'left'},
  { name: 'email', label: '帳號(Email)', field: 'email', align: 'left' },
  { name: 'phone', label: '電話', field: 'phone', align: 'center' },
  { name: 'role', label: '身分', field: 'role', align: 'center' },
  { name: 'actions', label: '操作', align: 'center' }
];

const pagination = ref({
  page: 1,
  rowsPerPage: 5,
  rowsNumber: 0 // 總筆數，由後端回傳
});

onMounted(() => {
  const storedToken = localStorage.getItem('userCert');
  if (!storedToken) {
    $q.notify({ type: 'warning', message: '請先登入' });
    router.push('/login');
    return;
  }
  const userCert = JSON.parse(storedToken);
  userrole.value = userCert.role;
  if (userrole.value !== 'ADMIN') {
    $q.notify({ type: 'negative', message: '沒有權限' });
    router.push('/homepage');
    return;
  }
  fetchUsers();
});

// 處理 QTable 的分頁請求
const onRequest = (props) => {
  const { page, rowsPerPage } = props.pagination;
  pagination.value.page = page;
  pagination.value.rowsPerPage = rowsPerPage;
  fetchUsers();
};

const fetchUsers = async () => {
  loading.value = true;
  try {
    const res = await axios.get('/api/admin/find-users', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.value.page - 1, // Quasar 是 1-based, Spring 是 0-based
        size: pagination.value.rowsPerPage
      }
    });
    userList.value = res.data.content;
    pagination.value.rowsNumber = res.data.totalElements;
  } catch (error) {
    $q.notify({ type: 'negative', message: '載入失敗' +error });
  } finally {
    loading.value = false;
  }
};

const editUser = (user) => {
  selectedUser.value = { ...user, password: '' };
  showEditDialog.value = true;
};

const saveUpdate = async () => {
  try {
    const response = await axios.post('/api/admin/update-user', selectedUser.value);
    if (response.data.includes("成功")) {
      $q.notify({ type: 'positive', message: '更新成功' });
      showEditDialog.value = false;
      fetchUsers();
    }
  } catch (error) {
    $q.notify({ type: 'negative', message: '更新失敗'+ error });
  }
};

const deleteUser = (user) => {
  $q.dialog({
    title: '確定刪除？',
    message: `將刪除使用者「${user.username}」，此操作無法復原。`,
    cancel: true,
    color: 'negative'
  }).onOk(async () => {
    try {
      await axios.post('/api/admin/deleteUser', null, { params: { userId: user.userId } });
      $q.notify({ type: 'positive', message: '已刪除' });
      fetchUsers();
    } catch (e) {
      $q.notify({ type: 'negative', message: '刪除失敗'+ e });
    }
  });
};

const addUser = async () => {
  try {
    const response = await axios.post('/api/admin/update-user', newUser.value);
    if (response.data.includes("成功")) {
      $q.notify({ type: 'positive', message: '建立成功' });
      newUser.value = { username: '', email: '', password: '', phone: '', role: 'USER' };
      setTimeout(() => {
        if (newUserForm.value) {
          newUserForm.value.resetValidation();
        }
      }, 0);
      
      fetchUsers();
    }
  } catch (error) {
    $q.notify({ type: 'negative', message: '新增失敗'+error });
  }
};

const handleLogout = () => {
  localStorage.removeItem('userCert');
  $q.notify('🐾 登出成功');
  router.push('/homepage');
};

const goTo = (routeName) => {
  router.push({ name: routeName });
};
</script>

<style scoped>
.main-layout {
  background: linear-gradient(135deg, #D4A574 0%, #8B4513 100%);
  min-height: 100vh;
  padding: 40px 20px;
}
.container {
  background: rgba(255, 248, 231, 0.9);
  backdrop-filter: blur(10px);
  padding: 2rem;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  
  /* 置中核心設定 */
  width: 95%;          /* 在手機上佔 95% 寬度 */
  max-width: 1000px;   /* 最大寬度鎖定在 1000px */
  margin-left: auto;   /* 左右 auto 是置中的靈魂 */
  margin-right: auto;
}

/* 讓表格容器配合外層 container */
.q-table__container {
  background-color: transparent !important; /* 背景交給外層 container 控制 */
  box-shadow: none !important;
}

/* 調整表格標題列顏色 */
:deep(.q-table thead tr th) {
  background-color: rgba(206, 113, 47, 0.288); /* 淡淡的咖啡色底 */
  color: #8B4513;
  font-weight: 900;
  font-size: 1rem;
}

/* 滑鼠經過效果 */
:deep(.q-table tbody tr:hover) {
  background-color: rgba(216, 175, 146, 0.089);
}

.footer { color: #E8D3B2; }
</style>