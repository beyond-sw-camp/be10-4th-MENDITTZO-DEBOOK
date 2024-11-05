import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            // 메인 화면
            path : '/',
            name : 'Main',
            component : () => import('@/views/MainView.vue')
        },
        {
            // 로그인 화면
            path: '/login',
            component: () => import('@/views/login/LoginForm.vue')
        },
    ]
});

export default router;