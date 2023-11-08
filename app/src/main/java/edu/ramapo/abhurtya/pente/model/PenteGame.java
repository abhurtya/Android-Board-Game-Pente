//package edu.ramapo.abhurtya.pente.model;
//
///*********************************************
// * Name: Anish Bhurtyal                       *
// * Project:  Pente Game                       *
// * Class:  CMPS 366                           *
// * Date:  10/27/2023                          *
// **********************************************/
//
//import java.util.Scanner;
//// import java.util.Random;
//
//public class PenteGame {
//
//    public static void displayIntro() {
//        System.out.println("_____________________________________");
//        System.out.println("|                                   |");
//        System.out.println("|          Let's Play               |");
//        System.out.println("|                                   |");
//        System.out.println("|             PENTE                 |");
//        System.out.println("|                                   |");
//        System.out.println("|            MENU OPTION            |");
//        System.out.println("|          1) Start new game        |");
//        System.out.println("|          2) Load  game            |");
//        System.out.println("|                                   |");
//        System.out.println("|___________________________________|");
//        System.out.println("\nGet ready...");
//    }
//
//    public static void main(String[] args) {
//        displayIntro();
//
//        Scanner scanner = new Scanner(System.in);
//        int option;
//        while (true) {
//            System.out.print("Enter your option: ");
//            option = scanner.nextInt();
//
//            if (option == 1 || option == 2) {
//                break; // Exit if input valid
//            } else {
//                System.out.println("Invalid choice. \nEnter 1 for new game, 2 for load game");
//                scanner.nextLine(); // Clear invalid input
//            }
//        }
//        // scanner.close();
//
//        Player human = new Human();
//        Player computer = new Computer();
//        Tournament t = new Tournament(human, computer);
//        // Random rand = new Random(); // No equivalent in Java for srand(time(0))
//
//        if (option == 1) {
//            t.startGame();
//        } else if (option == 2) {
//            Board loadedBoard = new Board();
//            String[] nextPlayer = new String[1];
//            char[] nextPlayerSymbol = new char[1];
//            if (
//                    PenteFileReader.loadGame(loadedBoard, human, computer, nextPlayer, nextPlayerSymbol)
//            ) {
//                System.out.println("Game loaded successfully!\n\n");
//                t.resumeGame(loadedBoard, human, computer, nextPlayer[0], nextPlayerSymbol[0]);
//            } else {
//                System.out.println("Failed to load the game!\n");
//            }
//        }
//    }
//}
