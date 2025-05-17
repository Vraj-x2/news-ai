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
        // 1. Get raw news articles
    	List<UnifiedNews> rawNews = aggregatorService.aggregateNews(request.getTopic());


        // 2. Use Gemini to select top 5 news
        String filtered = geminiService.filterWithGemini(rawNews, request.getTopic());

        // 3. Email the results
        emailService.sendEmail(
                request.getEmail(),
                "Your Curated News on: " + request.getTopic(),
                filtered
        );

        return ResponseEntity.ok("✅ Filtered news sent successfully to " + request.getEmail());
    }
}
