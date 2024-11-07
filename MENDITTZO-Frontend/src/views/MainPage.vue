<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/swiper-bundle.css'; // Swiper 스타일
import {Navigation, Pagination} from "swiper/modules";
import {ref} from "vue";

const images = [
  "/src/assets/image/ad1.png",
  "/src/assets/image/ad2.png",
  "/src/assets/image/ad3.png"
];

const chatrooms = ref([
  {
    "chatroom_id": 1,
    "book_id": 6352228,
    "book_title": "함께 읽는 고전문학",
    "open_datetime": "2024-11-01 10:00:00",
    "max_member_count": 10,
    "member_count": 7,
    "title": "고전문학 독서 모임",
    "status": "active"
  },
  {
    "chatroom_id": 2,
    "book_id": 6352229,
    "book_title": "함께 읽는 과학",
    "open_datetime": "2024-11-02 14:00:00",
    "max_member_count": 10,
    "member_count": 3,
    "title": "과학 서적 토론",
    "status": "active"
  },
  {
    "chatroom_id": 3,
    "book_id": 6352230,
    "book_title": "함께 읽는 동서양",
    "open_datetime": "2024-11-03 18:30:00",
    "max_member_count": 3,
    "member_count": 2,
    "title": "역사책 함께 읽기",
    "status": "active"
  }
]);
</script>

<template>

  <div class="my-swiper">
    <Swiper
        :modules="[Navigation, Pagination]"
        navigation
        pagination
        loop="true"
        class="my-swiper-container"
    >
      <SwiperSlide v-for="(image, index) in images" :key="index">
        <img class="slide-image" :src="image" alt="Slide Image" />
      </SwiperSlide>
    </Swiper>
  </div>

  <div id="books-and-chatrooms">
    <article class="list-div">
      <div class="chat" v-for="chatroom in chatrooms">
        <div class="chat-left">
          <img class="bmk-image" src="../assets/image/bookmark2.png" alt="북마크이미지">
          <p class="review-title">{{chatroom.title}}</p>
        </div>
        <div class="chat-right">
          <p class="list_info">{{chatroom.book_title}}</p>
          <p class="list_info">{{chatroom.member_count}}/{{chatroom.max_member_count}}</p>
        </div>
      </div>
    </article>
    <article class="list-div">
      <div class="chat" v-for="chatroom in chatrooms">
        <div class="chat-left">
          <img class="bmk-image" src="../assets/image/bookmark2.png" alt="북마크이미지">
          <p class="review-title">{{chatroom.title}}</p>
        </div>
        <div class="chat-right">
          <p class="list_info">{{chatroom.book_title}}</p>
          <p class="list_info">{{chatroom.member_count}}/{{chatroom.max_member_count}}</p>
        </div>
      </div>
    </article>
  </div>

  <article>

  </article>


</template>

<style scoped>
.my-swiper-container {
  width: 1000px;
  height: 200px;
  margin: 20px auto;
}
.slide-image{
  width: 1000px;
  padding: 0;
}
#books-and-chatrooms{
  padding: 20px 20px;
  display: grid;
  width: 1000px;
  margin: 50px auto;
  grid-template-columns: 1fr 1fr;
}
.chat{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 10px;
  border-bottom: 1px solid #888888;
  font-weight: bold;
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
.bmk-image{
  margin-top: 10px;
}
.list_info{
  margin: 17px 10px;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  color: #666666;
  font-size: 14px;
}
.list-div{
  padding: 10px;
}
</style>