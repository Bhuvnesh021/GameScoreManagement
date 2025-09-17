package com.example;

import java.util.List;

class History {
    private int round;
    private List<Commitment> entries;
    private int sumCommitted;
    private int target;
    private boolean redFlag;

    // Getters and Setters
    public int getRound() { return round; }
    public void setRound(int round) { this.round = round; }

    public List<Commitment> getEntries() { return entries; }
    public void setEntries(List<Commitment> entries) { this.entries = entries; }

    public int getSumCommitted() { return sumCommitted; }
    public void setSumCommitted(int sumCommitted) { this.sumCommitted = sumCommitted; }

    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }

    public boolean isRedFlag() { return redFlag; }
    public void setRedFlag(boolean redFlag) { this.redFlag = redFlag; }
}