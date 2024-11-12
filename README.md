# MENDITTZO-DEBOOK
![Group 19](https://github.com/user-attachments/assets/febad6e0-39f8-4d57-84ac-ea8a8d3cd522)

# 프로젝트 개요
한강 작가의 노벨문학상 소식과 함께 독서의 바람이 불어오고 있습니다. 막 책을 읽기 시작한 호기심 많은 초보 독서가들, 오랜 시간 책 속에 빠져 살아온 독서가들 모두를 위한 플랫폼 '책론을박'을 소개합니다.


책론을박은 수많은 도서 목록을 제공하고, 자신이 읽은 책에 대한 소중한 감상을 공유하는 것에서 그치지 않고, 책으로부터 얻은 풍부한 영감을 나눌 수 있는 장소를 제공합니다.


### 주요 기능
1. 카카오 로그인 제공
   - 카카오 로그인을 통해 쉽고 빠르게 책론을박에 가입 가능
   - 토큰을 통한 로그인 상태 관리
2. 기능
3. 기능
### 기술 스택

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/Vue-4FC08D?style=for-the-badge&logo=Vue.js&logoColor=white"><img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white"><img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white"><img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white"><img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white"><img src="https://img.shields.io/badge/Google Gemini-8E75B2?style=for-the-badge&logo=Google Gemini&logoColor=white">



<img src="https://img.shields.io/badge/표시할이름-색상?style=for-the-badge&logo=기술스택아이콘&logoColor=white">

### 프로젝트 아키텍처

# 개발 팀 소개
### 한화시스템 BEYOND SW캠프 10기 터치다운 ###

| [![](https://avatars.githubusercontent.com/u/77000498?v=4)](https://github.com/we4sley) | [![](https://avatars.githubusercontent.com/u/103301589?v=4)](https://github.com/kimjm9911) | [![](https://avatars.githubusercontent.com/u/174981455?v=4)](https://github.com/eunseo-76) | [![](https://avatars.githubusercontent.com/u/50124987?v=4)](https://github.com/JungUiJin) | [![](https://avatars.githubusercontent.com/u/58172997?v=4)](https://github.com/enking) | [![](https://avatars.githubusercontent.com/u/132972216?v=4)](https://github.com/HanDJ00)|
|:---:|:---:|:---:|:---:|:---:|:---:|
| 김영기 | 김지민 | 이은서 | 정의진 | 최두혁 | 한동주


# 협업 전략
### 1. 브랜치 전략
![git-flow](https://github.com/user-attachments/assets/2d7e2ef4-df03-4c5f-8c78-316065100dde)
- **main**: 배포용 branch
- **develop**: 실질적 main branch
- **feature**: 각 기능을 개발하는 branch
- 브랜치명은 '태그/도메인/#이슈번호' 의 형식을 따른다.
  - 예) feat/user/#12
- 기능 단위로 브랜치를 생성하며, 커밋 단위는 가능한 작게 조절한다. 한 번에 여러 클래스를 커밋하지 않고, Pull Request에 너무 많은 커밋이 포함되지 않도록 한다.

### 2. Issue
- 이슈 템플릿을 활용하여 다른 사람이 알기 쉽게 이슈를 작성한다.
- 이슈 작성 후 발급된 #이슈번호를 이용해 브랜치를 생성한다.

![issue_template](https://github.com/user-attachments/assets/eed8bc02-ed2c-49d3-8262-c96478b21c5b)
![issue](https://github.com/user-attachments/assets/c5f1a66d-467d-404e-b26f-b37f3992a7f5)


### 3. Pull Request
- Pull Request의 제목은 '태그/도메인 개발 내용' 의 형식을 따른다.
- 예) feat/user 개발 내용

![pr_1](https://github.com/user-attachments/assets/8f3f4f03-5e50-4d26-8963-18ece4f3ebfa)
![pr_2](https://github.com/user-attachments/assets/fd1b75e4-9a81-49b4-b3d3-d1fd365b8ca4)
![pr_3](https://github.com/user-attachments/assets/fb8650a6-6183-4644-a971-3ca22412544d)

### 4. 커밋 메시지 컨벤션
- 태그<br/>
```feat```: 기능 개발<br/>
```fix```: 버그 수정<br/>
```docs```: 문서 작성 및 수정<br/>
```style```: 코드 리팩토링 등 내용상 변경이 없는 경우<br/>
```test```: 테스트 코드<br/>
```chore```: 자잘한 수정사항<br/><br/>
- 커밋 메시지는 '태그: #이슈번호 내용' 의 형식을 따른다.
- 예) docs: #43 ReadMe 수정

![commit](https://github.com/user-attachments/assets/f4261d3e-7bba-41b8-8b21-5ade721c9da5)

### 5. 네이밍 규칙
- **클래스명**: PascalCase
- **메소드명**, **필드명**: camelCase
```
public class User{

    public void fetchUserInfo(){

        private Long userId;
       private String userName;
    }
}
```
- DTO명: ~DTO 예) ```UserRequestDTO```
- 기타 모호한 단어는 도메인 정의서에 작성한다.

### 6. 기타 규칙
- 가독성을 위해 클래스 첫 줄은 띄어쓰고, 의미 없는 공백은 줄인다.
- 최대 tap depth = 2
- magic number 사용 자제, 상수화
- 커밋 전 정렬(```ctrl + alt + L```)
- 커밋 전 사용하지 않는 import문 삭제(```ctrl + alt + o```)



# 프로젝트 설계 문서
## 1. DDD 설계
[DDD 설계 보기 - Miro](https://miro.com/app/board/uXjVLNZH4nw=/)

## 2. 요구사항 명세서
[요구사항 명세서](https://docs.google.com/spreadsheets/d/1oddc-l1flUAqUNzAMt8jnrshAidp2dHnd1w2Sz5JxWA/edit?gid=1147247037#gid=1147247037)

![요구사항 명세서 이미지](./asset/image/요구사항%20명세서%20캡쳐본.PNG)

## 3. ERD
![ERD 이미지](./asset/image/ERD%20캡쳐본.PNG)