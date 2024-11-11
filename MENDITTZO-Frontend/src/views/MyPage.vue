<script setup>
import {onMounted, reactive, ref} from "vue";
import {useAuthStore} from "@/store/auth.js";
import PagingBar from "@/components/common/PagingBar.vue";
import instance from "@/config/axios.js";
import router from "@/router/router.js";
import SignOutForm from "@/components/SignOutForm.vue";

const authStore = useAuthStore();

const state = reactive({
  reviews: [],
  currentPage: 1,
  totalPages: 1,
  totalItems: 0,
  pageSize: 10
});

const userImg = ref();
const userNick = ref();
const userImgUrl = ref();
const chatrooms = ref([]);
const isReadonly = ref(true);
const showSignOutForm = ref(false);

const fetchReviews = async (page = 1) => {
  try {
    const response = await instance.get(`/reviews/user`, {
      params: {
        page,
        size: 10
      },
    });
    state.reviews = response.data.reviewList;
    state.currentPage = response.data.currentPage;
    state.totalPages = response.data.totalPages;
    state.totalItems = response.data.totalItems;
    state.pageSize = response.data.pageSize;
  } catch (error) {
    console.error('도서 목록을 불러오는 중 에러가 발생했습니다: ', error);
  }
};

// 이미지 미리보기 기능
const previewImage = (event) => {

  const file = event.target.files[0];
  userImg.value = file;

  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader();

    // 파일이 로드되면 미리보기 데이터 URL을 저장
    reader.onload = (e) => {
      userImgUrl.value = e.target.result;
    };

    reader.readAsDataURL(file); // 이미지 파일을 데이터 URL로 변환
  } else {
    alert("이미지 파일을 선택해 주세요.");
  }
}

const triggerFileInput = () => {
  const confirmed = window.confirm('프로필 사진을 수정하시겠습니까?');
  if (confirmed) {
    document.getElementById('img-input').click();
  }

}

const enableEdit = () => {

  if(isReadonly.value){
    const confirmed = window.confirm('닉네임을 수정하시겠습니까?');
    if (confirmed) {
      isReadonly.value = !isReadonly.value;
    }
  }else{
    isReadonly.value = !isReadonly.value;
  }
}
const modifyUser = async () => {
  const confirmed = window.confirm('수정된 프로필을 저장하시겠습니까?');
  if (confirmed) {

    try {
      // FormData 생성 및 데이터 추가
      const formData = new FormData();
      formData.append("profileImage", userImg.value); // 이미지 파일 추가
      formData.append("nickname", userNick.value); // 닉네임 추가

      await instance.put("/users", formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }

      });
      await authStore.fetchUserInfo();

      alert("수정에 성공했습니다.");
    }catch (error){
      alert("유저 정보 수정중 오류가 발생했습니다.");
    }

  }
}

const handleSignOut = async () => {
  try {
    await instance.delete("/users");

    // Pinia 스토어, 로컬 스토리지에서 토큰 삭제
    authStore.logout();
    router.push("/login");
  } catch (error) {
    console.error("탈퇴 실패: ", error);
  } finally {
    showSignOutForm.value = false;
  }
};

onMounted(

    () => {

      // 로그인 하지 않은 경우 로그인 페이지로 이동
      if (!authStore.accessToken) {
        router.push("/login");
      }

      fetchReviews();
      userNick.value = authStore.nickname;
      userImgUrl.value = authStore.profileImg;
    }
)
</script>

