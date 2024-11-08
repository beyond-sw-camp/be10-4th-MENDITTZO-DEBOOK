<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/swiper-bundle.css'; // Swiper 스타일
import {Navigation, Pagination} from "swiper/modules";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import PagingBar from "@/components/common/PagingBar.vue";

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

const state = reactive({
  books: [],
  currentPage: 1,
  totalPages: 1,
  totalItems: 0,
  pageSize: 10
});

const bestBooks = ref([]);

const fetchBestBooks = async () => {
  const response = (await axios.get(`/booklists`, {
    params: {
      page: 1,
      size: '5'
    }
  })).data;
  bestBooks.value = response.bookLists;
}

const fetchBooks = async (page = 1) => {
  try {
    const response = await axios.get(`/booklists`, {
      params: {
        page,
        size: 10
      }
    });
    state.books = response.data.bookLists;
    state.currentPage = response.data.currentPage;
    state.totalPages = response.data.totalPages;
    state.totalItems = response.data.totalItems;
    state.pageSize = response.data.pageSize;
  } catch (error) {
    console.error('도서 목록을 불러오는 중 에러가 발생했습니다: ', error);
  }
};

onMounted(() => {
      fetchBestBooks();
      fetchBooks();
    }
)
</script>

<template>
  <section>
    <div class="my-swiper">
      <Swiper
          :modules="[Navigation, Pagination]"
          navigation
          pagination
          loop
          class="my-swiper-container"
      >
        <SwiperSlide v-for="(image, index) in images" :key="index">
          <img class="slide-image" :src="image" alt="Slide Image" />
        </SwiperSlide>
      </Swiper>
    </div>

    <div id="books-and-chatrooms">
      <article class="list-div">
        <h2>리뷰많은 책</h2>
        <div class="chat" v-for="book in bestBooks">
          <div class="list-left">
            <img class="bmk-image" src="../assets/image/bookmark.png" alt="북마크이미지">
            <p class="list-title">{{book.title}}</p>
          </div>
          <div class="list-right">
            <p class="list_info">{{book.author}}</p>
            <p class="list_info">{{book.publisher}}</p>
          </div>
        </div>
      </article>
      <article class="list-div">
        <h2>진행중인 독서 토론방</h2>
        <div class="chat" v-for="chatroom in chatrooms">
          <div class="list-left">
            <img class="bmk-image" src="../assets/image/bookmark2.png" alt="북마크이미지">
            <p class="list-title">{{chatroom.title}}</p>
          </div>
          <div class="list-right">
            <p class="list_info">{{chatroom.book_title}}</p>
            <p class="list_info">{{chatroom.member_count}}/{{chatroom.max_member_count}}</p>
          </div>
        </div>
      </article>
    </div>

    <h1>최근 도서</h1>
    <div id="books">
      <div class="book" v-for="book in state.books">
        <img class="book-img" :src="book.img" alt="책 이미지">
          <p class="book-title">{{book.title}}</p>
        <div class="book-info">
          <p class="book-author">{{book.author}} </p>
          <p class="book-author"> | {{book.publisher}}</p>
        </div>
      </div>
    </div>
    <PagingBar
        :currentPage="state.currentPage"
        :totalPages="state.totalPages"
        :totalItems="state.totalItems"
        :pageSize="state.totalPages"
        @page-changed="fetchBooks"
    />
  </section>

</template>

<style scoped>
section{
  width: 1000px;
  margin: 0 auto;
  padding: 0;
}
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
  display: grid;
  width: 940px;
  margin: 30px auto;
  grid-template-columns: 1fr 1fr;
}
.chat{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 5px;
  border-bottom: 1px solid #888888;
  font-weight: bold;
}
.list-left{
  display: grid;
  grid-template-columns: 50px auto;
  justify-content: start;
}
.list-right{
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
.list-title{
  margin-top: 15px;
  font-weight: bold;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  cursor: pointer;
  font-size: 15px;
}

#books{
  padding: 20px 20px;
  display: grid;
  width: 1000px;
  margin: 20px auto;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}
.book{
  padding: 10px;
  cursor: pointer;
}
.book-img{
  width: 150px;
  height: 225px;
  border-radius: 0 15px 15px 0;
  box-shadow: 10px 10px 20px rgba(0, 0, 0, 0.3); /* x-offset, y-offset, blur, color */
}

.book-info{
  display: flex;
}
.book-title{
  margin-top: 15px;
  font-weight: bold;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  font-size: 12px;
  width: 150px;
}
.book-author{
  margin-top: 5px;
  font-weight: bold;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  font-size: 10px;
  width: 50px;
  color: #888888;
}
</style>