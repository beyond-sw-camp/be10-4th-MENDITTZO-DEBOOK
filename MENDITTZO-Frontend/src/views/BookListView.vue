<script setup>
import { reactive, onMounted } from "vue";
import axios from "axios";
import BookListComponents from "@/components/BookListComponents.vue";
import PagingBar from "@/components/PagingBar.vue";

const state = reactive({
  bookLists: [],
  currentPage: 1,
  totalPages: 1,
  totalItems: 0,
  pageSize: 10
});

const fetchBooks = async (page = 1) => {
  try {
    const response = await axios.get(`/booklists`, {
      params: {
        page
      }
    });
    state.bookLists = response.data.bookLists;
    state.currentPage = response.data.currentPage;
    state.totalPages = response.data.totalPages;
    state.totalItems = response.data.totalItems;
    state.pageSize = response.data.pageSize;
  } catch (error) {
    console.error('도서 목록을 불러오는 중 에러가 발생했습니다: ', error);
  }
};

onMounted(() => fetchBooks());

</script>

<template>
  <h1>도서 목록</h1>
  <div class="container">
    <BookListComponents :bookLists="state.bookLists" />
  </div>
  <PagingBar
    :currentPage="state.currentPage"
    :totalPages="state.totalPages"
    :totalItems="state.totalItems"
    :pageSize="state.totalPages"
    @page-changed="fetchBooks"
  />
</template>

<style scoped>
  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh; /* 전체 화면의 세로 중앙 정렬 */
    text-align: center;
  }

  h1 {
    margin-top: 5%;
    margin-bottom: 70px;
    margin-left: 10%;
  }
</style>