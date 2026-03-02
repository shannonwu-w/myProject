<template>
  <div class="success-page">
    <nav class="navbar" aria-label="主要導覽">
      <router-link to="/bookingpage">➕ 再訂一筆</router-link>
      <router-link to="/bookinghistory">📜 訂位資料</router-link>
      <router-link to="/uploadmenu">🍽️ 查看菜單</router-link>
    </nav>

    <main class="container" role="main" aria-labelledby="mainTitle">
      <h1 id="mainTitle">預約成功！🎉</h1>
      <p>感謝您的預約，我們已收到您的訂位資訊 📝。</p>
      <p>期待您的光臨，祝您用餐愉快！🍽️</p>
      
      <button @click="handleGoHome" class="button" aria-label="返回首頁">
        🏠 返回首頁
      </button>
    </main>

    <footer class="footer" role="contentinfo">
      <p>🏠 餐廳地址：台北市大安區忠孝東路四段 123 號</p>
      <p>📞 連絡電話：(02) 1234-5678</p>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

// 處理返回首頁的邏輯（含原本要求的平滑滾動效果）
const handleGoHome = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' });
  
  // 稍微延遲後執行路由跳轉，模擬原本 JSP 腳本的效果
  setTimeout(() => {
    router.push('/homepage');
  }, 400);
};
</script>

<style scoped>
/* 將原本 body 的背景搬移至 success-page 容器 */
.success-page {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin: 0;
  padding: 0;
  background: linear-gradient(135deg, #D4A574 0%, #cc7d44 100%);
  color: #3E2F1C;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 導覽列 */
.navbar {
  background: rgba(139, 69, 19, 0.85);
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 2rem;
  padding: 1rem 0;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar a {
  color: #573b09;
  text-decoration: none;
  font-weight: 600;
  padding: 0.5rem 1.5rem;
  border-radius: 25px;
  background: linear-gradient(45deg, #cc6c28, #D4A574);
  /* background: #e6b37e; */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 1rem;
}

.navbar a:hover {
  background-color: rgba(211, 165, 116, 0.5);
  color: #6F4E37;
  transform: scale(1.05);
  box-shadow: 0 5px 15px rgba(211, 165, 116, 0.5);
}

/* 內容容器 */
.container {
  background: #FDF6E3;
  max-width: 600px;
  width: 90%;
  margin: 4rem auto 2rem;
  padding: 3rem 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(139, 69, 19, 0.2), 0 6px 6px rgba(211, 165, 116, 0.4);
  text-align: center;
  animation: fadeInUp 1s ease forwards;
}

h1 {
  font-size: 2.8rem;
  margin-bottom: 1rem;
  color: #8B4513;
  text-shadow: 1px 1px 3px #D4A574;
}

p {
  font-size: 1.2rem;
  margin: 0.6rem 0;
  line-height: 1.5;
}

/* 主要按鈕 */
.button {
  margin-top: 2rem;
  background: linear-gradient(45deg, #8B4513, #D4A574);
  color: #FFF8E7;
  font-weight: 700;
  font-size: 1.1rem;
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 30px;
  box-shadow: 0 6px 12px rgba(211, 165, 116, 0.7);
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.button:hover {
  background: linear-gradient(45deg, #D4A574, #8B4513);
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(211, 165, 116, 0.9);
}

/* 頁尾 */
.footer {
  width: 100%;
  background: rgba(139, 69, 19, 0.9);
  color: #F3E5D3;
  text-align: center;
  padding: 1.5rem 1rem;
  font-size: 0.95rem;
  margin-top: auto;
}

.footer p {
  margin: 0.2rem 0;
  font-size: 0.95rem;
}

/* 動畫 */
@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(40px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 響應式 */
@media (max-width: 480px) {
  .navbar {
    flex-direction: column;
    align-items: center;
    gap: 1rem;
  }
  .container {
    padding: 2rem 1rem;
  }
  h1 { font-size: 2.2rem; }
}
</style>