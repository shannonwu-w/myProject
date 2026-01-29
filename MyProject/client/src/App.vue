<template>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>


<script setup>
/**
 * 這裡可以放置全域的邏輯，例如：
 * 1. 初始化時檢查 LocalStorage 的 Token 是否過期
 * 2. 監聽全域的錯誤訊息
 */
import { onMounted } from 'vue';


onMounted(() => {
  console.log('App 已掛載，正在初始化使用者狀態...');
});
</script>


<style>
/* 這裡放置「全域」樣式，不需要加上 scoped */
#app {
  width: 100%;       /* 確保它是滿寬的 */
  max-width: none;   /* 移除最大寬度限制 */
  margin: 0;         /* 移除外邊距 */
  padding: 0;        /* 移除內邊距，這最重要！ */
}
:root {
  --primary-color: #8B4513;
  --accent-color: #D4A574;
  --bg-gradient: linear-gradient(135deg, #fffaf4, #f5e8d3);
}


body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  background: var(--bg-gradient);
  min-height: 100vh;
}


/* 頁面切換時的淡入淡出動畫 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}


.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