<template>
  <section>
    <p class="menu">나의 프로필</p>
    <hr class="cross">
    <article id="top-div">
      <div>
        <p class="middle-text">닉네임</p>
        <div id="nick-bar">
          <input id="nick-input" v-model="userNick" type="text" :placeholder=authStore.nickname :readonly="isReadonly">
          <img id="nick-icon" @click="enableEdit" src="https://img.icons8.com/?size=100&id=4299&format=png&color=000000" alt="수정아이콘">
        </div>
        <button class="myButton" @click="modifyUser">저장하기</button>
      </div>

      <div id="profile-right">
        <p class="middle-text">프로필사진</p>
        <div class="profile-image-container">
          <img id="profile-img" :src="userImgUrl" alt="프로필 사진" @click="triggerFileInput">
        </div>
        <!-- 숨겨진 파일 입력 필드 -->
        <input type="file" @change="previewImage" id="img-input" style="display: none;" />
      </div>
    </article>

    <p class="menu">나의 리뷰</p>
    <hr class="cross">
    <article class="list-div">
      <div class="review" v-for="review in state.reviews">
        <div class="review-left">
          <img src="../assets/image/bookmark.png" alt="북마크이미지">
          <p class="review-title">{{review.title}}</p>
        </div>
        <div class="review-right">
          <div class="rating">
            <img src="../assets/image/filled-star.png" alt="별" v-for="n in review.rating">
            <img src="../assets/image/empty-star.png" alt="빈별" v-for="n in 5-review.rating">
          </div>
          <p class="list_info">{{review.book_title}}</p>
          <p class="list_info">{{review.author}}</p>
          <div>
            <img class="review-icons" src="../assets/image/modify.png" alt="수정 아이콘">
            <img class="review-icons" src="../assets/image/delete.png" alt="삭제 아이콘">
          </div>
        </div>
      </div>
      <button class="myButton">전체보기</button>
    </article>
    <PagingBar
        :currentPage="state.currentPage"
        :totalPages="state.totalPages"
        :totalItems="state.totalItems"
        :pageSize="state.totalPages"
        @page-changed="fetchReviews"
    />

    <p class="menu">참가중인 독서 토론방</p>
    <hr class="cross">
    <article class="list-div">
      <div class="chat" v-for="chatroom in chatrooms">
        <div class="chat-left">
          <img src="../assets/image/bookmark2.png" alt="북마크이미지">
          <p class="review-title">{{chatroom.title}}</p>
        </div>
        <div class="chat-right">
          <p class="list_info">{{chatroom.book_title}}</p>
          <p class="list_info">{{chatroom.member_count}}/{{chatroom.max_member_count}}</p>
        </div>
      </div>
      <button class="myButton">전체보기</button>
    </article>
    <p class="menu">회원관리</p>
    <hr class="cross">

    <div v-if="showSignOutForm" class="modal-overlay">
      <SignOutForm @confirm="handleSignOut" @cancel="showSignOutForm = false"/>
    </div>
    <button class="myButton" id="delete-button" @click="showSignOutForm = true">탈퇴하기</button>

  </section>
</template>

<style scoped>
section {
  width: 1000px;
  margin: 0 auto 100px auto;
  padding: 30px 50px;
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
  padding: 30px 50px;
}
.list-div{
  padding: 50px
}

.modal-overlay{
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명한 검정색 */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000; /* 다른 요소보다 위에 표시 */
}

.signout-form {
  background-color: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 100%;
  text-align: center;
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
  cursor: pointer;
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
.myButton {
  margin: 30px 10px;
  width: 100px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 15px;
  float: right;
}
#delete-button{
  background-color: #F24822;
  margin-right: 60px;
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
  grid-template-columns: 120px 150px 80px 60px;
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
.list_info{
  margin: 7px 10px;
  white-space: nowrap;    /* 텍스트를 한 줄로 유지 */
  overflow: hidden;       /* 넘치는 텍스트를 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 '...'으로 표시 */
  color: #666666;
  font-size: 13px;
}
.review-icons{
  margin-top: 5px;
  margin-right: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.chat{
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 5px 10px;
  border-bottom: 1px solid #888888;
}
.chat-left{
  display: grid;
  grid-template-columns: 50px auto;
  justify-content: start;
}
.chat-right{
  display: grid;
  grid-template-columns: auto 60px;
  justify-content: end; /* 왼쪽 정렬 */
}
#image-icon{
  position: absolute;
  width: 25px;
  height: 25px;
  right: 360px;
  bottom: 5px;
  cursor: pointer;
}
</style>