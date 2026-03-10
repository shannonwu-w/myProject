<template>
  <q-page class="q-pa-lg bg-grey-2">
    <q-card class="q-pa-md shadow-10 rounded-borders">
      <q-card-section class="text-h5 text-brown-9">
        JWT 測試頁面
      </q-card-section>

      <q-separator class="q-my-md" />

      <q-card-section>
        <q-btn label="測試 JWT" color="primary" @click="testJwt" />
      </q-card-section>

      <q-card-section>
        <div v-if="loading">測試中...</div>
        <div v-if="error" class="text-negative">錯誤: {{ error }}</div>
        <div v-if="result">
          <p><strong>Status:</strong> {{ result.status }}</p>
          <p><strong>Principal:</strong> {{ result.principal }}</p>
          <p><strong>Authorities:</strong> {{ result.authorities }}</p>
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const result = ref(null)
const error = ref(null)

const testJwt = async () => {
  loading.value = true
  error.value = null
  result.value = null

  try {
    const res = await request.get('/api/test')
    result.value = res
  } catch (err) {
    error.value = err.response?.data?.message || err.message
    console.error('JWT 測試錯誤:', err)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.q-card-section p {
  margin: 5px 0;
}
</style>