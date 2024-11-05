<script setup>
import {defineEmits} from 'vue';

const emit = defineEmits(['click']);
</script>

<template>
  <button class="button" @click="emit('click')">
    <slot></slot>
  </button>
</template>

<style scoped>
.button{
  width: 327px;
  height: 49px;
  justify-content: center;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: bold;
}
</style>