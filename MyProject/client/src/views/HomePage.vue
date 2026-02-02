<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'


const router = useRouter()

// --- 狀態管理 ---
const userCert = ref(null) //存放使用者憑證 (例如 username)
const role = ref('') // 存放權限資訊 (例如 USER 或 ADMIN)
const isScrolled = ref(false)
const username = ref('訪客')


// --- 資料清單 (將原本重複的 HTML 轉為資料驅動) ---
const catFamily = ref([
  { name: '小橘', desc: '活潑好動的橘貓，最愛玩逗貓棒', img: 'images/orangecat.png' },
  { name: '雪球', desc: '溫柔的白色長毛貓，喜歡默默地觀察大家', img: '/images/whitecat.png' },
  { name: '歐嚕嚕', desc: '神秘的黑貓，聰明伶俐，愛躲在奇怪地方', img: '/images/blackcat.png' },
  { name: '花花', desc: '美麗的三花貓，性格獨立但很親人', img: '/images/calicocat.png' },
  { name: '小寶', desc: '嬌小的虎斑貓，最愛撒嬌求摸摸', img: '/images/tabbycat.png' },
  { name: '哼哼', desc: '沉穩的英短貓，溫和、安靜討人喜歡', img: '/images/graycat.png' }
])

const menuData = ref([
  {
    category: '☕ 精品咖啡',
    items: [
      { name: '美式咖啡', price: 'NT$ 120' },
      { name: '拿鐵咖啡', price: 'NT$ 150' },
      { name: '卡布奇諾', price: 'NT$ 140' },
      { name: '貓爪拉花特調', price: 'NT$ 180' }
    ]
  },
  {
    category: '🫖 特色茶飲',
    items: [
      { name: '招牌奶茶', price: 'NT$ 110' },
      { name: '蘋果冰茶', price: 'NT$ 130' },
      { name: '花草茶', price: 'NT$ 100' },
      { name: '貓薄荷特調', price: 'NT$ 160' }
    ]
  },
  {
    category: '🥯 早午餐套餐',
    items: [
      { name: '歐嗨唷早餐盤', price: 'NT$ 260' },
      { name: '貓掌鬆餅餐盤', price: 'NT$ 240' },
      { name: '鮭魚酪梨貝果', price: 'NT$ 250' }
    ]
  },
  {
    category: '🍝 麵飯主食',
    items: [
      { name: '喵皇咖哩豬排飯', price: 'NT$ 230' },
      { name: '肉丸起司焗烤飯', price: 'NT$ 240' },
      { name: '貓耳朵肉醬義大利麵', price: 'NT$ 220' }
    ]
  },
  {
    category: '🧁 美味點心',
    items: [
      { name: '貓爪造型馬卡龍', price: 'NT$ 90' },
      { name: '黑白貓起司蛋糕', price: 'NT$ 120' },
      { name: '貓咪造型餅乾', price: 'NT$ 60' }
    ]
  },
  {
    category: '🍟 加購小點',
    items: [
      { name: '雙色地瓜球', price: 'NT$ 80' },
      { name: '日式薯條', price: 'NT$ 130' },
      { name: '脆皮起司條', price: 'NT$ 80' }
    ]
  }
])

// --- 邏輯處理 ---
 onMounted(async () => {
  checkLoginStatus() })

const checkLoginStatus = () => {
  
    
    const stored = localStorage.getItem('userCert');
        username.value = 'userCert.username'; 

    if (stored) {
        userCert.value = JSON.parse(stored)
        username.value = userCert.value.username || '訪客'
        role.value = userCert.value.role || ''
        console.log('登入使用者：', userCert.value)
    }
    
  
}

const navigateTo = (path) => {
  router.push(path)
}

const handleLogout = async () => {
  try{
    // const response = await axios.get('api/logout');
    localStorage.removeItem('userCert');
    alert("🐾 登出成功，期待下次見面！");
    router.push('/login');

    // console(response);


  }catch(error){
    console.error("登出請求失敗:", error);
    localStorage.clear();
    router.push('/login');
  }
 
};
</script>

