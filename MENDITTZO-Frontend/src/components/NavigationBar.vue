<script setup>
import { ref } from 'vue';

const isLogin = ref(true);
</script>

<template>
  <header>
    <div id="top-nav">
      <div>
        <img id="logo" src="../assets/image/logo.png" alt="로고이미지">
      </div>
      <div id="search-bar">
        <input id="search-input" type="text" placeholder="도서명 또는 저자를 입력하세요.">
        <img id="search-icon" src="../assets/image/search-icon.png" alt="검색아이콘">
      </div>
      <div>
        <ul class="login-logout" v-show="!isLogin">
          <li><img src="../assets/image/sign-up.png" alt="회원가입아이콘">회원가입</li>
          <li><img src="../assets/image/profile.png" alt="로그인아이콘">로그인</li>
        </ul>
        <ul class="login-logout" v-show="isLogin">
          <li><img src="../assets/image/profile.png" alt="회원아이콘">홍길동님</li>
          <li id="logout-button">로그아웃</li>
        </ul>
      </div>
    </div>
  </header>
</template>

<style scoped>
header{
  width: 1440px;
  padding: 0;
  margin: 0;
}
#top-nav{
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 예: 3열 그리드 */
  gap: 10px;
  place-items: center; /* 모든 셀의 콘텐츠를 가운데 정렬 */
  margin: 30px 0 15px 0;
}

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


</style>