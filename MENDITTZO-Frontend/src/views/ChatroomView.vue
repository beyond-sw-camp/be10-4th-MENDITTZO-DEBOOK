<template>
  <div class="chat-container">
    <!-- 책 정보 -->
    <div class="book-info">
      <img :src="bookImg" alt="Book Cover" class="book-cover"/>
      <div class="book-details">
        <h3 class="book-title">{{ bookTitle }}</h3>
        <p class="book-author">by {{ bookAuthor }}</p>
      </div>
    </div>

    <h2 class="chat-title">{{ currentRoomTitle }}</h2>
    <div class="messages-container">
      <div v-for="message in messages" :key="message.createDatetime" class="message">
        <span class="message-nickname">{{ message.nickname }}</span>
        <span class="message-content">{{ message.chatContent }}</span>
        <span class="message-time">
          {{
            new Date(new Date(message.createDatetime).setHours(new Date(message.createDatetime).getHours() + 9)).toLocaleTimeString()
          }}
        </span>
      </div>
    </div>
    <div class="input-area">
      <input v-model="newMessage" placeholder="Type a message..."/>
      <button @click="sendMessage">Send</button>
    </div>
    <div class="actions">
      <button class="leave-room-button" @click="leaveRoom">Leave Room</button>
      <button class="back-to-menu-button" @click="goToMenu">Back to Menu</button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {Stomp} from '@stomp/stompjs';
import {useRoute, useRouter} from 'vue-router';
import {useAuthStore} from "@/store/auth.js";

const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const currentRoomId = route.params.id;
const bookId = route.query.bookId; // 책 ID를 쿼리에서 가져옵니다.
const currentRoomTitle = ref(route.query.title || 'Chat Room');

// 책 정보 변수 추가
const bookTitle = ref('');
const bookImg = ref('');
const bookAuthor = ref('');
const messages = ref([]);
const newMessage = ref('');
let stompClient = null;

// 책 정보 가져오기 함수
const fetchBookDetails = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    const response = await axios.get(`http://localhost:8080/api/v1/booklists/${bookId}`);

    const bookData = response.data.bookResponse; // BookResponseDTO
    bookTitle.value = bookData.title;
    bookImg.value = bookData.img;
    bookAuthor.value = bookData.author;
  } catch (error) {
    console.error('Failed to fetch book details:', error);
  }
};

// 기존 메시지 가져오기 함수
const fetchMessages = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    const response = await axios.get(`http://localhost:8080/api/v1/chats/${currentRoomId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    messages.value = response.data;
  } catch (error) {
    console.error('Failed to fetch messages:', error);
  }
};

const connectToServer = () => {
  const socket = new WebSocket('ws://localhost:8080/ws-stomp');
  stompClient = Stomp.over(() => socket);

  stompClient.connect({}, () => {
    console.log('Connected to WebSocket server');
    stompClient.subscribe(`/topic/chatroom/${currentRoomId}`, (message) => {
      if (message.body) {
        try {
          const parsedMessage = JSON.parse(message.body);
          messages.value.push(parsedMessage);
        } catch (e) {
          console.error('Failed to parse message body:', e);
        }
      }
    });
  }, (error) => {
    console.error('Connection error:', error);
  });
};

const sendMessage = () => {
  if (newMessage.value.trim() !== '' && stompClient) {
    const messageData = {
      chatContent: newMessage.value,
      chatroomId: currentRoomId,
      nickname: authStore.nickname,
      createDatetime: new Date().toISOString()
    };
    stompClient.send("/app/chat/send", {}, JSON.stringify(messageData));
    newMessage.value = '';
  }
};

const leaveRoom = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    await axios.post(`http://localhost:8080/api/v1/chatrooms/${currentRoomId}/leave`, null, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    await router.push('/chatrooms');
  } catch (error) {
    console.error('Failed to leave chat room:', error);
  }
};

const goToMenu = () => {
  router.push('/chatrooms');
};

onMounted(() => {
  fetchBookDetails(); // 책 정보를 가져옵니다
  fetchMessages();
  connectToServer();
});
</script>

<style scoped>
.chat-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.book-info {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.book-cover {
  width: 60px;
  height: 90px;
  object-fit: cover;
  border-radius: 5px;
  margin-right: 15px;
}

.book-details {
  flex-grow: 1;
}

.book-title {
  color: #333;
  font-size: 1.2rem;
  margin: 0;
  font-weight: bold;
}

.book-author {
  color: #666;
  font-size: 0.9rem;
}

.chat-title {
  color: #78ae6b;
  font-size: 1.5rem;
  text-align: center;
  margin-bottom: 15px;
}

.messages-container {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 10px;
  padding: 10px;
  background-color: #ffffff;
  border-radius: 5px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.05);
}

.message {
  display: flex;
  align-items: center;
  padding: 8px;
  margin-bottom: 8px;
  border-radius: 8px;
  background-color: #e0f4e1;
}

.message-nickname {
  font-weight: bold;
  color: #4b9f4a;
  margin-right: 10px;
}

.message-content {
  flex: 1;
  color: #333;
}

.message-time {
  font-size: 0.8rem;
  color: #999;
  margin-left: 10px;
  white-space: nowrap;
}

.input-area {
  display: flex;
  gap: 8px;
  padding: 10px;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 15px;
}

input[type="text"] {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  transition: border-color 0.3s ease;
}

input[type="text"]:focus {
  border-color: #78ae6b;
  outline: none;
}

button {
  background-color: #78ae6b;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #679e5e;
}

.actions {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.leave-room-button {
  flex: 1;
  background-color: #ff5f5f;
  padding: 10px;
  font-size: 1rem;
}

.leave-room-button:hover {
  background-color: #ff4c4c;
}

.back-to-menu-button {
  flex: 1;
  background-color: #a8a8a8;
  padding: 10px;
  font-size: 1rem;
}

.back-to-menu-button:hover {
  background-color: #888888;
}
</style>
