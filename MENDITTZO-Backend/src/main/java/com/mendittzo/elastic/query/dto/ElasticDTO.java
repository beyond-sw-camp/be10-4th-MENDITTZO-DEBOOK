package com.mendittzo.elastic.query.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
