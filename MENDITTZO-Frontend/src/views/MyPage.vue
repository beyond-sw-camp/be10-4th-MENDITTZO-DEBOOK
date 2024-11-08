<script setup>
import {reactive, ref} from "vue";
import {useAuthStore} from "@/store/auth.js";

const authStore = useAuthStore();
const user = reactive();
const reviews = ref([]);
const chatrooms = ref([]);

</script>

<template>
  <section>
    <p class="menu">나의 프로필</p>
    <hr class="cross">
    <article id="top-div">
      <div>
        <p class="middle-text">닉네임</p>
        <div id="nick-bar">
          <input id="nick-input" type="text" placeholder="홍길동">
          <img id="nick-icon" src="https://img.icons8.com/?size=100&id=4299&format=png&color=000000" alt="검색아이콘">
        </div>
        <button class="myButton">수정하기</button>
      </div>

      <div id="profile-right">
        <p class="middle-text">프로필사진</p>
        <div class="profile-image-container">
          <img id="profile-img" :src="user.img" alt="프로필 사진">
        </div>
      </div>
    </article>

    <p class="menu">나의 리뷰</p>
    <hr class="cross">
    <article class="list-div">
      <div class="review" v-for="review in reviews">
        <div class="review-left">
          <img src="../assets/image/bookmark.png" alt="북마크이미지">
          <p class="review-title">{{review.title}}</p>
        </div>
        <div class="review-right">
          <div class="rating">
            <img src="../assets/image/star.png" alt="별" v-for="n in review.rating">
            <img src="../assets/image/empty-star.png" alt="빈별" v-for="n in 5-review.rating">
          </div>
          <p class="list_info">{{review.book_title}}</p>
          <p class="list_info">{{review.author}}</p>
          <div>
            <img class="review-icons" src="../assets/image/modify.png" alt="수정 아이콘">
            <img class="review-icons" src="../assets/image/delete.png" alt="삭제 아이콘">
          </div>
        </div>
      </div>
      <button class="myButton">전체보기</button>
    </article>

    <p class="menu">참가중인 독서 토론방</p>
    <hr class="cross">
    <article class="list-div">
      <div class="chat" v-for="chatroom in chatrooms">
        <div class="chat-left">
          <img src="../assets/image/bookmark2.png" alt="북마크이미지">
          <p class="review-title">{{chatroom.title}}</p>
        </div>
        <div class="chat-right">
          <p class="list_info">{{chatroom.book_title}}</p>
          <p class="list_info">{{chatroom.member_count}}/{{chatroom.max_member_count}}</p>
        </div>
      </div>
      <button class="myButton">전체보기</button>
    </article>
    <p class="menu">회원관리</p>
    <hr class="cross">
    <button class="myButton" id="delete-button">탈퇴하기</button>
  </section>
</template>

<style scoped>
section {
  width: 1000px;
  margin: 0 auto 100px auto;
  padding: 30px 50px;
}
#profile-right{
  margin-left: 100px;
}
.menu {
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
  padding: 30px 50px;
}
.list-div{
  padding: 50px
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
.myButton {
  margin: 30px 10px;
  width: 100px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 15px;
  float: right;
}
#delete-button{
  background-color: #F24822;
  margin-right: 60px;
}

.review{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 10px;
  border-bottom: 1px solid #888888;
}
.review-left{
  display: grid;
  grid-template-columns: 50px auto;
  justify-content: start;
}
.review-right{
  display: grid;
  grid-template-columns: 120px 150px 80px 60px;
  justify-content: end; /* 왼쪽 정렬 */
}
.review-title{
  margin: 5px 0;
  font-weight: bold;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  cursor: pointer;
}
.rating{
  margin-top: 5px;
}
.list_info{
  margin: 7px 10px;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  color: #666666;
  font-size: 13px;
}
.review-icons{
  margin-top: 5px;
  margin-right: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.chat{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 10px;
  border-bottom: 1px solid #888888;
}
.chat-left{
  display: grid;
  grid-template-columns: 50px auto;
  justify-content: start;
}
.chat-right{
  display: grid;
  grid-template-columns: auto 60px;
  justify-content: end; /* 왼쪽 정렬 */
}
</style>