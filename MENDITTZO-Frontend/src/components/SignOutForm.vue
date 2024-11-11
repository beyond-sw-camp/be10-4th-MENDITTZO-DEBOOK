<script setup>
import ButtonLong from "@/components/common/ButtonLong.vue";
import {defineEmits} from 'vue';

const emit = defineEmits(['confirm', 'cancel']);

// 탈퇴 취소 버튼 클릭 시
const cancelSignOut = () => {
  emit('cancel');
};

// 탈퇴 확인 버튼 클릭 시
const confirmSignOut = () => {
  emit('confirm');
};
</script>

<template>
  <div class="signout-form">
    <img src="@/assets/image/emergency.png">
    <p class="confirm-message">정말 탈퇴하시겠습니까?</p>
    <div class="button-group">
      <button class="confirm-button" @click="confirmSignOut">탈퇴</button>
      <button class="cancel-button" @click="cancelSignOut">취소</button>
    </div>
  </div>
</template>

<style scoped>
.signout-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.confirm-message {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  margin-top: 10px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 100px;
}
.confirm-button,
.cancel-button {
  margin-left: 10px;
  width: 80px;
  height: 40px;
  color: white;
  background-color: #78AE6B;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold
}

.cancel-button {
  background-color: white;
  color: #78AE6B;
  border: 2px solid #78AE6B;
}

.signout-form img {
  width: 50px;
  height: auto; /* 비율 유지 */
}

</style>