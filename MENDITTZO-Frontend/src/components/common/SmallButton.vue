<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['click']);
</script>

<template>
  <button class="btn" @click="emit('click')">
    <slot></slot>
  </button>
</template>

<style scoped>
  .btn {
    width: 80px;
    height: 30px;
    background-color: #78AE6B;
    color: white;
    border: none;
    border-radius: 10px;
    cursor: pointer;
    font-size: 14px;
    font-weight: bold;
  }
</style>