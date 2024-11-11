<script setup>
const props = defineProps({
  bookResponse: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <div v-if="bookResponse" class="book-detail">
    <h1>{{ bookResponse.title }}</h1>
    <div class="img-review">
      <img :src="bookResponse.img" alt="책 이미지" class="img" />
      <div class="book-info">
        <p>{{ bookResponse.author }}</p>
        <p>{{ bookResponse.publisher }} | {{ bookResponse.pubdate }}</p>
        <div class="review">
          <!-- 평균 평점을 별 이미지로 렌더링 -->
          <div class="star-rating">
            <!-- 색이 채워진 별 -->
            <span v-for="star in Math.floor(bookResponse.avgRating)" :key="'filled' + star" class="star">
              <img src="../assets/image/filled-star.png" alt="채워진 별" />
            </span>
            <!-- 테두리만 있는 빈 별 (5 - Math.floor(avgRating) 만큼) -->
            <span v-for="star in (5 - Math.floor(bookResponse.avgRating))" :key="'empty' + star" class="star">
              <img src="../assets/image/empty-star.png" alt="빈 별" />
            </span>
          </div>
          <p>{{ bookResponse.avgRatingNumber }}</p>
          <span>({{ bookResponse.reviewCount }})</span>
        </div>
      </div>
    </div>
    <div class="book-introduce">
      <h1>소개</h1>
      <p>{{ bookResponse.info }}</p>
    </div>
  </div>
  <div v-else>
    Loading...
  </div>
</template>

<style scoped>
.book-detail {
  align-items: center;
  gap: 20px;
  padding: 30px;
}
.img-review {
  display: flex;
  gap: 50px;
  margin-top: 40px;
  margin-left: 50px;
  margin-bottom: 30px
}
.img {
  width: 200px;
  height: auto;
  border-radius: 0 10px 10px 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.book-info {
  display: flex;
  flex-direction: column;
  gap: 15px;
  font-size: 20px;
  margin-top: 35px;
  color: #444444;
  font-weight: bold;
}
.review {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-top: 5px;
}
.star-rating {
  display: flex;
}
.star img {
  width: 36px;
  height: 36px;
}
.book-introduce {
  margin-top: 10px;
  padding: 10px;
  font-size: 16px;
}
.book-introduce h1 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #444444;
}
.book-detail h1 {
  font-size: 36px;
  font-weight: bold;
  margin-top: 10px;
  color: #444444;
}
.review p {
  margin: 0;
  font-size: 24px;
  color: #444444;
}
.review span {
  margin: 0;
  font-size: 18px;
  color: #444444;
}
</style>