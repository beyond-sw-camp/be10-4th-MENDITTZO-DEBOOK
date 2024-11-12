<template>
  <div class="chat-room-container">
    <div class="chat-room-list">
      <h1>독서토론방</h1>
      <button class="create-room-button" @click="showModal = true">독서토론방 생성</button>
      <div v-for="room in chatRooms" :key="room.chatroomId" class="chat-room">
        <div class="room-info">
          <button class="enter-room-button" @click="enterRoom(Number(room.chatroomId), room.title, room.bookId)">{{ room.title }}</button>
          <span class="member-count">{{ room.currentMemberCount }} / {{ room.maxMemberCount }}명</span>
        </div>
      </div>
      <button class="refresh-room-button" @click="fetchChatRooms">새로고침</button>
    </div>

    <!-- 채팅방 생성 모달 -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>독서토론방 생성</h3>
        <input v-model="newRoomTitle" placeholder="제목을 입력하세요..." />
        <input v-model="newBookId" placeholder="도서 ID를 입력하세요..." type="number" />
        <input v-model="newMaxMemberCount" placeholder="최대 인원을 입력하세요..." type="number" />
        <div class="modal-buttons">
          <button @click="submitChatRoom">Create</button>
          <button @click="showModal = false">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';
import router from "@/router/router.js";

const chatRooms = ref([]);
const showModal = ref(false);
const newRoomTitle = ref('');
const newBookId = ref(null);
const newMaxMemberCount = ref(null); // maxMemberCount 변수 추가

// 채팅방 목록을 가져오는 함수
const fetchChatRooms = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('Please log in to view chat rooms.');
      return;
    }

    const response = await axios.get('http://localhost:8080/api/v1/chatrooms');
    if (response.status === 200) {
      chatRooms.value = response.data.content || response.data;
    } else {
      console.error('Failed to fetch chat rooms:', response.status, response.statusText);
    }
  } catch (error) {
    console.error('Error fetching chat rooms:', error);
  }
};

// 채팅방 생성 요청을 보내는 함수
const submitChatRoom = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('Please log in to create a chat room.');
      return;
    }

    const chatroomData = {
      bookId: newBookId.value,
      title: newRoomTitle.value,
      maxMemberCount: newMaxMemberCount.value
    };

    const response = await axios.post('http://localhost:8080/api/v1/chatrooms', chatroomData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (response.status === 200) {
      alert('Chat room created successfully!');
      showModal.value = false;
      await fetchChatRooms(); // 채팅방 목록 새로고침
    } else {
      console.error('Failed to create chat room:', response.status, response.statusText);
    }
  } catch (error) {
    console.error('Error creating chat room:', error);
  }
};

// 채팅방 입장
const enterRoom = async (roomId, roomTitle, bookId) => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('Please log in to enter chat rooms.');
      return;
    }

    const response = await axios.post(`http://localhost:8080/api/v1/chatrooms/${roomId}/enter`, null, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (response.status === 200) {
      await router.push({
        path: `/chatrooms/${roomId}`,
        query: { title: roomTitle, bookId: bookId } // bookId를 쿼리에 추가
      });
    } else {
      console.error('Failed to enter chat room:', response.status, response.statusText);
    }
  } catch (error) {
    console.error('Error entering chat room:', error);
  }
};

// 페이지 로드 시 채팅방 목록 가져오기
fetchChatRooms();
</script>

<style scoped>
/* 전체 채팅방 컨테이너 */
.chat-room-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.chat-room-list {
  text-align: center;
}

.create-room-button {
  background-color: #78ae6b;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
  margin-bottom: 15px;
}

.create-room-button:hover {
  background-color: #679e5e;
}

.refresh-room-button {
  background-color: #78ae6b;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
  margin-bottom: 15px;
}

.refresh-room-button:hover {
  background-color: #679e5e;
}

h1 {
  color: #78ae6b;
  font-size: 1.5em;
  margin-bottom: 20px;
  font-weight: bold;
}

.chat-room {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  margin-bottom: 10px;
  background-color: #ffffff;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.room-info {
  display: flex;
  align-items: center;
  width: 100%;
}

.enter-room-button {
  background-color: #eef6ee;
  color: #78ae6b;
  border: 1px solid #78ae6b;
  padding: 10px 15px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.enter-room-button:hover {
  background-color: #d9edd9;
}

/* 현재 멤버 수를 오른쪽에 정렬 */
.member-count {
  margin-left: auto;
  color: #333;
  font-size: 0.9em;
  text-align: right;
}

/* 모달 오버레이 및 콘텐츠 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 320px;
  text-align: center;
}

.modal-content h3 {
  color: #78ae6b;
  margin-bottom: 20px;
  font-size: 1.2em;
}

.modal-content input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 0.95em;
}

.modal-buttons {
  display: flex;
  gap: 10px;
}

.modal-buttons button {
  flex: 1;
  background-color: #78ae6b;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-buttons button:hover {
  background-color: #679e5e;
}

.modal-buttons button:last-child {
  background-color: #ff5f5f;
}

.modal-buttons button:last-child:hover {
  background-color: #ff4c4c;
}
</style>
