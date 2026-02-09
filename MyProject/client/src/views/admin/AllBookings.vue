<template>
  <q-page class="q-pa-lg bg-grey-2">


    <q-card class="q-pa-md shadow-10 rounded-borders">


      <!-- 標題區 -->
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
          <q-btn  icon="dashboard" label="管理首頁" color="brown-7" to="adminpage"/>
          <q-btn  icon="people" label="使用者管理" color="brown-7" to="/all-users"/>
          <q-btn  icon="event" label="訂位頁面" color="brown-7" to="/bookingpage"/>
          <q-btn  icon="pets" label="喵喵貓咖" color="brown-7"  to="/homepage"/>
          <q-btn  icon="logout" label="登出" color="negative" @click="handleLogout" />
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
            label="姓名 / 電話 / Email"
            class="col-12 col-md-6"
            @keyup.enter="handleSearch"
            
          >
            <template #prepend>
              <q-icon name="search" />
            </template>
          </q-input>


          <q-btn
            icon="search"
            label="搜尋"
            color="primary"
            unelevated
            rounded
            @click="handleSearch"
          />


          <q-btn
            icon="restart_alt"
            label="重設"
            flat
            color="grey-7"
            @click="resetSearch"
          />
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
            v-model:pagination="pagination" 
            hide-pagination
            class="rounded-borders"
          >
         
         
        
          <template #body-cell-actions="props">
            <q-td align="center">
              <q-btn
                dense
                round
                icon="edit"
                color="brown-7"
                @click="editReservation(props.row.reservationId)"
              />
              <q-btn
                dense
                round
                icon="delete"
                color="negative"
                class="q-ml-xs"
                @click="deleteReservation(props.row.reservationId)"
              />
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
        <q-card-section class="row justify-between items-center">

          <!-- 每頁筆數 -->
          <q-select
            dense
            outlined
            v-model="pageSize"
            :options="[5, 10, 20]"
            label="每頁筆數"
            style="width: 130px"
            @update:model-value="fetchReservations(0)"
          />

          <!-- 分頁按鈕 -->
          <q-pagination
            v-model="currentPagePlusOne"
            :max="totalPages"
            direction-links
            boundary-links
            color="brown-7"
            @update:model-value="pageChanged"
          />
        </q-card-section>




    </q-card>
  </q-page>
</template>




<script setup>
import { ref, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { computed } from 'vue'



const router = useRouter()


const userrole = ref('')
const searchQuery = ref('')
const displayList = ref([])


const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = ref(5)
const loading = ref(false)



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

const pagination = ref({
  sortBy:'username',
  descending: true,
  page: 1,           // Quasar pagination 從 1 開始
  rowsPerPage: 5,    // 每頁幾筆
  rowsNumber: 0      // 總筆數 (選填)
})

const fetchReservations = async (page = 0) => {
  loading.value = true
  try {
    const res = await axios.get('/api/reservation/search', {
      params: {
        keyword: searchQuery.value,
        page,
        size: pageSize.value
      }
    })
    displayList.value = res.data.content
    totalPages.value = res.data.totalPages
    currentPage.value = page
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}


const handleSearch = () => fetchReservations(0)


const resetSearch = () => {
  searchQuery.value = ''
  fetchReservations(0)
}



const editReservation = id => {
  router.push({ path: '/edit', query: { id } })
}


const deleteReservation = async id => {
  if (!confirm('確定要刪除這筆訂位嗎？')) return
  await axios.post(`/api/reservation/delete/${id}`)
  fetchReservations(currentPage.value)
}


const handleLogout = async () => {
  await axios.get('/api/logout')
  localStorage.removeItem('userCert')
  router.push('/login')
}

const currentPagePlusOne = computed({
  get: () => currentPage.value + 1,
  set: v => (currentPage.value = v - 1)
})

const pageChanged = page => {
  fetchReservations(page - 1)
}

onMounted(() => {
  const cert = JSON.parse(localStorage.getItem('userCert'))
  if (!cert || cert.role !== 'ADMIN') {
    alert('您沒有權限')
    router.push('/homepage')
    return
  }
  userrole.value = cert.role
  fetchReservations(0)
})
</script>




<style scoped>
.q-btn{
  margin-left: 10px;
}


</style>

