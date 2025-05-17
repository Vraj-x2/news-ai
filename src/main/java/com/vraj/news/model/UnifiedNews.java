package com.vraj.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedNews {
    private String title;
    private String description;
    private String url;
    private String source;
}
