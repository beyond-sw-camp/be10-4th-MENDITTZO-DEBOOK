<script setup>
import { ref, computed, watch, reactive } from 'vue';
import ConfirmModal from "@/components/common/ConfirmModal.vue";
import {useRoute, useRouter} from 'vue-router';
import BigButton from "@/components/common/BigButton.vue";
import filled from "@/assets/image/filled-star.png";
import empty from "@/assets/image/empty-star.png";

// 부모 컴포넌트로부터 초기 데이터 받기
const props = defineProps({
  initialData: {
    type: Object
  },
  bookResponse: {
    type: Object,
    required: true
  }
});

// 폼 데이터
const formData = reactive({
  title: "",
  content: "",
  rating: 0,
});

// 폼 유효성 체크
const isFormValid = computed(() => {
  return formData.title &&
      formData.content &&
      formData.rating > 0;
});

// 취소 모달 상태
const showCancelModal = ref(false);

// 초기 데이터가 변경될 때 폼 데이터 업데이트
watch(() => props.initialData, (newData) => {
  if (newData) {
    formData.title = newData.title || '';
    formData.content = newData.content || '';
    formData.rating = newData.rating || 0;
  }
}, { immediate: true });

const router = useRouter();
const route = useRoute();

const bookId = route.params.id;

// 모달에서 확인 클릭 시 도서 상세 페이지로 이동
const navigateToBookDetail = () => {
  router.push(`/booklists/${bookId}`);
};

const emit = defineEmits(['submit']);

// 폼 제출 함수
const submitForm = () => {
  const formDataToSend = {
    title: formData.title,
    content: formData.content,
    rating: formData.rating,
  };

  // 상위 컴포넌트로 데이터 전송
  emit('submit', formDataToSend);
};

// 취소 버튼을 클릭하면 모달이 열리도록 설정
const openCancelModal = () => {
  showCancelModal.value = true;
};

// 모달에서 취소 클릭 시 모달 닫기
const closeCancelModal = () => {
  showCancelModal.value = false;
};

// 별점 설정 함수
const setRating = (selectedRating) => {
  formData.rating = selectedRating;
};
</script>

<template>
  <div class="review-form">
    <!-- 도서 정보 및 별점 선택 영역 -->
    <div v-if="bookResponse" class="book-info">
      <img :src="bookResponse.img" alt="Book Cover" class="book-cover" />
      <div class="book-details">
        <h3 class="book-title">{{ bookResponse.title }}</h3>
        <p class="book-author">{{ bookResponse.author }}</p>
        <p class="book-publisher">{{ bookResponse.publisher }} | {{ bookResponse.pubdate }}</p>

        <!-- 별점 선택 -->
        <div class="star-rating">
          <img
              v-for="star in 5"
              :key="star"
              :src="star <= formData.rating ? filled : empty"
              @click="setRating(star)"
              class="star"
              alt="star"
          />
        </div>
      </div>
    </div>

    <div v-else>
      Loading...
    </div>

    <!-- 리뷰 제목 및 내용 입력 -->
    <div class="review-input">
      <input type="text" placeholder="제목을 입력해주세요" v-model="formData.title" class="title-input" required />
      <hr />
      <textarea placeholder="내용을 입력해주세요" v-model="formData.content" required></textarea>
    </div>

    <!-- 제출 및 취소 버튼 -->
    <div class="form-actions">
      <BigButton @click="openCancelModal">취소</BigButton>
      <BigButton @click="submitForm" :disabled="!isFormValid">제출</BigButton>
    </div>

    <!-- 취소 모달 -->
    <ConfirmModal
        :visible="showCancelModal"
        :message="'정말로 취소하시겠습니까?'"
        :confirmButtonText="'확인'"
        :cancelButtonText="'취소'"
        @confirm="navigateToBookDetail"
        @cancel="closeCancelModal"
    />
  </div>
</template>

<style scoped>
.review-form {
  padding: 20px;
  max-width: 800px;
  height: auto;
  margin: 20px auto;
  background-color: white;
}

.book-info {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.book-cover {
  width: 170px;
  height: auto;
  object-fit: cover;
  border-radius: 4px 10px 10px 4px
}

.book-details {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
}

.book-title {
  font-size: 24px;
  font-weight: bold;
  color: #444444;
}

.book-author, .book-publisher {
  font-size: 15px;
  color: #444444;
  margin-top: 10px;
}

.star-rating {
  display: flex;
  margin-top: 15px;
}

.star {
  width: 36px;
  height: 36px;
  cursor: pointer;
}

.review-input {
  margin-top: 30px;
  padding: 10px;
  border: 1px solid #78AE6B;
  border-radius: 8px;
  height: 500px;
}

.title-input {
  width: 700px;
  padding: 10px;
  font-size: 25px;
  font-weight: bold;
  border: none;
  outline: none;
  color: #444444;
  margin-top: 5px;
}

.title-input::placeholder {
  color: #BDBDBD;
  font-size: 25px;
  margin-top: 5px;
}

textarea {
  width: 750px;
  height: 400px;
  padding: 10px;
  font-size: 18px;
  border: none;
  resize: none;
  outline: none;
  margin-top: 8px;
  color: #444444;
}

textarea::placeholder {
  color: #BDBDBD;
  font-size: 18px;
}

hr {
  border: none;
  border-top: 2px solid #888888;
  margin: 10px 0;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

BigButton:hover {
  background-color: #45a049;
}

BigButton:disabled {
  background-color: #BDBDBD;
  cursor: not-allowed;
}
</style>
