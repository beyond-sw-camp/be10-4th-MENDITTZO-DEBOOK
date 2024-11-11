package com.mendittzo.recommend.command.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequestDTO {
    private List<Content> contents;
    private GenerationConfig  generationConfig;

    @Getter
    @Setter
    public static class Content {
        private String role;
        private Parts parts;
    }

    @Getter
    @Setter
    public static class Parts{
        private String text;
    }

    @Getter
    @Setter
    public static class GenerationConfig {
        private int candidate_count;
        private int max_output_tokens;
        private double temperature;
    }

    public ChatRequestDTO(String prompt) {

        // 다음 내용들은 설정 고정값으로 변할일이 없음
        // 한번에 제공하는 응답 수
        int CandidateCount = 1;
        // 한번에 응답 가능한 최대 토큰수 설정
        int MaxOutputToken = 1000;
        // 응답 민감도(높을수록 창의적인 대답)
        double Temperature = 0.7;

        this.contents = new ArrayList<>();
        Content content = new Content();

        // 설정은 유저가 제공하므로 유저로 설정
        Parts parts = new Parts();
        parts.setText(prompt);
        content.setParts(parts);
        content.setRole("user");

        this.contents.add(content);
        this.generationConfig = new GenerationConfig();
        this.generationConfig.setCandidate_count(CandidateCount);
        this.generationConfig.setMax_output_tokens(MaxOutputToken);
        this.generationConfig.setTemperature(Temperature);
    }

    public void addContent(String role, String text) {
        Content content = new Content();

        Parts parts = new Parts();
        parts.setText(text);
        content.setParts(parts);
        content.setRole(role);

        this.contents.add(content);
    }
}
