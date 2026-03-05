<template>
  <q-page class="q-pa-lg bg-grey-2">
    <q-card class="q-pa-md shadow-10 rounded-borders">

      <!-- 標題 -->
      <q-card-section class="row items-center q-pb-none">
        <q-icon name="event_note" size="32px" color="brown-8" />
        <div class="text-h5 text-brown-9 q-ml-sm">
          顧客訂位總覽
        </div>
      </q-card-section>

      <q-separator class="q-my-md" />

      <!-- 功能按鈕 -->
      <q-card-section>
        <div class="row q-col-gutter-sm justify-center">
          <q-btn icon="dashboard" label="管理首頁" color="brown-7" to="/adminpage" />
          <q-btn icon="people" label="使用者管理" color="brown-7" to="/all-users" />
          <q-btn icon="event" label="訂位頁面" color="brown-7" to="/bookingpage" />
          <q-btn icon="pets" label="喵喵貓咖" color="brown-7" to="/homepage" />
          <q-btn icon="logout" label="登出" color="negative" @click="handleLogout" />
        </div>
      </q-card-section>

      <!-- 搜尋 -->
      <q-card-section>
        <div class="row q-col-gutter-sm items-center justify-center">
          <q-input
            outlined
            dense
            clearable
            v-model="searchQuery"
            label="輸入條件"
            class="col-12 col-md-6"
            @keyup.enter="handleSearch"
          >
            <template #prepend>
              <q-icon name="search" />
            </template>
          </q-input>

          <q-btn icon="search" label="搜尋" color="primary" @click="handleSearch" />
          <q-btn icon="restart_alt" label="重設" flat color="grey-7" @click="resetSearch" />
        </div>
      </q-card-section>

      <!-- 表格 -->
      <q-card-section>
        <q-table
          flat
          bordered
          separator="horizontal"
          :rows="displayList"
          :columns="columns"
          row-key="reservationId"
          :loading="loading"
          hide-pagination
        >
          <template #body-cell-actions="props">
            <q-td align="center">
              <q-btn dense round icon="edit" color="brown-7"
                     @click="editReservation(props.row.reservationId)" />
              <q-btn dense round icon="delete" color="negative" class="q-ml-xs"
                     @click="deleteReservation(props.row.reservationId)" />
            </q-td>
          </template>

          <template #no-data>
            <div class="text-grey-6 q-pa-lg">
              🔍 找不到相符的訂位資料
            </div>
          </template>
        </q-table>
      </q-card-section>

      <q-separator />

      <!-- 分頁 -->
      <q-card-section class="row justify-between items-center">
        <q-select
          dense
          outlined
          v-model="pageSize"
          :options="[5, 10, 20]"
          label="每頁筆數"
          style="width: 130px"
          @update:model-value="fetchReservations(0)"
        />

        <q-pagination
          v-model="currentPagePlusOne"
          :max="totalPages"
          boundary-links
          direction-links
          color="brown-7"
          @update:model-value="pageChanged"
        />
      </q-card-section>

    </q-card>
  </q-page>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()

// 資料與狀態
const searchQuery = ref('')
const displayList = ref([])
const loading = ref(false)
const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = ref(5)

// 表格欄位
const columns = [
  { name: 'reservationId', label: '編號', field: 'reservationId', align: 'center' },
  { name: 'resvDate', label: '日期', field: 'resvDate', align: 'center' },
  { name: 'timeSlot', label: '時段', field: 'timeSlot', align: 'center' },
  { name: 'name', label: '姓名', field: 'name', align: 'center' },
  { name: 'email', label: 'Email', field: 'email', align: 'center' },
  { name: 'phone', label: '電話', field: 'phone', align: 'center' },
  { name: 'people', label: '人數', field: 'people', align: 'center' },
  { name: 'message', label: '備註', field: 'message', align: 'center' },
  { name: 'actions', label: '操作', align: 'center' }
]

// 取訂位資料
const fetchReservations = async (page = 0) => {
  loading.value = true
  try {
    const res = await request.get('/api/reservation/search', {
      params: { keyword: searchQuery.value, page, size: pageSize.value }
    })
    displayList.value = res.content
    totalPages.value = res.totalPages
    currentPage.value = page
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 搜尋 & 重設
const handleSearch = () => fetchReservations(0)
const resetSearch = () => {
  searchQuery.value = ''
  fetchReservations(0)
}

// 編輯 / 刪除
const editReservation = id => router.push({ path: '/edit', query: { id } })
const deleteReservation = async id => {
  if (!confirm('確定要刪除這筆訂位嗎？')) return
  await request.delete(`/api/reservation/${id}`)
  fetchReservations(currentPage.value)
}

// 登出
const handleLogout = async () => {
  try { await request.post('/api/logout') } 
  finally {
    localStorage.removeItem('userCert')
    localStorage.removeItem('userRole')
    router.push('/login')
  }
}

// 分頁
const currentPagePlusOne = computed({
  get: () => currentPage.value + 1,
  set: v => currentPage.value = v - 1
})
const pageChanged = page => fetchReservations(page - 1)

// 權限檢查
onMounted(() => {
  const cert = JSON.parse(localStorage.getItem('userCert'))
  if (!cert || cert.role !== 'ADMIN') {
    alert('您沒有權限')
    router.push('/homepage')
    return
  }
  fetchReservations(0)
})
</script>

<style scoped>
.q-btn { margin-left: 10px; }
</style>