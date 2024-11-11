<script>
import { onMounted, reactive, ref, nextTick } from "vue";
import axios from "axios";
import {useAuthStore} from "@/store/auth.js";

const authStore = useAuthStore();

const fetchUserInfo = async () => {
  try {
    const response = await axios.get('/api/v1/user/query/info', {
      headers: { Authorization: `Bearer ${authStore.accessToken}` } // 토큰 추가
    });
    // 사용자 정보 저장 로직 추가
  } catch (error) {
    console.error("사용자 정보 가져오기 실패: ", error);
  }
};

const state = reactive({
  books: [],
  currentPage: 1,
  totalPages: 1,
  totalItems: 0,
  pageSize: 10
});

const bestBooks = ref([]);
const chatMessages = ref([
]);




const fetchBestBooks = async () => {
  const response = (await axios.get('/booklists', {
  params: {
    page: 1,
        size: '5'
  }
})).data;
bestBooks.value = response.bookLists;
};

const fetchBooks = async (page = 1) => {
  try {
    const response = await axios.get('/booklists', {
    params: {
      page,
          size: 10
    }
  });
  state.books = response.data.bookLists;
  state.currentPage = response.data.currentPage;
  state.totalPages = response.data.totalPages;
  state.totalItems = response.data.totalItems;
  state.pageSize = response.data.pageSize;
} catch (error) {
  console.error('도서 목록을 불러오는 중 에러가 발생했습니다: ', error);
}
};

const updateChatMessage = async () => {
  try{
    chatMessages.value = [];

    // 서버로 요청 전송
    const response = await axios.get(
        '/recommend/chatlog',
        {
          headers: { Authorization: `Bearer ${authStore.accessToken}` },
        }
  );

    // 서버에서 받아온 데이터를 chatMessages에 추가
    if (response.data && Array.isArray(response.data)) {
      response.data.forEach(chat => {
        // questionContent 파싱하여 prompt 추출
        const question = JSON.parse(chat.questionContent).prompt;

        // 질문 메시지 추가
        chatMessages.value.push({
          sender: "user",
          message: question,
          timestamp: new Date(chat.questionDatetime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
        });

        // 응답 메시지 추가
        chatMessages.value.push({
          sender: "bot",
          message: chat.responseContent,
          timestamp: new Date(chat.questionDatetime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
        });
      });
    } else {
      console.error("서버에서 예상하지 않은 데이터 형식이 반환되었습니다:", response.data);
    }

    // 새로운 메시지가 추가된 후 스크롤을 맨 아래로 이동
    nextTick(() => {
      const chatBox = document.querySelector(".chat-box");
      chatBox.scrollTop = chatBox.scrollHeight;
    });
  }catch (error) {
    console.error("메시지를 전송하는 중 에러가 발생했습니다: ", error);
  }

};

const sendMessageToServer = async (message) => {
  try {

    // 서버로 요청 전송
    const response = await axios.post('/recommend/chat',  { prompt: message },
        {
          headers: { Authorization: `Bearer ${authStore.accessToken}` }}
  );

    // 새로운 메시지가 추가된 후 스크롤을 맨 아래로 이동
    nextTick(() => {
      const chatBox = document.querySelector(".chat-box");
      chatBox.scrollTop = chatBox.scrollHeight;
    });
  } catch (error) {
    console.error("메시지를 전송하는 중 에러가 발생했습니다: ", error);
  }
};


export default {
  name: 'BookRecommendation',
  data() {
    return {
      newMessage: ''
    };
  },
  methods: {
    async sendMessage() { // async 추가
      if (this.newMessage.trim() !== "") {
        await sendMessageToServer(this.newMessage); // 메시지 전송이 완료될 때까지 기다림
        await updateChatMessage(); // 메시지 전송 후 채팅 기록 업데이트
        this.newMessage = ""; // 메시지 전송 후 입력 필드 초기화
      }
    }
  },
  setup() {

    onMounted(async () => {
      await fetchBestBooks();
      await fetchBooks();

      console.log("페이지 로딩 시 updateChatMessage 호출");
      await updateChatMessage(); // 최초 로딩 시 채팅 메시지를 불러오기
    });
    return {
      state,
      bestBooks,
      chatMessages,
      updateChatMessage,
      sendMessageToServer,
      fetchUserInfo
    };
  }
};
</script>

<template>
  <div class="recommendation-container">
    <div class="chat-section">
      <h2>도서 추천</h2>
      <div class="chat-box">
        <div class="chat-message" v-for="(chat, index) in chatMessages" :key="index">
          <div v-if="chat.sender === 'bot'" class="bot-message">
            <span>도서 추천 챗봇</span>
            <p>{{ chat.message }}</p>
            <small class="timestamp">{{ chat.timestamp }}</small>
          </div>
          <div v-else class="user-question">
            <p>{{ chat.message }}</p>
            <small class="timestamp">{{ chat.timestamp }}</small>
          </div>
        </div>
      </div>
      <div class="user-input">
        <input type="text" v-model="newMessage" placeholder="메시지를 입력하세요" />
        <button @click="sendMessage">전송하기</button>
      </div>
    </div>

    <div class="sidebar">
      <div class="top-books">
        <article class="list-div">
          <h2>리뷰많은 책</h2>
          <div class="chat" v-for="(book, index) in bestBooks" :key="index">
            <div class="list-left">
              <img class="bmk-image" src="../assets/image/bookmark.png" alt="북마크이미지">
              <p class="list-title">{{ book.title }}</p>
            </div>
            <div class="list-right">
              <p class="list_info">{{ book.author }}</p>
              <p class="list_info">{{ book.publisher }}</p>
            </div>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>
.recommendation-container {
  display: flex;
  gap: 20px;
  padding: 20px;
}

.chat-section {
  flex: 2;
  background-color: #f0f5f5;
  border-radius: 8px;
  padding: 20px;
}

.chat-box {
  background-color: #78AE6B;
  border-radius: 8px;
  padding: 15px;
  max-height: 700px; /* 최대 높이를 설정 */
  overflow-y: auto; /* 내용이 많아질 때 스크롤 가능 */
}

.chat-message {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.bot-message {
  background-color: #ffffff;
  padding: 10px;
  border-radius: 8px;
  max-width: 60%;
  display: inline-block;
  margin-right: auto;
}

.user-question {
  background-color: #ffc857;
  padding: 10px;
  border-radius: 8px;
  max-width: 60%;
  display: inline-block;
  margin-left: auto;
}

.user-input {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 15px;
}

.user-input input {
  flex: 1;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.user-input button {
  background-color: #ffa500;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.sidebar {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top-books,
.recent-requests {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.timestamp {
  color: gray;
}
</style>