<template>
  <div class="home-container">
    <nav class="navbar" :class="{ 'navbar-scrolled': isScrolled }">
      <div class="nav-container1">
        <div class="logo">🐱 喵喵貓咖</div>
        <div class="nav-buttons">
          <template v-if="!userCert">
            <span class="user-name-tag" >尚未登入</span>
            <button class="nav-btn" @click="navigateTo('/register')">註冊</button>
            <button class="nav-btn" @click="navigateTo('/login')">登入</button>
          </template>

          <template v-else>
            <span class="user-name-tag">您好，{{username}}</span>
            
            <button v-if="role === 'USER'" class="nav-btn" @click="navigateTo('/userpage')">會員中心</button>
            <button v-if="role === 'USER'" class="nav-btn" @click="navigateTo('/bookingpage')">訂位</button>

            <button v-if="role === 'ADMIN'" class="nav-btn" @click="navigateTo('/adminpage')">後台管理</button>
            <button v-if="role === 'ADMIN'" class="nav-btn" @click="navigateTo('/userpage')">使用者頁面</button>
            <button v-if="role === 'ADMIN'" class="nav-btn" @click="navigateTo('/bookingpage')">訂位</button>


            <button class="nav-btn" @click="handleLogout">登出</button>
          </template>
        </div>
      </div>
    </nav>

  <section class="hero">
      <div class="hero-inner">
        <h1>歡迎來到喵喵貓咖</h1>
        <p>在溫馨的環境中，與可愛的貓咪們共度美好時光</p>
      </div>
    </section>

    <section class="about">
      <h2>關於我們</h2>
      <div class="about-text">
        <p>喵喵貓咖是一個充滿溫暖與愛的地方，我們致力於為貓咪提供一個安全舒適的家，同時讓愛貓人士能夠在這裡放鬆身心。我們的每一隻貓咪都經過悉心照料，性格溫順親人，歡迎來跟貓貓玩，貓貓也都很歡迎大家唷~</p>
        <br />
        <p>在這裡，您可以一邊品嚐精心調製的咖啡和美味點心，一邊與貓咪互動玩耍。我們相信，貓咪的陪伴能夠帶給人們內心的平靜與快樂，讓繁忙的生活節奏得到舒緩。</p>
        <br />
        <p>我們也積極參與貓咪救助活動，店內的部分貓咪正在尋找永遠的家。如果您在這裡遇到了心動的小夥伴，歡迎考慮領養，給牠們一個溫暖的家。</p>
      </div>
    </section>

    <section class="cat-gallery">
      <div class="gallery-container">
        <h2>我們的貓咪家族</h2>
        <div class="cat-grid">
          <div v-for="cat in catFamily" :key="cat.name" class="cat-card">
            <img :src="cat.img" :alt="cat.name" class="cat-photo" />
            <div class="cat-name">{{ cat.name }}</div>
            <div class="cat-description">{{ cat.desc }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="reservation">
      <div class="reservation-content">
        <h2>現在就來預約吧！</h2>
        <p>點擊下方按鈕開始預約，與可愛的貓咪們度過美好時光</p>
        <button class="reservation-btn" @click="navigateTo('/bookingpage')">🐾 開始訂位</button>
      </div>
    </section>

    <section class="menu">
      <div class="menu-container">
        <h2>精選菜單</h2>
        <div class="menu-grid">
          <div v-for="category in menuData" :key="category.category" class="menu-category">
            <h3>{{ category.category }}</h3>
            <div v-for="item in category.items" :key="item.name" class="menu-item">
              <span class="item-name">{{ item.name }}</span>
              <span class="item-price">{{ item.price }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <footer class="footer">
      <div class="footer-container">
        <div class="footer-section">
          <h3>📞 聯絡電話</h3>
          <p>(02) 2345-6789</p>
          <p>營業時間：周一到周日 10:00 - 20:00</p>
        </div>
        <div class="footer-section">
          <h3>✉️ 電子信箱</h3>
             <a href="mailto:meowmeowcafe@gmail.com"  > meowmeowcafe@gmail.com </a>
        </div>
        <div class="footer-section">
          <h3>📍 店面地址</h3>
          <p>台北市大安區咖啡街123號</p>
          <p>捷運大安站2號出口站步行5分鐘</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2026 喵喵貓咖 All Rights Reserved. | 與貓咪一起創造美好回憶 🐾</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
/* 全域基本樣式 (scoped) */
.hero,
.cat-gallery,
.reservation,
.menu,
.footer {
  /* 將 100vw 改為 100%，這樣它們會緊貼父容器的邊緣 */
  width: 100%; 
  box-sizing: border-box;
}

.home-container {
  width: 100%;
  min-width: 100%;        
  margin: 0;
  padding: 0;
  display: block;        /* 改回 block，避免 flex 造成的子元素縮排問題 */
  background-color: #fef8f0;
  font-family: 'Arial', '微軟正黑體', sans-serif;
  text-align: center;    /* 讓內部的行內元素或文字預設居中 */
  overflow-x: hidden; 

}


/* .home-container {
  width: 100%;
  margin: 0 auto;

  padding: 0;
  overflow-x: hidden;
  box-sizing: border-box;
  font-family: 'Arial', '微軟正黑體', sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #fef8f0;
} */


/* --- Navigation Bar --- */
.navbar {
  background: linear-gradient(135deg, #8B4513, #D4A574);
  padding: 1rem 0;
  position: fixed;
  width: 100%;
  left: 0;
  right: 0;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  box-sizing: border-box;
  
}

.navbar-scrolled {
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.95), rgba(212, 165, 116, 0.95));
  backdrop-filter: blur(10px);
}

.nav-container1 {
  width: 100%;
  max-width: 1100px;     /* 限制內容最大寬度，避免分太開 */
  margin: 0 auto;        /* 關鍵：這會讓導覽列內容水平置中 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5%;         /* 使用百分比 padding，確保不同螢幕都有呼吸空間 */
}

/* .nav-container1 {
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
} */

.logo {
  font-size: 2rem;
  font-weight: bold;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  cursor: pointer;
}

.nav-buttons {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.user-name-tag {
  color: white;
  font-weight: bold;
}

.nav-btn {
  background: rgba(255, 255, 255, 0.9);
  color: #8B4513;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.nav-btn:hover {
  background: #fff;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

/* --- Hero Section --- */
.hero {
  width: 100%;           
  min-height:   100vh;
  display: flex;
  flex-direction: column; 
  align-items: center;    
  justify-content: center;
  margin: 0;
  padding: 100px 20px;   /* 稍微給點左右 padding 讓文字不貼邊 */
  /* 加入下面這行確保漸層或背景圖鋪滿 */
  background: linear-gradient(rgba(139, 69, 19, 0.1), rgba(212, 165, 116, 0.1)),
    url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 600"><rect fill="%23f5e6d3" width="1000" height="600"/><circle fill="%23e6d2b7" cx="200" cy="150" r="80"/><circle fill="%23d4a574" cx="800" cy="100" r="60"/><path fill="%23c99660" d="M0,300 Q250,200 500,300 T1000,300 V600 H0 Z"/></svg>');
  background-size: cover;
  background-position: center;
}

/* .hero {
  background: linear-gradient(rgba(139, 69, 19, 0.1), rgba(212, 165, 116, 0.1)),
    url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 600"><rect fill="%23f5e6d3" width="1000" height="600"/><circle fill="%23e6d2b7" cx="200" cy="150" r="80"/><circle fill="%23d4a574" cx="800" cy="100" r="60"/><path fill="%23c99660" d="M0,300 Q250,200 500,300 T1000,300 V600 H0 Z"/></svg>');
  background-size: cover;
  background-position: center;
  min-height: 50vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 100px 20px;
} */

.hero-inner h1 {
  font-size: 3.5rem;
  color: #8B4513;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.hero-inner p {
  font-size: 1.3rem;
  color: #666;
  max-width: 600px;
  margin: 0 auto;
}

/* --- About Section --- */
.about {
  padding: 5rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.about h2 {
  font-size: 2.5rem;
  color: #8B4513;
  margin-bottom: 2rem;
  position: relative;
}

.about h2::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  height: 4px;
  background: linear-gradient(135deg, #8B4513, #D4A574);
  border-radius: 2px;
}

.about-text {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #555;
  max-width: 800px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.8);
  padding: 2rem;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

/* --- Cat Gallery --- */
.cat-gallery {
  padding: 5rem 2rem;
  background: linear-gradient(135deg, #fef8f0, #f5e6d3);
}

.gallery-container {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.gallery-container h2 {
  font-size: 2.5rem;
  color: #8B4513;
  margin-bottom: 3rem;
}

.cat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.cat-card {
  background: #fff;
  border-radius: 20px;
  padding: 1.5rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.cat-card:hover {
  transform: translateY(-15px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.cat-photo {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 15px;
  margin-bottom: 1rem;
}

.cat-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: #8B4513;
  margin-bottom: 0.5rem;
}

.cat-description {
  color: #666;
  font-size: 0.9rem;
}

/* --- Reservation Section --- */
.reservation {
  padding: 5rem 2rem;
  text-align: center;
  /* 此處路徑應依專案調整 */
  background: linear-gradient(rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.8)),
    url('/images/reservation-bg.png');
  background-size: cover;
  background-position: center;
}

.reservation-btn {
  background: linear-gradient(135deg, #8B4513, #D4A574);
  color: white;
  border: none;
  padding: 1.5rem 3rem;
  font-size: 1.3rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 30px rgba(139, 69, 19, 0.3);
}

.reservation-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(139, 69, 19, 0.4);
}

/* --- Menu Section --- */
.menu {
  padding: 5rem 2rem;
  background: #fff;
}

.menu-container {
  /* max-width: 100vw; */
  margin: 0 auto;
  text-align: center;
}

.menu h2 {
  font-size: 2.5rem;
  color: #8B4513;
  margin-bottom: 3rem;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.menu-category {
  background: linear-gradient(135deg, #fef8f0, #f5e6d3);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.menu-category h3 {
  font-size: 1.5rem;
  color: #8B4513;
  margin-bottom: 1.5rem;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding: 0.5rem 0;
  border-bottom: 1px dotted #D4A574;
}

.menu-item:last-child {
  border-bottom: none;
}

.item-name {
  font-weight: 600;
  color: #333;
}

.item-price {
  color: #8B4513;
  font-weight: 600;
}

/* --- Footer --- */
.footer {
  background: linear-gradient(135deg, #8B4513, #D4A574);
  color: white;
  padding: 3rem 2rem 2rem;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  text-align: center;
}

.footer-section h3 {
  font-size: 1.3rem;
  margin-bottom: 1rem;
  color: #fff;
}

.footer-section p {
  margin-bottom: 0.5rem;
  color: rgba(255, 255, 255, 0.9);
}

.footer-section a {
  margin-bottom: 0.5rem;
  color: rgba(255, 255, 255, 0.9);
}

.footer-bottom {
  text-align: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  color: rgba(255, 255, 255, 0.8);
}


/* --- RWD --- */
@media (max-width: 800px) {
  .hero-content h1 {
    font-size: 2.5rem;
  }
  .nav-container1
   {
    padding: 0 1rem;
  }
  .logo {
    font-size: 1.5rem;
  }
  .nav-btn {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }
}
</style>
