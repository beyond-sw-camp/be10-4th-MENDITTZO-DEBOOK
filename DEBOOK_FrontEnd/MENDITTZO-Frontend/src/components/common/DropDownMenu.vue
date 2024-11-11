<script setup>
import { ref } from 'vue';

const props = defineProps({
  options: {
    type: Array,
    default: () => ["최신순", "별점순", "리뷰순"],
  },
  defaultOption: {
    type: String,
    default: "최신순",
  },
});

const emit = defineEmits(["select"]);

const isMenuOpen = ref(false);
const selectedOption = ref(props.defaultOption);

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value;
}

function selectOption(option) {
  selectedOption.value = option;
  isMenuOpen.value = false;
  emit("select", option);
}
</script>

<template>
  <div class="dropdown-menu" @click="toggleMenu">
    <div class="selected-option">
      {{ selectedOption }}
      <img
          v-if="isMenuOpen"
          src="../../assets/image/open-icon.png"
          alt="open icon"
          class="icon"
      />
      <img
          v-else
          src="../../assets/image/close-icon.png"
          alt="close icon"
          class="icon"
      />
    </div>
    <ul v-if="isMenuOpen" class="options">
      <li v-for="option in options" :key="option" @click.stop="selectOption(option)">
        {{ option }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
.dropdown-menu {
  border: 1px solid #78AE6B;
  border-radius: 5px;
  width: 110px;
  height: 30px;
  cursor: pointer;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  position: relative;
  z-index: 10;
}
.selected-option {
  padding: 8px;
  color: #444444;
  display: flex;
  align-items: center;
  justify-content: center;
}
.options {
  list-style: none;
  padding: 0;
  margin: 0;
  border: 1px solid #78AE6B;
  border-radius: 5px;
  background-color: white;
}
.options li {
  padding: 8px;
  cursor: pointer;
}
.options li:hover {
  background-color: #e8f5e9;
}
.icon {
  width: 20px; /* 아이콘 크기 설정 */
  height: 20px;
  margin-left: 16px;
}
</style>
