package com.vraj.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vraj.news.model.UnifiedNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class GeminiFilterService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public String filterWithGemini(List<UnifiedNews> newsList, String topic, int count) {
        Collections.shuffle(newsList); // Helps avoid source bias

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an expert news summarizer.\n")
              .append("You will be given a list of articles from multiple sources (NYTimes, Reddit, AlphaVantage, etc).\n")
              .append("Your job is to pick the top ").append(count)
              .append(" most relevant and diverse articles about: \"").append(topic).append("\".\n\n")
              .append("For each article, include:\n")
              .append("- A clickable title using markdown: [Title](URL)\n")
              .append("- A 2–4 sentence summary that explains the article's content and relevance.\n")
              .append("Use bullet points and avoid duplicates. Ensure variety in sources.\n\n");

        for (int i = 0; i < newsList.size(); i++) {
            UnifiedNews news = newsList.get(i);
            prompt.append("Article ").append(i + 1).append(":\n")
                  .append("Title: ").append(news.getTitle()).append("\n")
                  .append("Description: ").append(news.getDescription()).append("\n")
                  .append("URL: ").append(news.getUrl()).append("\n\n");
        }

        Map<String, Object> textMap = Map.of("text", prompt.toString());
        Map<String, Object> partsMap = Map.of("parts", List.of(textMap));
        Map<String, Object> requestBody = Map.of("contents", List.of(partsMap));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            String response = restTemplate.postForObject(
                geminiApiUrl + "?key=" + geminiApiKey,
                entity,
                String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            return textNode.isMissingNode()
                ? "❌ Gemini response format unexpected."
                : cleanGeminiResponse(textNode.asText());

        } catch (Exception e) {
            log.error("❌ Gemini API call failed: {}", e.getMessage(), e);
            return "Failed to retrieve filtered news.";
        }
    }

    private String cleanGeminiResponse(String rawText) {
        return rawText
                .replaceAll("\\*\\*", "") // Remove markdown bold
                .replaceAll("\\n\\s*", "\n") // Normalize line breaks
                .trim();
    }
}
