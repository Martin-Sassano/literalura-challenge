package com.literalura.challenge.literalura.challenge.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {

    private List<BookDTO> results;

    public List<BookDTO> getResults() {
        return results;
    }

    public void setResults(List<BookDTO> results) {
        this.results = results;
    }
}