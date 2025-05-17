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

    public String filterWithGemini(List<UnifiedNews> newsList, String topic) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an expert news summarizer.\n")
        .append("You will be given a list of articles from multiple sources (NYTimes, Reddit, AlphaVantage, NewsAPI, GNews).\n")
        .append("Your job is to pick the **5 most diverse and relevant** articles related to the topic: \"")
        .append(topic).append("\".\n\n")
        .append("For each article, return:\n")
        .append("- A clickable title in markdown format: [Title](URL)\n")
        .append("- A short 2-4 sentence summary explaining the content.\n\n")
        .append("Ensure articles are from a **variety of sources**. Try not to pick more than 2 from the same source. Use bullet points.\n\n")
        .append("Here are the articles:\n\n");



        for (int i = 0; i < newsList.size(); i++) {
            UnifiedNews news = newsList.get(i);
            prompt.append("Article ").append(i + 1).append(":\n")
                  .append("Title: ").append(news.getTitle()).append("\n")
                  .append("Description: ").append(news.getDescription()).append("\n")
                  .append("URL: ").append(news.getUrl()).append("\n\n");
        }

        // Build JSON request
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
                .trim(); // Clean leading/trailing whitespace
    }
}
