package com.dekinci.eden.model.animal;

public class State {
    private int satiety;
    private int hp;


    public State(int satiety, int hp) {
        this.satiety = satiety;
        this.hp = hp;
    }

    public void increaseSatiety(int inc) {
        satiety += inc;
        if (satiety > 100) satiety = 100;
    }

    public void decreaseSatiety(int dec) {
        satiety -= dec;
    }

    public void increaseHP(int inc) {
        hp += inc;
    }

    public void decreaseHP(int dec) {
        hp -= dec;
    }

    public boolean hungry() {
        return satiety < 100;
    }
}
