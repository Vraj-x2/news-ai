package com.vraj.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vraj.news.model.UnifiedNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public class NewsAggregatorService {

    @Value("${gnews.api.key}")
    private String gnewsApiKey;

    @Value("${newsapi.key}")
    private String newsapiKey;

    @Value("${nytimes.api.key}")
    private String nytimesKey;

    @Value("${alphavantage.key}")
    private String alphaKey;

    @Value("${reddit.client.id}")
    private String redditClientId;

    @Value("${reddit.client.secret}")
    private String redditClientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<UnifiedNews> aggregateNews(String topic) {
        List<UnifiedNews> allNews = new ArrayList<>();
        allNews.addAll(getNewsFromGNews(topic));
        allNews.addAll(getNewsFromNewsApi(topic));
        allNews.addAll(getNewsFromNYTimes(topic));
        allNews.addAll(getNewsFromReddit(topic));
        allNews.addAll(getNewsFromAlphaVantage(topic));

        log.info("📰 Aggregated total articles: {}", allNews.size());
        allNews.forEach(article ->
            log.debug("🔗 [{}] {} - {}", article.getSource(), article.getTitle(), article.getUrl())
        );

        return allNews;
    }

    public List<UnifiedNews> getNewsFromGNews(String topic) {
        List<UnifiedNews> results = new ArrayList<>();
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://gnews.io/api/v4/search")
                    .queryParam("q", topic)
                    .queryParam("lang", "en")
                    .queryParam("token", gnewsApiKey)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode articles = mapper.readTree(response).path("articles");

            for (JsonNode item : articles) {
                results.add(new UnifiedNews(
                        item.path("title").asText(),
                        item.path("description").asText(),
                        item.path("url").asText(),
                        "GNews"
                ));
            }
        } catch (Exception e) {
            log.error("❌ GNews fetch error: {}", e.getMessage());
        }
        return results;
    }

    public List<UnifiedNews> getNewsFromNewsApi(String topic) {
        List<UnifiedNews> results = new ArrayList<>();
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://newsapi.org/v2/everything")
                    .queryParam("q", topic)
                    .queryParam("apiKey", newsapiKey)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode articles = mapper.readTree(response).path("articles");

            for (JsonNode item : articles) {
                results.add(new UnifiedNews(
                        item.path("title").asText(),
                        item.path("description").asText(),
                        item.path("url").asText(),
                        "NewsAPI"
                ));
            }
        } catch (Exception e) {
            log.error("❌ NewsAPI fetch error: {}", e.getMessage());
        }
        return results;
    }

    public List<UnifiedNews> getNewsFromNYTimes(String topic) {
        List<UnifiedNews> results = new ArrayList<>();
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://api.nytimes.com/svc/search/v2/articlesearch.json")
                    .queryParam("q", topic)
                    .queryParam("api-key", nytimesKey)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode docs = mapper.readTree(response).path("response").path("docs");

            for (JsonNode item : docs) {
                results.add(new UnifiedNews(
                        item.path("headline").path("main").asText(),
                        item.path("snippet").asText(),
                        item.path("web_url").asText(),
                        "NYTimes"
                ));
            }
        } catch (Exception e) {
            log.error("❌ NYTimes fetch error: {}", e.getMessage());
        }
        return results;
    }

    public List<UnifiedNews> getNewsFromReddit(String topic) {
        List<UnifiedNews> results = new ArrayList<>();
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://www.reddit.com/search.json")
                    .queryParam("q", topic)
                    .queryParam("limit", "10")
                    .build().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "spring-news-ai");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode posts = mapper.readTree(response.getBody()).path("data").path("children");

            for (JsonNode post : posts) {
                JsonNode data = post.path("data");
                results.add(new UnifiedNews(
                        data.path("title").asText(),
                        "[Reddit Post]",
                        "https://www.reddit.com" + data.path("permalink").asText(),
                        "Reddit"
                ));
            }
        } catch (Exception e) {
            log.error("❌ Reddit fetch error: {}", e.getMessage());
        }
        return results;
    }

    public List<UnifiedNews> getNewsFromAlphaVantage(String topic) {
        List<UnifiedNews> results = new ArrayList<>();
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://www.alphavantage.co/query")
                    .queryParam("function", "NEWS_SENTIMENT")
                    .queryParam("topics", topic)
                    .queryParam("apikey", alphaKey)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode feed = mapper.readTree(response).path("feed");

            for (JsonNode item : feed) {
                results.add(new UnifiedNews(
                        item.path("title").asText(),
                        item.path("summary").asText(),
                        item.path("url").asText(),
                        "AlphaVantage"
                ));
            }
        } catch (Exception e) {
            log.error("❌ AlphaVantage fetch error: {}", e.getMessage());
        }
        return results;
    }
}
