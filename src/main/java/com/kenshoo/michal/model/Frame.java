package com.kenshoo.michal.model;

public class Frame {

    public static final int STRIKE_COUNT = 10;
    Integer score = 0;
    Integer firstHit;
    Integer secondHit;

    int bonus = 0;

    public Frame() {
    }

    public int getScore() {
        return ((firstHit == null ? 0 : firstHit) + (secondHit == null ? 0 : secondHit) + bonus);
    }

    public Integer getFirstHit() {
        return firstHit;
    }

    public void setFirstHit(int firstHit) {
        this.firstHit = firstHit;
    }

    public Integer getSecondHit() {
        return secondHit;
    }

    public void setSecondHit(int secondHit) {
        this.secondHit = secondHit;
    }

    public boolean isFrameClosed() {
        if (isStrike() || (secondHit != null)) {
            return true;
        }

        return false;
    }

    public void addToBonus(int bonus) {
        this.bonus += bonus;
    }

    public boolean isStrike() {
        return (firstHit != null && firstHit == STRIKE_COUNT);
    }

    public boolean isSpare() {
        return ((firstHit != STRIKE_COUNT) && (getScore() == STRIKE_COUNT));
    }

    public boolean isFirstHit() {
        return (firstHit != null && secondHit == null);
    }
}
