package com.fortna.hackathon.dto;

import javax.validation.constraints.NotEmpty;

public class CreateRoundDto {
    
    @NotEmpty(message = "Round name can not be empty!")
    private String roundName;
    
    private Long startDate;

    private Long endDate;

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

}
