<script setup>
import { defineProps, defineEmits } from 'vue';

// Props 정의
const props = defineProps({
  message: {
    type: String,
    default: '정말로 삭제하시겠습니까?'
  },
  visible: {
    type: Boolean,
    default: false
  },
  confirmButtonText: {
    type: String,
    default: '확인'
  },
  cancelButtonText: {
    type: String,
    default: '취소'
  }
});

// Emits 정의
const emit = defineEmits(['confirm', 'cancel']);

// Methods 정의
const confirm = () => {
  emit('confirm');
};

const cancel = () => {
  emit('cancel');
};
</script>

<template>
  <div class="modal-overlay" v-if="visible">
    <div class="modal-content">
      <div class="modal-header">
        <span class="modal-icon">
          <img src="../../assets/image/emergency.png" alt="경고" />
        </span>
        <h3>{{ message }}</h3>
      </div>
      <div class="modal-actions">
        <button @click="confirm" class="confirm-button">{{ confirmButtonText }}</button>
        <button @click="cancel" class="cancel-button">{{ cancelButtonText }}</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.modal-header {
  font-size: 18px;
  margin-bottom: 20px;
}

.modal-icon {
  margin-right: 10px;
  width: 12px; /* 아이콘 크기 지정 */
  height: 12px; /* 아이콘 크기 지정 */
}

.modal-actions button {
  margin: 0 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.confirm-button {
  background-color: #78AE6B;
  color: white;
}

.cancel-button {
  background-color: white;
  color: #888888;
  border: 1px solid #78AE6B;
}
</style>
