<template>
  <q-page class="q-pa-lg bg-grey-2">
    <q-card class="q-pa-md shadow-10 rounded-borders">

      <!-- 標題 -->
      <q-card-section class="row items-center q-pb-none">
        <q-icon name="event_note" size="32px" color="brown-8" />
        <div class="text-h5 text-brown-9 q-ml-sm">顧客訂位總覽（測試版）</div>
      </q-card-section>

      <q-separator class="q-my-md" />

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

      <!-- 資料表 -->
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
          <template #no-data>
            <div class="text-grey-6 q-pa-lg">
              🔍 找不到相符的訂位資料
            </div>
          </template>
        </q-table>
      </q-card-section>

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

      <!-- 錯誤訊息 -->
      <q-banner v-if="errorMessage" class="bg-red-2 text-red-10 q-mt-md">
        ⚠ {{ errorMessage }}
      </q-banner>

    </q-card>
  </q-page>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '@/utils/request'

const searchQuery = ref('')
const displayList = ref([])
const loading = ref(false)
const errorMessage = ref('')

const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = ref(5)

const columns = [
  { name: 'reservationId', label: '編號', field: 'reservationId', align: 'center' },
  { name: 'resvDate', label: '日期', field: 'resvDate', align: 'center' },
  { name: 'timeSlot', label: '時段', field: 'timeSlot', align: 'center' },
  { name: 'name', label: '姓名', field: 'name', align: 'center' },
  { name: 'email', label: 'Email', field: 'email', align: 'center' },
  { name: 'phone', label: '電話', field: 'phone', align: 'center' },
  { name: 'people', label: '人數', field: 'people', align: 'center' },
  { name: 'message', label: '備註', field: 'message', align: 'center' }
]

const fetchReservations = async (page = 0) => {
  loading.value = true
  errorMessage.value = ''
  try {
    const res = await request.get('/api/reservation/search', {
      params: { keyword: searchQuery.value, page, size: pageSize.value }
    })
    displayList.value = res.content || []
    totalPages.value = res.totalPages || 1
    currentPage.value = page
  } catch (err) {
    console.error(err)
    errorMessage.value = err.response?.data?.error || '抓取資料失敗'
  } finally {
    loading.value = false
  }
}

const handleSearch = () => fetchReservations(0)
const resetSearch = () => {
  searchQuery.value = ''
  fetchReservations(0)
}

const currentPagePlusOne = computed({
  get: () => currentPage.value + 1,
  set: v => (currentPage.value = v - 1)
})

const pageChanged = page => fetchReservations(page - 1)

onMounted(() => {
  // JWT 權限檢查
  const certStr = localStorage.getItem('userCert')
  if (!certStr) {
    errorMessage.value = '未登入，無法抓取資料'
    return
  }
  const cert = JSON.parse(certStr)
  if (cert.role !== 'ADMIN') {
    errorMessage.value = '沒有管理員權限'
    return
  }
  fetchReservations(0)
})
</script>

<style scoped>
.q-btn {
  margin-left: 10px;
}
</style>