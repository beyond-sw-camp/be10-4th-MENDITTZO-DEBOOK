import { createApp } from 'vue'
import App from './App.vue'
import axios from "axios";
import router from "@/router";

axios.interceptors.request.use((config)=>{
    const accessToken = localStorage.getItem('accessToken');
    if(accessToken){
        config.headers.Authorization=`${accessToken}`
    }
    return config;
});

axios.defaults.baseURL = 'http://localhost:8080/api/v1';
const app = createApp(App); // Vue 애플리케이션 생성
app.config.globalProperties.axios = axios;
app.use(router);  // 애플리케이션에 라우터 추가
app.mount('#app');