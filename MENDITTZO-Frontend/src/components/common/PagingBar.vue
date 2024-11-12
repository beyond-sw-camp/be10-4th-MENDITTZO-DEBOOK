<script setup>
import { computed } from "vue";

const props = defineProps({
  totalPages: {
    type: Number,
    required: true
  },
  totalItems: {
    type: Number,
    required: true
  },
  currentPage: {
    type: Number,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(["page-changed"]);

const changePage = (page) => {
  if (page >= 1 && page <= props.totalPages) {
    emit("page-changed", page);
  }
};

const visiblePages = computed(() => {
  const pages = [];
  const maxVisiblePages = 5; // 고정할 페이지 개수
  const half = Math.floor(maxVisiblePages / 2); // 현재 페이지 기준 양 옆 페이지 수

  let start = Math.max(1, props.currentPage - half);
  let end = Math.min(props.totalPages, props.currentPage + half);

  // 현재 페이지가 처음이나 끝에 가까울 때 페이지 개수를 고정된 숫자로 유지
  if (props.currentPage <= half) {
    end = Math.min(props.totalPages, maxVisiblePages);
  } else if (props.currentPage + half >= props.totalPages) {
    start = Math.max(1, props.totalPages - maxVisiblePages + 1);
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return pages;
});
</script>

<template>
  <div class="paging-bar">
    <!-- 첫 페이지로 이동 -->
    <button :disabled="currentPage === 1" @click="changePage(1)">
      <img src="../../assets/image/first-page.png" alt="first page">
    </button>

    <!-- 이전 페이지로 이동 -->
    <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)">
      <img src="../../assets/image/previous-page.png" alt="previous">
    </button>

    <!-- 페이지 번호 -->
    <span v-for="page in visiblePages" :key="page">
      <button
          :class="{ active: page === currentPage }"
          @click="changePage(page)">
        {{ page }}
      </button>
    </span>

    <!-- 다음 페이지로 이동 -->
    <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">
      <img src="../../assets/image/next-page.png" alt="next">
    </button>

    <!-- 마지막 페이지로 이동 -->
    <button :disabled="currentPage === totalPages" @click="changePage(totalPages)">
      <img src="../../assets/image/last-page.png" alt="last page">
    </button>
  </div>
</template>

<style scoped>
.paging-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px;
}

.paging-bar span button {
  background-color: transparent;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  font-size: 16px;
  color: #444444;
  gap: 10px;
}

.paging-bar span button.active {
  background-color: #78AE6B;
  color: white;
  border: none;
}

/* 이미지 크기 조절 */
.paging-bar img {
  width: 48px;
  height: 48px;
}

.paging-bar button {
  background: none;
  border: none;
  cursor: pointer;
}

.paging-bar button:disabled {
  opacity: 0.3;
  cursor: default;
}
</style>
