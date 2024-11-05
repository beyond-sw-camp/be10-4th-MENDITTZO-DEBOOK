package com.mendittzo.elastic.query.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@Document(indexName = "books")
// 엘라스틱 서치와 인덱싱 테스트 할때 작성한 어노테이션 로그 스테시 작업 이후 삭제 예정
public class ElasticDTO {

    @Id
    private Long bookId;

    @Field(type = FieldType.Text) // 검색 쿼리 작성시 필드 검색이 가능하게 함 인덱싱?하는 느낌이라고 생ㄱ가하면 될듯
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Text)
    private String publisher;

    @Field(type = FieldType.Text)
    private String pubdate;

    @Field(type = FieldType.Text)
    private String info;

    @Field(type = FieldType.Text)
    private String img;
}
