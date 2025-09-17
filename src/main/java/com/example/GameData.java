package com.example;

import java.util.List;

public class GameData {
    private List<String> players;
    private List<Integer> totals;
    private int round;
    private List<History> history;

    // Getters and Setters
    public List<String> getPlayers() { return players; }
    public void setPlayers(List<String> players) { this.players = players; }

    public List<Integer> getTotals() { return totals; }
    public void setTotals(List<Integer> totals) { this.totals = totals; }

    public int getRound() { return round; }
    public void setRound(int round) { this.round = round; }

    public List<History> getHistory() { return history; }
    public void setHistory(List<History> history) { this.history = history; }
}