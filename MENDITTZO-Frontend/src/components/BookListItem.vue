<script setup>
import { useRouter } from "vue-router";

const props = defineProps({
  book: {
    type: Object,
    required: true
  }
});

const router = useRouter();

const goToBookDetail = (id) => {
  router.push(`/booklists/${id}`);
}
</script>

<template>
  <div class="book-item" >
    <img :src="book.img" alt="책 이미지" @click="goToBookDetail(book.bookId)"/>
    <div class="title" @click="goToBookDetail(book.bookId)">{{ book.title }}</div>
    <div class="book-info">{{ book.author }} | {{ book.publisher }}</div>
  </div>
</template>

<style scoped>
.book-item {
  width: 16%; /* 한 줄에 5개가 들어가도록 18% 설정 */
  margin-bottom: 40px;
  margin-right: 30px;
  text-align: left;
}

.book-item img:hover {
  cursor: pointer;
}

.book-item img {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
}
.book-item .title {
  font-size: 14px;
  color: #444444;
  font-weight: bold;
  margin-bottom: 7px;
  text-decoration: none;
}

.book-item .title:hover {
  text-decoration: underline;
  text-decoration-color: #444444;
  cursor: pointer;
}

.book-item .book-info {
  font-size: 10px;
  color: #888888;
}
</style>
