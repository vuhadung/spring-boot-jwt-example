package com.fortna.hackathon.dto;

import java.util.List;

public class TournamentDto {

    private List<MatchDto> teams;

    private List<List<String>> results;

    public List<MatchDto> getTeams() {
        return teams;
    }

    public void setTeams(List<MatchDto> teams) {
        this.teams = teams;
    }

    public List<List<String>> getResults() {
        return results;
    }

    public void setResults(List<List<String>> results) {
        this.results = results;
    }

    // Nested classes
    public static class MatchDto {
        private Long id;
        private String firstPlayer;
        private String firstPlayerAvatar;
        private String secondPlayer;
        private String secondPlayerAvatar;

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

        public String getFirstPlayerAvatar() {
            return firstPlayerAvatar;
        }

        public void setFirstPlayerAvatar(String firstPlayerAvatar) {
            this.firstPlayerAvatar = firstPlayerAvatar;
        }

        public String getSecondPlayerAvatar() {
            return secondPlayerAvatar;
        }

        public void setSecondPlayerAvatar(String secondPlayerAvatar) {
            this.secondPlayerAvatar = secondPlayerAvatar;
        }

    }
}
