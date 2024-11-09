<script setup>
import { computed, ref } from 'vue';
import ConfirmModal from "@/components/common/ConfirmModal.vue";
import axios from 'axios';
import { useRouter } from 'vue-router';
import openIcon from '@/assets/image/review-open.png';
import closeIcon from '@/assets/image/review-close.png';

const props = defineProps({
  review: {
    type: Object,
    required: true
  }
});

const isExpanded = ref(false);
const showDeleteModal = ref(false);
const showReportModal = ref(false);
const router = useRouter();

// 리뷰 작성자인지 여부 확인
const isAuthor = computed(() => props.review.isWriter);

const toggleContent = () => {
  isExpanded.value = !isExpanded.value;
};

const openDeleteModal = () => {
  showDeleteModal.value = true;
};

const closeDeleteModal = () => {
  showDeleteModal.value = false;
};

const deleteReview = async () => {
  closeDeleteModal();
  try {
    await axios.delete(`http://localhost:8080/booklists/${bookId}/reviews/${reviewId}`);
    alert('리뷰가 성공적으로 삭제되었습니다.');
    // 페이지 새로고침이나 리스트 갱신 로직 추가 필요
  } catch (error) {
    console.error('리뷰 삭제 중 오류가 발생했습니다: ', error);
    alert('리뷰 삭제에 실패했습니다.');
  }
};

const openReportModal = () => {
  showReportModal.value = true;
};

const closeReportModal = () => {
  showReportModal.value = false;
};

const reportReview = () => {
  closeReportModal();
  alert('리뷰가 신고되었습니다.');
};

const openEditPage = () => {
  router.push(`/booklists/${props.reviewList.bookId}/reviews/${props.reviewList.reviewId}/edit`);
};
</script>

<template>
  <div v-if="review" class="review-item">
    <div class="review-header">
      <h1 class="review-title">{{ review.title }}</h1>
      <div class="meta">
        <span class="user-info">{{ review.nickname }} | {{ review.createDatetime }}</span>
        <div class="actions">
          <!-- 리뷰 작성자일 때만 수정, 삭제 버튼 표시 -->
          <template v-if="isAuthor">
            <button @click="openEditPage">수정</button> <!-- 수정 버튼 -->
            <button @click="openDeleteModal">삭제</button> <!-- 삭제 버튼 -->
            <!-- 삭제 모달 -->
            <ConfirmModal
                :visible="showDeleteModal"
                :message="'정말로 이 리뷰를 삭제하시겠습니까?'"
                :confirmButtonText="'삭제'"
                :cancelButtonText="'취소'"
                @confirm="deleteReview"
                @cancel="closeDeleteModal"
            />
          </template>
          <!-- 작성자가 아닐 때는 신고 버튼 표시 -->
          <template v-else>
            <button @click="openReportModal">신고</button>
            <!-- 신고 모달 -->
            <ConfirmModal
                :visible="showReportModal"
                :message="'정말로 이 리뷰를 신고하시겠습니까?'"
                :confirmButtonText="'신고'"
                :cancelButtonText="'취소'"
                @confirm="reportReview"
                @cancel="closeReportModal"
            />
          </template>
        </div>
      </div>
      <div class="rating">
        <!-- 평균 평점을 별 이미지로 렌더링 -->
        <span v-for="star in Math.floor(review.rating)" :key="'filled' + star" class="star">
          <img src="../assets/image/filled-star.png" alt="채워진 별" />
        </span>
        <span v-for="star in (5 - Math.floor(review.rating))" :key="'empty' + star" class="star">
          <img src="../assets/image/empty-star.png" alt="빈 별" />
        </span>
      </div>
    </div>
    <div class="review-content">
      <p class="content">
        {{ isExpanded ? review.content : review.content.slice(0, 50) + '...' }}
      </p>
      <button @click="toggleContent" class="toggle-btn">
        {{ isExpanded ? '접기' : '펼치기' }}
        <img :src="isExpanded ? closeIcon : openIcon" alt="펼치기/접기 아이콘" class="toggle-icon" />
      </button>
    </div>
  </div>
  <div v-else>
    <p>리뷰 정보를 불러오는 중입니다...</p>
  </div>
</template>

<style scoped>
.review-item {
  padding: 20px;
  margin-left: 20px;
  margin-right: 20px;
  border-bottom: 1px solid #BDBDBD;
  color: #444444;
}

.review-header {
  display: flex;
  align-items: center;
  width: 100%;
}

.review-title {
  font-size: 1.5rem;
  margin: 0;
  flex-grow: 1;
}

.meta {
  display: flex;
  align-items: center;
  gap: 10px; /* 제목, 닉네임, 작성일자, 버튼 간 간격 */
}

.user-info {
  font-size: 0.9rem;
  color: #888;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions button {
  margin-right: 8px;
  padding: 5px 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  color: #444444;
}

.rating {
  display: flex;
  align-items: center;
  margin-left: 20px; /* 닉네임과 별 이미지 간격을 띄우기 위한 마진 */
}

.rating img {
  width: 20px;
  height: 20px;
}

.content {
  margin-top: 10px;
}

.toggle-btn {
  margin-top: 5px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  color: #444444;
}

.toggle-icon {
  width: 16px;
  height: 16px;
  margin-left: 5px;
}
</style>
