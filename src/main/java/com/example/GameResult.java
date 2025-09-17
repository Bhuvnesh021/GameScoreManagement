package com.example;

import java.util.Map;

public class GameResult {
    private Map<String, Integer> scores;
    private String winner;

    public GameResult(Map<String, Integer> scores) {
        this.scores = scores;
    }
    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
