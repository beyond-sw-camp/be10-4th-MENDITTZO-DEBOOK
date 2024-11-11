# OpenJDK 17 버전의 이미지를 가져와 JVM 환경을 구축한다.
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 소스 파일 복사
COPY . .

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# Gradle 빌드, 테스트 제외
RUN ./gradlew build -x test

# 생성된 JAR 파일을 실행하도록 설정
ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
