<template>
  <q-app>
    <q-layout view="lHh Lpr lFf">
      <q-page-container>
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <template v-if="Component">
          <component :is="Component" />
        </template>
          </transition>
      </router-view>
      </q-page-container>
</q-layout>
      
    </q-app>
</template>

<script setup>
import { onMounted } from 'vue';

onMounted(() => {
  console.log('App 已掛載，正在初始化使用者狀態...');
});
</script>

<style>
/* 1. 解決字體過大問題 */
html {
  /* Quasar 預設基於 16px，如果你覺得太大，可以強制設回 14px */
  font-size: 14px !important; 
}

body {
  margin: 0;
  padding: 0;
  font-family: "Microsoft JhengHei", "PingFang TC", sans-serif;
  font-weight: 500 !important;
  background: var(--bg-gradient);
  min-height: 100vh;
  /* 覆蓋 Quasar 預設可能較大的行高 */
  line-height: 1.428 !important; 
}

/* 2. 修正 #app 佈局 */
#app {
  width: 100% !important;
  max-width: none !important;
  margin: 0 !important;
  padding: 0 !important;
}

/* 3. 你的變數設定 */
:root {
  --primary-color: #8B4513;
  --accent-color: #D4A574;
  --bg-gradient: linear-gradient(135deg, #fffaf4, #f5e8d3);
}

/* 頁面切換動畫 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 4. 額外：強制縮小 Quasar 標題的大小 (選用) */
h1 {
  font-size: 3.5rem !important;  
  line-height: 1.2 !important;
  letter-spacing: normal !important;
  margin: 16px 0 !important;     /* Quasar 的 h1 預設邊距也很大 */
}
h2 { 
  font-size: 3rem !important; 
}
h1, h2, h3, .text-h1, .text-h2, .text-h3 {
  font-weight: 600 !important; /* 直接給最粗 */
}

h4, h5, h6, .text-h4, .text-h5, .text-h6 {
  font-weight: 500 !important;
}


.q-app, .q-btn, .q-field {
  font-weight: 300 !important;
}
</style>