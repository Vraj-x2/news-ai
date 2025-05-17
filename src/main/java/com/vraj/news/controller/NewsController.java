package com.vraj.news.controller;

import com.vraj.news.model.NewsRequest;
import com.vraj.news.model.UnifiedNews;
import com.vraj.news.service.EmailService;
import com.vraj.news.service.GeminiFilterService;
import com.vraj.news.service.NewsAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NewsAggregatorService aggregatorService;

    @Autowired
    private GeminiFilterService geminiService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFilteredNews(@Valid @RequestBody NewsRequest request) {
        String refinedTopic = request.getTopic() + " " + (request.getSubtopic() != null ? request.getSubtopic() : "");
        List<UnifiedNews> rawNews = aggregatorService.aggregateNews(refinedTopic);
        String filtered = geminiService.filterWithGemini(rawNews, request.getTopic(), request.getCount());

        emailService.sendEmail(
                request.getEmail(),
                "Your Curated News on: " + request.getTopic(),
                filtered
        );

        return ResponseEntity.ok("✅ Filtered news sent successfully to " + request.getEmail());
    }
}
