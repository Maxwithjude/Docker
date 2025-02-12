package com.be.byeoldam.domain.extension.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class KeywordExtractorService {

    private final ChatClient chatClient;

    @Autowired
    public KeywordExtractorService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String extractKeywords(String url) {
        SystemMessage systemMessage = new SystemMessage("""
            당신은 웹 콘텐츠 분석 전문가입니다. 주요 역할은 다음과 같습니다:
            1. 기술 블로그, 뉴스, 아티클 등 다양한 웹 콘텐츠를 분석하여 가장 핵심적인 키워드를 추출합니다.
            2. 추출된 키워드는 반드시 본문에 실제로 등장하는 단어나 구문이어야 합니다.
            3. 기술 문서의 경우 프레임워크, 라이브러리, 기술 스택 등 실제 개발자들이 검색하고 사용할 수 있는 구체적인 용어를 선호합니다.
            4. 일반 문서의 경우 해당 콘텐츠의 주제나 핵심 내용을 대표할 수 있는 명확한 단어를 선택합니다.
            5. 추출된 키워드는 나중에 사용자가 콘텐츠를 다시 찾을 때 사용되므로, 문맥상 의미있고 검색 가능한 단어여야 합니다.
            
            응답 형식은 반드시 "{키워드1}, {키워드2}, {키워드3}" 형태로만 출력해야 합니다.
            """);

        UserMessage userMessage = new UserMessage("다음 웹페이지의 본문을 분석하여 가장 핵심적인 키워드 3개만 추출해주세요. " +
                "- 키워드는 반드시 본문에 등장하는 단어/구문이어야 합니다 " +
                "- 다른 설명 없이 키워드 3개만 쉼표로 구분하여 응답해주세요 " +
                "- 기술 문서의 경우 구체적인 기술 용어를 선호합니다 " +
                "웹페이지 URL: " + url);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        try {
            return chatClient.call(prompt).getResult().getOutput().getContent();
        } catch (Exception e) {
            return "{\"error\": \"키워드를 추출할 수 없습니다.\"}";
        }

    }
}