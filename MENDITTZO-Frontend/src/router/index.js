import {createRouter, createWebHistory} from "vue-router";
import MyPage from "@/views/MyPage.vue";
import BookListView from "@/views/BookListView.vue";
import MainPage from "@/views/MainPage.vue";
import BookDetailView from "@/views/BookDetailView.vue";
import BookReviewCreateView from "@/views/BookReviewCreateView.vue";

const routes = [

    {
        path: '/myPage',
        component: MyPage // 검색 결과 페이지
    },
    {
        path: '/booklists',
        component: BookListView
    },
    {
        path: '/',
        component: MainPage
    },
    {
        path: '/booklists/:id',
        component: BookDetailView
    },
    {
        path: '/booklists/:id/create',
        component: BookReviewCreateView
    },
    {
        path: '/booklists/:id/edit',
        component: BookReviewCreateView
    }

];

const router = createRouter({
    history: createWebHistory(),
    routes,
    // 라우팅 시 화면 최 상단으로 이동됨.
    scrollBehavior(to, from, savedPosition) {
        // savedPosition 이 있는 경우(예: 뒤로 가기), 해당 위치로 이동
        if (savedPosition) {
            return savedPosition;
        } else {
            // 새로운 페이지 이동 시 맨 위로 스크롤
            return { top: 0 };
        }
    }
});

export default router;