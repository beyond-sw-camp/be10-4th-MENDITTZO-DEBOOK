import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            // 메인 화면
            path : '/',
            name : 'Main',
            component : () => import('@/views/MainPage.vue')
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
        {
            path: '/booklists',
            component: () => import('@/views/BookListView.vue')
        },
        {
            path: '/booklists/:id',
            component: () => import('@/views/BookDetailView.vue')
        },
        {
            path: '/booklists/:id/review/create',
            component: () => import('@/views/BookReviewCreateView.vue')
        },
        {
            path: '/booklists/:bookId/review/:reviewId/edit',
            component: () => import('@/views/BookReviewEditView.vue')
        }
    ],
    scrollBehavior(to, from, savedPosition) {
        // 스크롤을 항상 맨 위로 이동
        return { top: 0 };
    }

});

export default router;