import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/router.js";
import {createPinia} from "pinia";

const app = createApp(App); // Vue 애플리케이션 생성
const pinia = createPinia();

app.use(pinia);
app.use(router);  // 애플리케이션에 라우터 추가
app.mount('#app');