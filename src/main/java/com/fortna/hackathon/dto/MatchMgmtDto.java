package com.fortna.hackathon.dto;

public class MatchMgmtDto {
    
    private Long id;
    
    private String firstPlayer;
    
    private String secondPlayer;
    
    private String awayMatchWinner;
    
    private String homeMatchWinner;
    
    private String finalWinner;
    
    private String errorMessage;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getAwayMatchWinner() {
        return awayMatchWinner;
    }

    public void setAwayMatchWinner(String awayMatchWinner) {
        this.awayMatchWinner = awayMatchWinner;
    }

    public String getHomeMatchWinner() {
        return homeMatchWinner;
    }

    public void setHomeMatchWinner(String homeMatchWinner) {
        this.homeMatchWinner = homeMatchWinner;
    }

    public String getFinalWinner() {
        return finalWinner;
    }

    public void setFinalWinner(String finalWinner) {
        this.finalWinner = finalWinner;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isResultPublished() {
        return resultPublished;
    }

    public void setResultPublished(boolean resultPublished) {
        this.resultPublished = resultPublished;
    }

    private boolean resultPublished;

}
