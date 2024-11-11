<script setup>

import {useAuthStore} from "@/store/auth.js";
import {useRouter} from "vue-router";
import {onMounted} from "vue";
import axios from "axios";

const authStore = useAuthStore();
const router = useRouter();


</script>

<template>
  <h1>홈화면</h1>
</template>

<style scoped>

</style>