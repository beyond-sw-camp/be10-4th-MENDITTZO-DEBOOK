<script setup>
import ReviewItem from "@/components/ReviewItem.vue";

const props = defineProps({
  reviewList: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="container">
    <ReviewItem v-for="review in reviewList" :key="review.reviewId" :review="review" />
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  padding-right: 20px;
  padding-left: 20px;
}
</style>