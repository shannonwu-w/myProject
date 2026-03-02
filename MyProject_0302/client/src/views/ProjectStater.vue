<template>
  <div class="nav-container">
   <button class="btn"  @click="navigateTo('/homepage')">首頁</button>
   <button  class="btn"  @click="navigateTo('/login')">登入</button>

    <hr />

    <RouterView />
  </div>
</template>

<script setup>

import { RouterView , useRouter} from 'vue-router'

// 1. 初始化路由實例
const router = useRouter()

// 2. 定義跳轉函式
const navigateTo = (path) => {
  router.push(path)
}

</script>


<style scoped>
.nav-container {
  padding: 20px;
  text-align: center;
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  margin: 10px;
  background-color: #8B4513;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: bold;
  transition: background 0.3s;
}

.btn:hover {
  background-color: #5f3010;
}

hr {
  margin: 20px 0;
  border: 0;
  border-top: 1px solid #ddd;
}
</style>