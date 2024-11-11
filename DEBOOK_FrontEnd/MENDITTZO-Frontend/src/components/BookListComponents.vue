<script setup>
import BookListItem from "@/components/BookListItem.vue";

const props = defineProps({
  bookLists: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="book-list">
    <BookListItem v-for="book in bookLists" :key="book.bookId" :book="book" />
  </div>
</template>

<style scoped>
.book-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  width: 80%;
  height: auto;
}
</style>
