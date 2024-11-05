<script setup>
import {reactive, ref} from "vue";

const user = reactive(
    {
      id:1,
      img:`https://cdn.pixabay.com/photo/2019/07/23/13/51/shepherd-dog-4357790_1280.jpg`
    }
);
const reviews = ref([
  {
    "review_id": 1,
    "book_id": 6352228,
    "user_id": 1,
    "create_datetime": "2024-11-01 10:00:00",
    "update_datetime": null,
    "delete_datetime": null,
    "title": "감동적, 추억의 목소리가 들리는 기분",
    "content": "90년대생에게 보석 같은 추억을 선물해준 성우 이용신의 이야기를 잘 담아낸 책입니다. 감동적이고 따뜻합니다.",
    "book_title": "너에게 목소리를 보낼게",
    "author": "이용신",
    "rating": 5,
    "status": "active"
  },
  {
    "review_id": 2,
    "book_id": 6352229,
    "user_id": 2,
    "create_datetime": "2024-11-01 10:30:00",
    "update_datetime": null,
    "delete_datetime": null,
    "title": "솔직한 에세이에 그렇지 못한 제목",
    "book_title": "일기에도 거짓말을 쓰는 사람",
    "content": "자의식 넘치는 차도하 시인의 첫 에세이, 솔직하고 도발적인 내용이 흥미로웠습니다.",
    "author": "차도하",
    "rating": 4,
    "status": "active"
  },
  {
    "review_id": 3,
    "book_id": 6352230,
    "user_id": 3,
    "create_datetime": "2024-11-01 11:00:00",
    "update_datetime": null,
    "delete_datetime": null,
    "title": "역사적 통찰이 뛰어난 책",
    "book_title": "너에게 목소리를 보낼게",
    "content": "임오군란과 통킹 위기를 통해 한중일의 관계를 재조명한 흥미로운 역사서입니다.",
    "author": "한국역사관",
    "rating": 3,
    "status": "active"
  }
]);
</script>

<template>
  <section>
    <p class="menu">나의 프로필</p>
    <hr class="cross">
    <article id="top-div">
      <div>
        <p class="middle-text">닉네임</p>
        <div id="nick-bar">
          <input id="nick-input" type="text" placeholder="홍길동">
          <img id="nick-icon" src="https://img.icons8.com/?size=100&id=4299&format=png&color=000000" alt="검색아이콘">
        </div>
        <button id="modify-button">수정하기</button>
      </div>

      <div id="profile-right">
        <p class="middle-text">프로필사진</p>
        <div class="profile-image-container">
          <img id="profile-img" :src="user.img" alt="프로필 사진">
        </div>
      </div>
    </article>

    <p class="menu">나의 리뷰</p>
    <hr class="cross">
    <article id="middle-div">
      <div class="review" v-for="review in reviews">
        <div class="review-left">
          <img src="../assets/image/bookmark.png" alt="북마크이미지">
          <p class="review-title">{{review.title}}</p>
        </div>
        <div class="review-right">
          <div class="rating">
            <img src="../assets/image/star.png" alt="별" v-for="n in review.rating">
            <img src="../assets/image/empty-star.png" alt="빈별" v-for="n in 5-review.rating">
          </div>
          <p class="review_info">{{review.book_title}}</p>
          <p class="review_info">{{review.author}}</p>
          <div>
            <img class="review-icons" src="../assets/image/modify.png" alt="수정 아이콘">
            <img class="review-icons" src="../assets/image/delete.png" alt="삭제 아이콘">
          </div>
        </div>
      </div>
        <button id="modify-button">전체보기</button>

    </article>

  </section>
</template>

<style scoped>
section {
  width: 1000px;
  margin: 0 auto;
  padding: 50px;
}
#profile-right{
  margin-left: 100px;
}
.menu {
  font-size: 30px;
  font-weight: bold;
  color: #444444;
  padding-left: 20px;
}
.middle-text {
  font-size: 20px;
  font-weight: bold;
  color: #444444;
  padding-left: 10px;
}
.cross {
  border: none;
  height: 1px;
  background-color: #888888;
}
#top-div{
  display: flex;
  padding: 50px;
}
#middle-div{
  padding: 50px
}
#nick-icon{
  width: 20px;
  height: 20px;
  margin-top: 20px;
  margin-right: 15px;
  cursor: pointer;
}
#profile-img{
  width: 100%;
  height: 100%;
  object-fit: cover;
}
#nick-bar{
  display: flex;
  justify-content: center;
  border: 2px solid #78AE6B;
  border-radius: 15px;
  padding: 0 10px 0 10px;
  margin-bottom: 50px;
}
#nick-input{
  width: 500px;
  height: 60px;
  border: none;
  margin: 0 10px 0 10px;
}
#nick-input:focus{
  box-shadow: none;
  outline: none;
}
.profile-image-container {
  width: 200px;
  height: 200px;
  border-radius: 50%; /* 원형으로 만들기 */
  overflow: hidden; /* 넘치는 부분 숨기기 */
  display: flex;
  align-items: center;
  justify-content: center;
}
#modify-button {
  margin: 30px 10px;
  width: 100px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 15px;
}

.review{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 10px;
  border-bottom: 1px solid #888888;
}
.review-left{
  display: grid;
  grid-template-columns: 50px auto;
  justify-content: start;
}
.review-right{
  display: grid;
  grid-template-columns: 120px auto auto 60px;
  justify-content: end; /* 왼쪽 정렬 */
}
.review-title{
  margin: 5px 0;
  font-weight: bold;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  cursor: pointer;
}
.rating{
  margin-top: 5px;
}
.review_info{
  margin: 5px 10px;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
}
.review-icons{
  margin-top: 5px;
  margin-right: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>