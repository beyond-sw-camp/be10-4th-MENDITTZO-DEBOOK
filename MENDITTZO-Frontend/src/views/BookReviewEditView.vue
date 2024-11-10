<script setup>
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import ReviewForm from "@/components/ReviewForm.vue";
import { onMounted, ref } from "vue";

const reviewData = ref(null);
const router = useRouter();
const route = useRoute();
const bookResponse = ref(null);

const bookId = route.params.id;

// 도서 상세 정보를 가져오는 함수
const fetchBookDetail = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/v1/booklists/${bookId}`);
    bookResponse.value = response.data.bookResponse;
  } catch (error) {
    console.error('도서 상세 정보를 불러오는 중 에러가 발생했습니다. : ', error);
  }
};

const fetchReviewData = async (reviewId) => {

  try {
    const response = await axios.get(`http://localhost:8080/api/v1/api/v1/reviews/${reviewId}`);
    reviewData.value = response.data.review;
  } catch (error) {
    console.error("리뷰 정보를 가져오는 중 오류 발생 : ", error);
  }
};

const handleReviewEdit = async (formData) => {
  try {
    await axios.put(`http://localhost:8080/api/v1/${bookId}/reviews/${reviewId}`, formData)

    router.push(`/booklists/${bookId}`)
  } catch (error) {
    console.error("리뷰 수정 중 오류 발생 : ", error);
  }
};

onMounted(() => {
  const reviewId = router.currentRoute.value.params.id;
  fetchReviewData(reviewId);
  fetchBookDetail();
});
</script>

<template>
  <div>
    <ReviewForm
        @submit="handleReviewEdit"
        :bookResponse="bookResponse"
        :initialData="reviewData"
    />
  </div>
</template>

<style scoped>

</style>