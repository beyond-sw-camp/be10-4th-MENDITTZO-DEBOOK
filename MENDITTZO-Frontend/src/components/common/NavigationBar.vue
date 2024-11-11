<script setup>
import {computed, ref} from 'vue';
import {useAuthStore} from "@/store/auth.js";
import router from "@/router/router.js";

const authStore = useAuthStore();
import {RouterLink} from "vue-router";
import instance from "@/config/axios.js";

// accessToken 이 있으면 로그인 한 상태
const isLogin = computed(() => !!authStore.accessToken);

// 로그아웃
const handleLogout = async () => {

  try {
    // await instance.delete("/logout", {
    //   headers: {Authorization: `Bearer ${authStore.accessToken}`},
    // });

    // Pinia 스토어, 로컬 스토리지에서 토큰 삭제
    authStore.logout();
    router.push("/login");
  } catch (error) {
    console.error("로그아웃 실패: ", error);
  }

};

// 마이페이지로 이동 시 사용자 정보 불러오기
const handleMyPage = async () => {

  await authStore.fetchUserInfo();  // 사용자 정보를 백엔드에서 가져와 업데이트
};
</script>

<template>
  <header>
    <div id="top-nav">
      <div>
        <RouterLink to="/" active-class="active" replace>
        <img id="logo" src="../../assets/image/logo.png" alt="로고이미지">
        </RouterLink>
      </div>

      <div id="search-bar">
        <input id="search-input" type="text" placeholder="도서명 또는 저자를 입력하세요.">
        <img id="search-icon" src="../../assets/image/search-icon.png" alt="검색아이콘">
      </div>

      <div>
        <ul class="login-logout" v-if="!isLogin">
          <li>
            <RouterLink to="/login" class="login-logout-button">
              <img src="../../assets/image/sign-up.png" alt="회원가입아이콘">회원가입
            </RouterLink>
          </li>
          <li>
            <RouterLink to="/login" class="login-logout-button">
              <img src="../../assets/image/profile.png" alt="로그인아이콘">로그인
            </RouterLink>
          </li>
        </ul>

        <ul class="login-logout" v-if="isLogin">
          <li>
            <RouterLink to ="/mypage" class ="mypage-button">
              <img src="../../assets/image/profile.png" alt="회원아이콘">{{ authStore.nickname }} 님
            </RouterLink>
          </li>
          <li id="logout-button" @click="handleLogout">로그아웃</li>
        </ul>
      </div>
    </div>

    <div id="bottom-nav">
      <div>
        <RouterLink to="/booklists" active-class="active" replace>
          <p class="nav-bottom-text">도서목록</p>
        </RouterLink>
      </div>
      <div><p class="nav-bottom-text">도서추천</p></div>
      <div><p class="nav-bottom-text">독서토론방</p></div>
    </div>

  </header>

  <hr id="bottom-hr">
</template>

<style scoped>
a{
  text-decoration: none;
}
/* header */
header{
  width: 1440px;
  padding: 0;
  margin: 0 auto; /* 좌우 여백 자동으로 설정하여 가운데 정렬 */
}

/* 네비게이션 하단 선 */
#bottom-hr{
  border: none;
  height: 1px;
  background-color: #78AE6B;
}

/* 네비바 상단 */
#top-nav{
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  place-items: center;
  margin: 30px 0 15px 0;
}

/* 네비바 하단 */
#bottom-nav{
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 예: 3열 그리드 */
  gap: 10px;
  place-items: center; /* 모든 셀의 콘텐츠를 가운데 정렬 */
}

/*이미지 파일*/
#logo{
  width: 150px;
  height: 30px;
  cursor: pointer;
}
#search-icon{
  width: 30px;
  height: 30px;
  margin-top: 12px;
  cursor: pointer;
}

/* 검색바 */
#search-bar{
  display: flex;
  justify-content: center;
  border: 2px solid #78AE6B;
  border-radius: 15px;
  padding: 0 10px 0 10px;
}
#search-input{
  width: 400px;
  height: 50px;
  border: none;
  margin: 0 10px 0 10px;
}
#search-input:focus{
  box-shadow: none;
  outline: none;
}

/* 로그인,로그아웃 버튼 영역 */
.login-logout{
  list-style: none;
  display: flex;
  margin: 10px 10px 0 10px ;
  white-space: nowrap;
  padding: 0;
  text-align: center;
}
.login-logout > li{
  display: flex;
  margin: 0 10px 0 10px ;
  white-space: nowrap;
  color: #78AE6B;
  font-weight: bold;
  justify-content: center;  /* 수평 중앙 정렬 */
  align-items: center;      /* 수직 중앙 정렬 */
  cursor: pointer;
}
#logout-button{
  margin-left: 10px;
  width: 80px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border-radius: 10px;
}

/* routerlink 기본 스타일 제거 */
.login-logout-button{
  text-decoration: none; /* 밑줄 제거 */
  color: inherit;        /* 텍스트 색상을 상속받아 기본 색으로 설정 */
  display: flex;
  align-items: center;   /* 아이콘과 텍스트 수직 정렬 */
  font-weight: bold;     /* 스타일 통일을 위해 굵게 설정 */
}
/* routerlink 기본 스타일 제거 */
.mypage-button{
  text-decoration: none; /* 밑줄 제거 */
  color: inherit;        /* 텍스트 색상을 상속받아 기본 색으로 설정 */
  display: flex;
  align-items: center;   /* 아이콘과 텍스트 수직 정렬 */
  font-weight: bold;     /* 스타일 통일을 위해 굵게 설정 */
}

/* 네비게이션 하단 버튼 텍스트 */
.nav-bottom-text{
  white-space: nowrap;
  color: #444444;
  font-weight: bold;
  justify-content: center;  /* 수평 중앙 정렬 */
  align-items: center;      /* 수직 중앙 정렬 */
  cursor: pointer;
}

.active .nav-bottom-text {
  color: #78AE6B; /* 활성화 상태일 때 색상 */
}

</style>