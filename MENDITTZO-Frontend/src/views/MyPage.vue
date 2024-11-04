<script setup>
import {reactive} from "vue";

const user = reactive(
    {
      id:1,
      img:`https://cdn.pixabay.com/photo/2019/07/23/13/51/shepherd-dog-4357790_1280.jpg`
    }
);
</script>

<template>
  <section>
    <p class="title">나의 프로필</p>
    <hr class="cross">
    <article id="top-div">
      <div>
        <p class="middle-text">닉네임</p>
        <div id="nick-bar">
          <input id="nick-input" type="text" placeholder="도서명 또는 저자를 입력하세요.">
          <img id="nick-icon" src="https://img.icons8.com/?size=100&id=4299&format=png&color=000000" alt="검색아이콘">
        </div>
        <button id="modify-button">수정하기</button>
      </div>

      <div id="profile-right">
        <p class="middle-text">프로필사진</p>
        <div class="profile-image-container">
          <img id="profile-img" :src="user.img" alt="프로필 사진">
        </div>
      </div>
    </article>

  </section>
</template>

<style scoped>
section {
  width: 1000px;
  margin: 0 auto;
  padding: 50px;
}
#profile-right{
  margin-left: 100px;
}
.title {
  font-size: 30px;
  font-weight: bold;
  color: #444444;
  padding-left: 20px;
}
.middle-text {
  font-size: 20px;
  font-weight: bold;
  color: #444444;
  padding-left: 10px;
}
.cross {
  border: none;
  height: 1px;
  background-color: #888888;
}
#top-div{
  display: flex;
  padding: 50px;
}

#nick-icon{
  width: 20px;
  height: 20px;
  margin-top: 20px;
  margin-right: 15px;
  cursor: pointer;
}
#profile-img{
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 검색바 */
#nick-bar{
  display: flex;
  justify-content: center;
  border: 2px solid #78AE6B;
  border-radius: 15px;
  padding: 0 10px 0 10px;
  margin-bottom: 50px;
}
#nick-input{
  width: 500px;
  height: 60px;
  border: none;
  margin: 0 10px 0 10px;
}
#nick-input:focus{
  box-shadow: none;
  outline: none;
}
.profile-image-container {
  width: 200px;
  height: 200px;
  border-radius: 50%; /* 원형으로 만들기 */
  overflow: hidden; /* 넘치는 부분 숨기기 */
  display: flex;
  align-items: center;
  justify-content: center;
}
#modify-button {
  margin-left: 10px;
  width: 100px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 15px;
}
</style>