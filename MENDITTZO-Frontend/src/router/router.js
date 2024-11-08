import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            // 메인 화면
            path : '/',
            name : 'Main',
            component : () => import('@/views/HomeView.vue')
        },
        {
            // 로그인 화면
            path: '/login',
            component: () => import('@/views/login/LoginForm.vue')
        },
        {
            // 로그인 성공 화면
            path: '/login-success',
            component: () => import('@/views/login/LoginSuccessView.vue')
        },
        {
            // 마이페이지 화면
            path: '/mypage',
            component: () => import('@/views/MyPage.vue')
        },
    ]
});

export default router;