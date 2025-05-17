package com.vraj.news.controller;

import com.vraj.news.model.NewsRequest;
import com.vraj.news.model.UnifiedNews;
import com.vraj.news.service.EmailService;
import com.vraj.news.service.GeminiFilterService;
import com.vraj.news.service.NewsAggregatorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class WebController {

    @Autowired
    private NewsAggregatorService aggregatorService;

    @Autowired
    private GeminiFilterService geminiService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("newsRequest", new NewsRequest());
        return "index";
    }

    @PostMapping("/send-news")
    public String handleSubmit(@Valid @ModelAttribute NewsRequest newsRequest,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        String refinedTopic = newsRequest.getTopic() + " " + (newsRequest.getSubtopic() != null ? newsRequest.getSubtopic() : "");
        List<UnifiedNews> rawNews = aggregatorService.aggregateNews(refinedTopic);
        String filtered = geminiService.filterWithGemini(rawNews, newsRequest.getTopic(), newsRequest.getCount());

        emailService.sendEmail(newsRequest.getEmail(),
                "Your Gemini-Picked News on: " + newsRequest.getTopic(),
                filtered);

        model.addAttribute("successMessage", "✅ News sent to " + newsRequest.getEmail());
        model.addAttribute("newsRequest", new NewsRequest()); // Reset form
        return "index";
    }
}
