<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const showButton = ref(false);

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function handleScroll() {
  showButton.value = window.scrollY > 100; // 스크롤이 100px 이상일 때만 버튼 표시
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <div v-if="showButton" @click="scrollToTop" class="top-button">
    <img src="../../assets/image/top.png" alt="Top" />
  </div>
</template>

<style scoped>
.top-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  cursor: pointer;
  z-index: 1000;
  margin-right: 30px;
  margin-bottom: 30px;
}

.top-button img {
  width: 80px;
  height: 80px;
}
</style>
