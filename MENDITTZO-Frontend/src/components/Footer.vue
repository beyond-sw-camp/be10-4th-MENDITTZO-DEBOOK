<script setup>

</script>

<template>
  <hr id="footer-top-hr">
  <footer>
    <div id="footer-top">
      <img id="footer-logo" src="../assets/image/logo_gray.png" alt="흑백로고이미지">
      <p class="footer-text">(07060) 서울특별시 동작구 보라매로 87(신대방동 344-4) 팀 맨뒷조</p>
    </div>

    <div class="footer-div">
      <p id="csr">고객센터</p> <p id="phone">1234-5678</p> <p class="footer-text">운영시간: 평일 09:00 ~ 09:00(평일·토·일·공휴일 휴무)</p>
    </div>
    
    <div class="footer-div">
      <p class="footer-text">COPYRIGHT © 2024 MENDITTZO. ALL RIGHTS RESERVED. </p>
      <img id="github-logo" src="../assets/image/github-mark.png" alt="깃허브마크">
    </div>
  </footer>
</template>

<style scoped>
/* 푸터 상단 선 */
#footer-top-hr{
  border: none;
  height: 2px;
  background-color: #78AE6B;
}
/* 푸터 */
footer{
  width: 1440px;
  margin: 0 auto;
  height: 300px;
  padding: 20px;
}
/* 푸터 책론을박 로고 */
#footer-logo{
  padding: 0 10px;
}
/* 푸터 깃허브 로고 */
#github-logo{
  width: 20px;
  height: 20px;
  margin-top: 17px;
}

/* 푸터 일반 텍스트 */
.footer-text{
  color: #888888;
  padding: 0 10px;
}
/* 푸터 안 div */
.footer-div{
  display: flex;
}
/* div 안 p 태그 */
.footer-div > p {
  padding: 0 10px;
}

/* 고객센터 */
#csr{
  color: #888888;
  background-color: #EFEFEF;
  border-radius: 15px;
  height: 25px;
}

/* 전화번호 */
#phone{
  color: #444444;
  font-weight: bold;
  font-size: 25px;
  margin: 10px 0;
  height: 33px;
  border-right: 1px solid #888888;
}
</style>