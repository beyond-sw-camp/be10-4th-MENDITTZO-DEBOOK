# 공식 Elasticsearch 이미지를 베이스로 사용
FROM docker.elastic.co/elasticsearch/elasticsearch:8.15.3

# 한글 텍스트 분석을 위한 Nori 플러그인 설치
RUN elasticsearch-plugin install analysis-nori
