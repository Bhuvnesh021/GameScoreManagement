package com.example;

public class PlayerInfo {
    private String playerName;
    private Integer noOfGamesPlayed;
    private Integer totalWins;
    private Integer highestScore;
    private Integer lowestScore;
    private Integer totalScore;

    public PlayerInfo(String playerName) {
        this.playerName = playerName;
        noOfGamesPlayed = 0;
        totalScore = 0;
        highestScore = Integer.MIN_VALUE;
        totalWins = 0;
        lowestScore = Integer.MAX_VALUE;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getNoOfGamesPlayed() {
        return noOfGamesPlayed;
    }

    public void setNoOfGamesPlayed(Integer noOfGamesPlayed) {
        this.noOfGamesPlayed = noOfGamesPlayed;
    }

    public Integer getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(Integer totalWins) {
        this.totalWins = totalWins;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public Integer getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Integer lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
