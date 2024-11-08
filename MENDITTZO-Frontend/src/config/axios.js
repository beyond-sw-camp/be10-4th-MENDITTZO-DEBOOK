import axios from 'axios';
import { useAuthStore } from "@/store/auth.js";
import router from "@/router/router.js";


// axios 인스턴스 생성
const instance = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // baseURL 설정
});

// 요청 인터셉터 : 모든 요청에 액세스 토큰을 포함
instance.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore();
        if (authStore.accessToken) {
            config.headers['Authorization'] = `Bearer ${authStore.accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 인터셉터
instance.interceptors.response.use(
    (response) => response,
    async (error) => {

        const originalRequest = error.config;

        // 액세스 토큰 만료 감지
        // error.response.status === 401 && error.response.data.errorCode === 'EXPIRED_JWT'
        if (error.response.headers['errorCode'] === 'EXPIRED_JWT') {
            const authStore = useAuthStore();
            try {

                // 리프레시 토큰으로 새 액세스 토큰 요청 보내기
                const response = await instance.post('/access-tokens', {
                    refreshToken: authStore.refreshToken.value,
                });

                // 새 엑세스 토큰을 Pinia 스토어, 로컬 스토리지에 저장
                authStore.setAccessToken(response.data.accessToken);

                //  원래 요청에 새 액세스 토큰을 헤더에 넣어 보내기
                originalRequest.headers['Authorization'] = `Bearer ${authStore.accessToken.value}`;

                // 요청 재시도
                return instance(originalRequest);
            } catch (refreshError) {

                // 리프레시 토큰으로 인증 실패 시 로그아웃
                authStore.logout();
                router.push("/login");
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default instance;