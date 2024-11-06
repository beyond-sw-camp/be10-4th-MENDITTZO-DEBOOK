import {defineStore} from "pinia";
import {onMounted, ref} from "vue";

// 어디서든 사용할 수 있는 userAuthStore
export const useAuthStore = defineStore('auth', () => {

    const accessToken = ref(null);
    const refreshToken = ref(null);
    const loginId = ref(null);
    const nickname = ref(null);

    // 페이지가 로드될 때마다 localStorage 에서 토큰을 읽어와 상태를 초기화한다.
    onMounted(() => {

        const access = localStorage.getItem('accessToken');
        const refresh = localStorage.getItem('refreshToken');

        if (access) {
            accessToken.value = access;
            // JWT 토큰의 페이로드 추출
            const payload = JSON.parse(atob(access.split('.')[1]));
            loginId.value = payload.loginId;
            nickname.value = payload.nickname;
        }

        if (refresh) {
            refreshToken.value = refresh;
        }
    });

    // 로그아웃(클라이언트 상태 초기화 용도. 실제 로그아웃을 통한 토큰 무효화는 서버에서 처리)
    const logout = () => {

        accessToken.value = null;
        refreshToken.value = null;
        loginId.value = null;
        nickname.value = null;
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
    };

    // 토큰 저장
    const setTokens = (newAccessToken, newRefreshToke) => {
        accessToken.value = newAccessToken;
        refreshToken.value = newRefreshToke;
        localStorage.setItem("accessToken", newAccessToken);
        localStorage.setItem("refreeshToken", newRefreshToke);
    };

    return {accessToken, refreshToken, loginId, nickname};
});