import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  withCredentials: true // 如果後端需要 cookie，可保留
})

// 請求攔截器：自動附上 JWT
request.interceptors.request.use(
  config => { 
    const certStr = localStorage.getItem('userCert')
    if (certStr) {
      const cert = JSON.parse(certStr)
      if (cert.token) {
        config.headers.Authorization = `Bearer ${cert.token}`
        console.log('token from localStorage:', cert.token)
      }
    }
    return config
  },
  error => Promise.reject(error)
)

// 回應攔截器：**不自動跳轉 /login**
request.interceptors.response.use(
  response => response.data,
  error => {
    // 仍可捕捉 401 但不跳轉
    if (error.response && error.response.status === 401) {
      console.warn('API 回傳 401，可能未登入或 token 過期', error.response)
      // 可在此統一顯示提示訊息，或在 Vue 內自行處理
    }
    return Promise.reject(error)
  }
)

export default request