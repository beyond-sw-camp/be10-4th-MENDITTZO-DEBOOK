<script setup>

import ButtonLong from "@/components/common/ButtonLong.vue";
</script>

<template>
  <div class="main-page">
    <h1>책론을박</h1>

    <!-- 로그인 버튼 -->
    <RouterLink to="/login" active-class="active">
      <ButtonLong>로그인</ButtonLong>
    </RouterLink>
  </div>

</template>

<style scoped>

</style>