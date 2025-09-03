package com.bajajfinserv.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolutionRequestDTO {
    @JsonProperty("finalQuery")
    private String finalQuery;

    public SolutionRequestDTO() {}

    public SolutionRequestDTO(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}