package edu.ramapo.abhurtya.pente.model;

import java.util.Scanner;

public class Tournament {
    private Player human;
    private Player computer;
    private int totalHumanPoints;
    private int totalComputerPoints;

    public Tournament(Player human, Player computer) {
        this.human = human;
        this.computer = computer;
        this.totalHumanPoints = 0;
        this.totalComputerPoints = 0;
    }

    public void startGame() {
        do {
            playRound(human, computer, null, "", ' ');
        } while (askUserPlay());
        announceTournamentWinner();
    }

    public void resumeGame(Board loadedBoard, Player human, Player computer, String nextPlayerName, char nextPlayerSymbol) {
        this.totalHumanPoints = human.getPoints();
        this.totalComputerPoints = computer.getPoints();
        System.out.println("Total tournament Score:\t Human " + this.totalHumanPoints + " - " + this.totalComputerPoints + " Computer");
        System.out.println("Next Player: " + nextPlayerName + "\t Playing Symbol: " + nextPlayerSymbol);

        playRound(human, computer, loadedBoard, nextPlayerName, nextPlayerSymbol);
        while (askUserPlay()) {
            playRound(this.human, this.computer, null, "", ' ');
        }
        announceTournamentWinner();
    }

    private void announceTournamentWinner() {
        if (this.totalHumanPoints > this.totalComputerPoints) {
            System.out.println("Human wins the tournament!");
        } else if (this.totalComputerPoints > this.totalHumanPoints) {
            System.out.println("Computer wins the tournament!");
        } else {
            System.out.println("The tournament is a draw!");
        }
    }

    private boolean askUserPlay() {
        Scanner scanner = new Scanner(System.in);
        char continuePlaying;
        do {
            System.out.print("Do you want to play another round? (y/n): ");
            continuePlaying = scanner.next().charAt(0);
            if (continuePlaying == 'y' || continuePlaying == 'Y' || continuePlaying == 'n' || continuePlaying == 'N') {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        } while (true);
        // scanner.close();
        return (continuePlaying == 'y' || continuePlaying == 'Y');
    }

    private void playRound(Player human, Player computer, Board loadedBoard, String nextPlayerName, char nextPlayerSymbol) {

        Pair<Integer, Integer> roundPoints;

        if (loadedBoard != null) {
            // We're resuming a saved game; continue from where we left off
            Round round = new Round(human, computer, loadedBoard);
            Player currentPlayer = nextPlayerName.equals("Human") ? human : computer;
            roundPoints = round.resume(currentPlayer, nextPlayerSymbol);
        } else {
            Round round = new Round(human, computer);
            if (this.totalHumanPoints == this.totalComputerPoints) {
                // By default, it will toss in Round class
                roundPoints = round.play(' ');
            } else {
                char firstPlayerSymbol;
                if (this.totalHumanPoints > this.totalComputerPoints) {
                    System.out.println("Human points more, will play first.");
                    firstPlayerSymbol = 'H';
                } else {
                    System.out.println("Computer points more, will play first.");
                    firstPlayerSymbol = 'C';
                }
                roundPoints = round.play(firstPlayerSymbol);
            }
        }

        this.totalHumanPoints += roundPoints.getKey();
        this.totalComputerPoints += roundPoints.getValue();

        System.out.println("Points this round: \tHuman " + roundPoints.getKey() + " - " + roundPoints.getValue() + " Computer");
        System.out.println("Total tournament Score:\t Human " + this.totalHumanPoints + " - " + this.totalComputerPoints + " Computer");
    }

}
