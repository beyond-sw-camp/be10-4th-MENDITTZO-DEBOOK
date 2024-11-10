package com.mendittzo.elastic.query.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

/*    @Value("${spring.elasticsearch.rest.uris}")
    private String host;*/

    @Bean
    public ElasticsearchClient elasticsearchClient() {

        // 자격 증명을 제거한 RestClient 생성
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")

        ).build();
        // ElasticsearchClient 설정
        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}
