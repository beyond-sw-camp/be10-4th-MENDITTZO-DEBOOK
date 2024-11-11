<script setup>
import { defineProps, defineEmits } from 'vue';

const emit = defineEmits(['click']);

// props로 disabled를 받아옵니다.
const props = defineProps({
  disabled: {
    type: Boolean,
    default: false
  }
});

// 버튼 클릭 처리
const handleClick = () => {
  if (!props.disabled) {
    emit('click');
  }
};
</script>

<template>
  <button
      class="btn"
      :disabled="props.disabled"
      @click="handleClick"
  >
  <slot></slot>
  </button>
</template>

<style scoped>
.btn {
  width: 100px;
  height: 40px;
  background-color: #78AE6B;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 20px;
  font-weight: bold;
}

.btn:disabled {
  background-color: #BDBDBD;  /* 비활성화된 상태의 배경색 */
  cursor: not-allowed;         /* 비활성화된 상태에서 커서 변경 */
}
</style>
