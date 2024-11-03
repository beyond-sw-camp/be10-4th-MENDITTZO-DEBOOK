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
#footer-top-hr{
  border: none;
  height: 2px;
  background-color: #78AE6B;
}
footer{
  width: 1440px;
  margin: 0 auto;
  height: 300px;
  padding: 20px;
}
#footer-logo{
  padding: 0 10px;
}
#github-logo{
  width: 20px;
  height: 20px;
  margin-top: 17px;
}
.footer-text{
  color: #888888;
  padding: 0 10px;
}
.footer-div{
  display: flex;
}
.footer-div > p {
  padding: 0 10px;
}
#csr{
  color: #888888;
  background-color: #EFEFEF;
  border-radius: 15px;
  height: 25px;
}
#phone{
  color: #444444;
  font-weight: bold;
  font-size: 25px;
  margin: 10px 0;
  height: 33px;
  border-right: 1px solid #888888;
}
</style>