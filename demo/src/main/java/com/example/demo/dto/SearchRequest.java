package com.example.demo.dto;

import lombok.Data;

@ Data
public class SearchRequest {
    private String query;
    private String language;
    private String sort;
}
