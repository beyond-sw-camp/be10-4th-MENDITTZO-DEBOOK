import { defineStore } from "pinia";
import { onMounted, ref } from "vue";
import axios from "axios";

// 어디서든 사용할 수 있는 userAuthStore
export const useAuthStore = defineStore('auth', () => {

    const accessToken = ref(null);
    const refreshToken = ref(null);
    const loginId = ref(null);
    const nickname = ref(null);
    const status = ref(null);
    const profileImg = ref(null);
    const userId = ref(null);

    // 페이지가 로드될 때마다 localStorage 에서 토큰을 읽어와 상태를 초기화한다.
    onMounted(() => {
        const access = localStorage.getItem('accessToken');
        const refresh = localStorage.getItem('refreshToken');

        if (access) {
            accessToken.value = access;
            try {
                // JWT 토큰의 페이로드 추출
                const base64url = access.split('.')[1];
                const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
                const payload = JSON.parse(atob(base64));

                loginId.value = payload.loginId;
                nickname.value = payload.nickname;

            } catch (error) {
                console.error("토큰 디코딩 중 에러: ", error);
            }

            fetchUserInfo();
        }

        if (refresh) {
            refreshToken.value = refresh;
        }
    });

    // 사용자 정보 요청
    const fetchUserInfo = async () => {
        try {
            const response = await axios.get("/user/query/info", {
                headers: { Authorization: `Bearer ${accessToken.value}` },
            });
            const data = response.data;
            nickname.value = data.nickName;
            profileImg.value = data.profileImg;
            status.value = data.status;
            userId.value = data.userId;

        } catch (error) {
            console.error("사용자 정보 가져오기 실패: ", error);
            if (error.response && error.response.status === 401) {
                // 토큰이 만료되었거나 인증이 실패한 경우 로그아웃 요청
                console.warn("인증 실패: 로그아웃 수행");
                logout();
            }
        }
    };

    // 로그아웃(클라이언트 상태 초기화 + 서버 로그아웃 처리)
    const logout = async () => {
        try {
            await axios.delete("/logout", {
                headers: { Authorization: `Bearer ${accessToken.value}` },
            });
        } catch (error) {
            console.error("로그아웃 요청 실패: ", error);
        } finally {
            accessToken.value = null;
            refreshToken.value = null;
            loginId.value = null;
            nickname.value = null;
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        }
    };


    // 토큰 저장
    const setTokens = (newAccessToken, newRefreshToken) => {
        accessToken.value = newAccessToken;
        refreshToken.value = newRefreshToken;
        localStorage.setItem("accessToken", newAccessToken);
        localStorage.setItem("refreshToken", newRefreshToken);

        fetchUserInfo();
    };

    // 액세스 토큰 저장
    const setAccessToken = (newAccessToken) => {
        accessToken.value = newAccessToken;
        localStorage.setItem("accessToken", newAccessToken);

        fetchUserInfo();
    };

    // 로그인 id 저장
    const setLoginId = (newLoginId) => {
        loginId.value = newLoginId;
        localStorage.setItem("loginId", newLoginId);
    };

    return {
        accessToken,
        refreshToken,
        loginId,
        nickname,
        status,
        profileImg,
        userId,
        logout,
        setTokens,
        setLoginId,
        fetchUserInfo,
        setAccessToken
    };
});
