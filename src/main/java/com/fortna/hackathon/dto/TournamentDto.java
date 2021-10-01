package com.fortna.hackathon.dto;

import java.util.List;

public class TournamentDto {

    private List<MatchDto> teams;

    private List<List<ScoreDto>> results;

    public List<MatchDto> getTeams() {
        return teams;
    }

    public void setTeams(List<MatchDto> teams) {
        this.teams = teams;
    }

    public List<List<ScoreDto>> getResults() {
        return results;
    }

    public void setResults(List<List<ScoreDto>> results) {
        this.results = results;
    }

    // Nested classes
    public static class MatchDto {
        private Long id;
        private Long firstPlayerId;
        private String firstPlayer;
        private String firstPlayerAvatar;
        private Long secondPlayerId;
        private String secondPlayer;
        private String secondPlayerAvatar;
        private Long roundId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getRoundId() {
            return roundId;
        }

        public void setRoundId(Long roundId) {
            this.roundId = roundId;
        }

        public Long getFirstPlayerId() {
            return firstPlayerId;
        }

        public void setFirstPlayerId(Long firstPlayerId) {
            this.firstPlayerId = firstPlayerId;
        }

        public Long getSecondPlayerId() {
            return secondPlayerId;
        }

        public void setSecondPlayerId(Long secondPlayerId) {
            this.secondPlayerId = secondPlayerId;
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

    public static class ScoreDto {

        private List<Integer> score;

        public List<Integer> getScore() {
            return score;
        }

        public void setScore(List<Integer> score) {
            this.score = score;
        }

    }
}
