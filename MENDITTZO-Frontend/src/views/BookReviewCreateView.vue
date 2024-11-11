<script setup>
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import ReviewForm from "@/components/ReviewForm.vue";
import { ref, onMounted } from "vue";

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

// 페이지가 로드될 때 도서 상세 정보를 가져옴
onMounted(() => {
  fetchBookDetail();
});

const handleReviewCreate = async (formData) => {
  try {
    await axios.post(`http://localhost:8080/api/v1/${bookId}/reviews`, formData)

    router.push(`/booklists/${bookId}`)
  } catch (error) {
    console.error("리뷰 등록 중 오류 발생 : ", error);
  }
};

</script>

<template>
  <div>
    <ReviewForm @submit="handleReviewCreate" :bookResponse="bookResponse" />
  </div>
</template>

<style scoped>

</style>