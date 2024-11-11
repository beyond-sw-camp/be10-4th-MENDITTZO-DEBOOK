<script setup>

import NavigationBar from "@/components/common/NavigationBar.vue";
import Footer from "@/components/common/Footer.vue";
import TopButton from "@/components/common/TopButton.vue";
</script>

<template>
    <NavigationBar/>
    <main>
      <RouterView :key="$route.fullPath"/>
      <TopButton />
    </main>
    <Footer/>
</template>

<style scoped>
main{
  min-height: 100vh;
}
</style>
