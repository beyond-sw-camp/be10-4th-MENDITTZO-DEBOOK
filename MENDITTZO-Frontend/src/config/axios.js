import axios from 'axios';
import { useAuthStore } from "@/store/auth.js";
import router from "@/router/router.js";

// axios 인스턴스 생성
const instance = axios.create({
    // baseURL: 'http://localhost:8080/api/v1', // baseURL 설정
    baseURL: '/boot/api/v1' // ingress 설정으로 인한 변경
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
        if (error.response.headers['errorCode'] === 'EXPIRED_JWT') {
            const authStore = useAuthStore();
            try {
                // 리프레시 토큰으로 새 액세스 토큰 요청 보내기
                const response = await instance.post('/access-tokens', {
                    refreshToken: authStore.refreshToken
                });
                authStore.setAccessToken(response.data.accessToken);
                originalRequest.headers['Authorization'] = `Bearer ${authStore.accessToken}`;
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

// 로그아웃 요청 함수 추가
instance.logout = async function() {
    const authStore = useAuthStore();
    try {
        await instance.delete('/logout', {
            headers: {
                Authorization: `Bearer ${authStore.accessToken}`
            }
        });
        authStore.logout();
        router.push("/login");
    } catch (error) {
        console.error("로그아웃 실패:", error);
    }

};

// Elasticsearch 연결 확인 함수
async function checkElasticsearchConnection() {
    try {
        const response = await axios.get('http://localhost:9200/_cat/indices?v');
        console.log("Elasticsearch 응답:", response.data);
    } catch (error) {
        console.error("Elasticsearch와의 통신 실패:", error);
    }
}

// Elasticsearch 연결 상태 확인 함수 실행
checkElasticsearchConnection();

export default instance;
