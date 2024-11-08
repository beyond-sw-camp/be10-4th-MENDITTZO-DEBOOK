<script setup>

import {useAuthStore} from "@/store/auth.js";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";
import axios from "axios";

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

onMounted(async () => {

  try {

    // url 쿼리 파라미터에서 토큰 추출
    const accessToken = route.query.accessToken;
    const refreshToken = route.query.refreshToken;

    if (accessToken && refreshToken) {
      authStore.setTokens(accessToken, refreshToken);
    }
    // 홈 화면으로 이동
    router.push("/");
  } catch (error) {
    console.error("로그인 요청 실패", error);
  }
});
</script>

<template>
<h1>로그인 성공!</h1>
<RouterLink to="/login-success">홈으로 돌아가기</RouterLink>
</template>

<style scoped>

</style>