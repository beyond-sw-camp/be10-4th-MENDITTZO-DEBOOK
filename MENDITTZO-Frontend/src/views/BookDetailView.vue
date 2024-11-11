<script setup>
import BookDetailComponent from "@/components/BookDetailComponent.vue";
import { onMounted, reactive, ref} from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import ReviewListComponent from "@/components/ReviewListComponent.vue";
import PagingBar from "@/components/common/PagingBar.vue";
import SmallButton from "@/components/common/SmallButton.vue";
import DropDownMenu from "@/components/common/DropDownMenu.vue";

const bookResponse = ref(null);
const route = useRoute();
const router = useRouter();
const selectedOption = ref("최신순");

const bookId = route.params.id;

const state = reactive({
  reviewList: [],
  currentPage: 1,
  totalPages: 1,
  totalItems: 0,
  pageSize: 5
});

const fetchBookDetail = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/v1/booklists/${bookId}`);
    bookResponse.value = response.data.bookResponse;
  } catch (error) {
    console.error('도서 상세 정보를 불러오는 중 에러가 발생했습니다. : ', error);
  }
};

const fetchReview = async (page = 1) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/v1/reviews/${bookId}`, {
      params: {
        page
      }
    });
    state.reviewList = response.data.reviewList;
    state.currentPage = response.data.currentPage;
    state.totalPages = response.data.totalPages;
    state.totalItems = response.data.totalItems;
    state.pageSize = response.data.pageSize;

  } catch (error) {
    console.error('리뷰 목록을 불러오는 중 에러가 발생했습니다. : ', error);
  }
};

const goToReviewCreatPage = () => {
  router.push(`/booklists/${bookId}/review/create`);
}

function handleSelect(option) {
  selectedOption.value = option;
  if (option === "최신순") {
    state.reviewList.sort((a, b) => new Date(b.createDatetime) - new Date(a.createDatetime));
  } else if (option === "별점순") {
    state.reviewList.sort((a, b) => b.rating - a.rating);
  }
}

onMounted(() => {
    fetchBookDetail();
    fetchReview()
});
</script>

<template>
  <div class="container">
    <div class="book-detail">
      <BookDetailComponent :bookResponse="bookResponse" />
    </div>
    <div class="review-list">
      <div class="review">
        <h1>리뷰 <span>({{ state.totalItems }})</span></h1>
        <div class="review-else">
          <DropDownMenu
            :options="['최신순', '별점순']"
            @select="handleSelect"
          />
          <SmallButton @click="goToReviewCreatPage">리뷰작성</SmallButton>
        </div>
      </div>
      <ReviewListComponent :reviewList="state.reviewList" />
      <PagingBar
        :currentPage="state.currentPage"
        :totalPages="state.totalPages"
        :totalItems="state.totalItems"
        :pageSize="state.pageSize"
        @page-changed="fetchReview"
      />
    </div>
  </div>
</template>

<style scoped>
  .book-detail {
    border-bottom: 1px solid #BDBDBD;
  }

  .review-list {
    border-bottom: 1px solid #BDBDBD;
  }
  .review {
    margin-left: 10px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .review h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 10px;
    color: #444444;
    margin-left: 30px;
  }

  .review span {
    font-size: 23px;
  }

  .review-else {
    display: flex;
    width: 220px;
    height: 56px;
    justify-content: space-between;
    margin-right: 40px;
    margin-top: 30px;
  }
</style>