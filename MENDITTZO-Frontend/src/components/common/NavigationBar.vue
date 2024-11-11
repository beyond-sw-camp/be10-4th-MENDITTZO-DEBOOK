<script setup>
import { computed, ref } from 'vue';
import { useAuthStore } from "@/store/auth.js";
import router from "@/router/router.js";
import instance from "@/config/axios.js";
import { RouterLink } from "vue-router";

const authStore = useAuthStore();
const isLogin = computed(() => !!authStore.accessToken);
const searchQuery = ref(""); // 검색어 상태
const searchResults = ref([]); // 검색 결과 상태

// 로그아웃
const handleLogout = async () => {
  try {
    // Pinia 스토어, 로컬 스토리지에서 토큰 삭제
    await instance.delete("/logout", {
      headers: { Authorization: `Bearer ${authStore.accessToken}` },
    });
    authStore.logout();
    router.push("/login");
  } catch (error) {
    console.error("로그아웃 실패: ", error);
  }
};

// 마이페이지로 이동 시 사용자 정보 불러오기
const handleMyPage = async () => {
  await authStore.fetchUserInfo(); // 사용자 정보를 백엔드에서 가져와 업데이트
};

// 자동완성 검색 함수
const handleSearch = async () => {
  if (searchQuery.value.trim() === "") {
    searchResults.value = []; // 검색어가 없으면 결과를 비웁니다
    return;
  }

  try {
    const response = await instance.get("/public/elastic/autocomplete", {
      params: {query: searchQuery.value},
    });
    searchResults.value = response.data; // 검색 결과 업데이트
  } catch (error) {
    console.error("자동완성 결과를 가져오는 중 오류가 발생했습니다:", error);
  }
};

// 도서 선택 시 상세 페이지로 이동
const goToBookDetail = (bookId) => {
  router.push(`/booklists/${bookId}`);
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
        <input
            id="search-input"
            type="text"
            placeholder="검색할 도서명을 입력하세요."
            v-model="searchQuery"
            @input="handleSearch"
        />
        <img id="search-icon" src="../../assets/image/search-icon.png" alt="검색아이콘">

        <!-- 자동완성 결과 -->
        <ul v-if="searchResults.length" class="autocomplete-results">
          <li
              v-for="result in searchResults"
              :key="result.id"
              class="autocomplete-item"
              @click="goToBookDetail(result.bookId)"
          >
            {{ result.title }} - {{ result.author }}
          </li>
        </ul>
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
            <RouterLink to="/mypage" class="mypage-button">
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
</template>

<style scoped>
a {
  text-decoration: none;
}

header {
  width: 1440px;
  padding: 0;
  margin: 0 auto;
}


#bottom-hr {
  border: none;
  height: 1px;
  background-color: #78AE6B;
}

#top-nav {

  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  place-items: center;
  margin: 30px 0 15px 0;
}

#bottom-nav {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  place-items: center;

}

#logo {
  width: 150px;
  height: 30px;
  cursor: pointer;
}

#search-icon {
  width: 30px;
  height: 30px;
  margin-top: 12px;
  cursor: pointer;
}

#search-bar {
  display: flex;
  justify-content: center;
  border: 2px solid #78AE6B;
  border-radius: 15px;
  padding: 0 10px;
  position: relative;
}

#search-input {
  width: 400px;
  height: 50px;
  border: none;
  margin: 0 10px;
}

#search-input:focus {
  box-shadow: none;
  outline: none;
}

.login-logout {
  list-style: none;
  display: flex;
  margin: 10px 10px 0 10px;
  white-space: nowrap;
  padding: 0;
  text-align: center;
}

.login-logout > li {
  display: flex;
  margin: 0 10px;
  color: #78AE6B;
  font-weight: bold;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

#logout-button {
  margin-left: 10px;
  width: 80px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border-radius: 10px;
}

.login-logout-button {
  text-decoration: none;
  color: inherit;
  display: flex;
  align-items: center;
  font-weight: bold;
}

.mypage-button {
  text-decoration: none;
  color: inherit;
  display: flex;
  align-items: center;
  font-weight: bold;
}


.nav-bottom-text {
  white-space: nowrap;
  color: #444444;
  font-weight: bold;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.active .nav-bottom-text {
  color: #78AE6B;
}

/* 자동완성 드롭다운 스타일 */
.autocomplete-results {
  position: absolute;
  top: 60px;
  left: 0;
  width: 100%;
  background-color: white;
  border: 1px solid #ddd;
  list-style: none;
  margin: 0;
  padding: 0;
  border-radius: 5px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 10;
}

.autocomplete-item {
  padding: 10px;
  cursor: pointer;
}

.autocomplete-item:hover {
  background-color: #f0f0f0;
}
</style>
