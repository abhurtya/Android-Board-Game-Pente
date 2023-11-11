package edu.ramapo.abhurtya.pente.model;

import java.util.Scanner;

public class Human extends Player {

    private Pair<Integer, Integer> location;

    public Human() {

        this.location = new Pair<>(-1, -1);
    }

    @Override
    public void play(Board board, char symbol) {

    }

    @Override
    public void setLocation(int x, int y) {
        this.location = new Pair<>(x, y);
    }

    @Override
    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    @Override
    public String getPlayerType() {
        return "Human";
    }
}
