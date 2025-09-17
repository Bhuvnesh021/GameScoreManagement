package com.example;

class Commitment {
    private int committed;
    private boolean negative;
    private int delta;

    // Getters and Setters
    public int getCommitted() { return committed; }
    public void setCommitted(int committed) { this.committed = committed; }

    public boolean isNegative() { return negative; }
    public void setNegative(boolean negative) { this.negative = negative; }

    public int getDelta() { return delta; }
    public void setDelta(int delta) { this.delta = delta; }
}