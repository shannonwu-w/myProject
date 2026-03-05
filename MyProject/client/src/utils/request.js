import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  withCredentials: true
})

request.interceptors.request.use(
  config => {
    const certStr = localStorage.getItem('userCert')
    if (certStr) {
      const cert = JSON.parse(certStr)
       console.log('token from localStorage:', cert.token) 
      if (cert.token) {
        config.headers.Authorization = `Bearer ${cert.token}`
      }
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('userCert')
      localStorage.removeItem('userRole')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default request