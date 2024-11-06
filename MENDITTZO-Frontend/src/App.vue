<script setup>

import NavigationBar from "@/components/NavigationBar.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
    <NavigationBar/>
    <main>
      <RouterView :key="$route.fullPath"/>
    </main>
    <Footer/>
</template>

<style scoped>
main{
  min-height: 100vh;
}
</style>
