package com.javarush.task.task21.task2113;

import java.util.ArrayList;
import java.util.List;

public class Hippodrome {
    static Hippodrome game;
    private List<Horse> horses;

    public Hippodrome(List<Horse> horses) {
        this.horses = horses;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public void move() {
        for (Horse horse : horses) {
            horse.move();
        }
    }

    public void print() {
        horses.forEach(Horse::print);
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    public void run() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            move();
            print();
            Thread.sleep(200);
        }
    }

    public Horse getWinner() {
        double fullDistance = 0.0;
        for (Horse horse : horses) {
            if (horse.getDistance() > fullDistance) {
                fullDistance = horse.getDistance();
            }
        }
        for (Horse winner : horses) {
            if (winner.getDistance() == fullDistance)
                return winner;
        }
        return null;
    }

    public void printWinner() {
        System.out.println("Winner is " + getWinner().getName() + "!");
    }

    public static void main(String[] args) {
        List<Horse> list = new ArrayList<>();
        Horse zevs = new Horse("Zevs", 3.0, 0.0);
        Horse sivkaByrka = new Horse("Sivka-Byrka", 3.0, 0.0);
        Horse merin = new Horse("Merin", 3.0, 0.0);
        list.add(zevs);
        list.add(sivkaByrka);
        list.add(merin);
        game = new Hippodrome(list);
        try {
            game.run();
            game.printWinner();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
