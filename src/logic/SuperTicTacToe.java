package logic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static logic.Globals.*;

public class SuperTicTacToe {

    private final int size;

    private final int fieldSize;

    private int currentActive;

    private int nextActive;

    private int playerTurn;

    private final Set<Integer> availableGames;

    private final TicTacToe[] board;

    private final TicTacToe bgBoard;

    private final Scanner scanner;

    private boolean running;

    public SuperTicTacToe(int size){
        this.size = size;
        fieldSize = size * size;
        board = new TicTacToe[fieldSize];
        playerTurn = (int) Math.round(Math.random());
        availableGames = new HashSet<>();
        bgBoard = new TicTacToe(size);
        scanner = new Scanner(System.in);
        running = true;

        initAvailableGames();
        initBoard();
    }

    private void initAvailableGames(){
        for(int i = 0; i < fieldSize; i++){
            availableGames.add(i);
        }
    }

    private void initBoard(){
        for(int i = 0; i < fieldSize; i++){
            board[i] = new TicTacToe(size);
        }
        currentActive = (int) Math.floor(fieldSize * Math.random());
    }

    public void playGame(){
        printInstructions();
        printGame();
        while(running){
            TicTacToe currentGame = board[currentActive];
            handleSubgameMove(currentGame, scanner);
            handleSubgameOver(currentGame);
            handleNextGameFull(scanner);
            currentActive = nextActive;
            printGame();
            checkFullGameWinner();
            playerTurn ^= 1;
        }
        scanner.close();
    }

    private void printInstructions(){
        System.out.println("Welcome to Super Tic-Tac-Toe! To make a move, type in the X and Y coordinates back to back.");
        System.out.println("The origin is in the top-left corner. So for example type '00' to place in the top-left corner");
        System.out.println("or '12' for the second field in the third row.");
        System.out.println("The game starts on the highlighted field " + intToCoords(currentActive));
    }

    private void handleSubgameMove(TicTacToe currentGame, Scanner scanner){
        boolean validTurn = false;
        int x, y;
        while(!validTurn) {
            System.out.printf("Player %c's turn on board %s\n", SYMBOLS[playerTurn + 1],
                    intToCoords(currentActive));
            String input = scanner.nextLine();
            if(validateNumbers(input)){
                x = input.charAt(0) - '0';
                y = input.charAt(1) - '0';
                validTurn = currentGame.doTurn(playerTurn, x, y);
                nextActive = coordsToIndex(size, x, y);
            }
        }
    }

    private void handleSubgameOver(TicTacToe currentGame){
        int x = currentActive % size;
        int y = currentActive / size;
        //Joever check
        if(currentGame.checkFull()){
            currentGame.setJoever(true);
            availableGames.remove(currentActive);
            if(!currentGame.checkWinner()){
                int higherOcc = currentGame.getHigherOccupancy();
                currentGame.setWinner(higherOcc);
                currentGame.fillBoard(higherOcc);
                bgBoard.doTurn(playerTurn, x, y);
            }
        }

        //Joever check
        if(currentGame.checkWinner()){
            currentGame.setWinner(playerTurn);
            currentGame.fillBoard(playerTurn);
            currentGame.setJoever(true);
            bgBoard.doTurn(playerTurn, x, y);
            availableGames.remove(currentActive);
        }
    }

    private void handleNextGameFull(Scanner scanner){
        if(board[nextActive].isJoever() && (!availableGames.isEmpty())){
            System.out.println("Game " + intToCoords(nextActive) + " is already over.\n" +
                    "You can choose one of the available games left.");
            for(int i : availableGames){
                System.out.print(intToCoords(i) + " ");
            }
            boolean validInput = false;
            while(!validInput){
                String input = scanner.nextLine();
                if(validateNumbers(input)){
                    int x = input.charAt(0) - '0';
                    int y = input.charAt(1) - '0';
                    int choice = coordsToIndex(size, x, y);
                    validInput = availableGames.contains(choice);
                    nextActive = choice;
                }
            }
        }
    }

    private void checkFullGameWinner(){
        if(bgBoard.checkWinner()){
            System.out.printf("Player %c won Super Tic-Tac-Toe!\n", SYMBOLS[playerTurn+1]);
            running = false;
        }

        if(bgBoard.checkFull()){
            if(!bgBoard.checkWinner()){
                System.out.println("Tie!");
                running = false;
            }
        }
    }

    private void printGame(){
        for(int r = 0; r < fieldSize; r += size){
            for(int j = 0; j < size; j++){
                for(int i = r; i < r + size; i++){
                    TicTacToe currentBoard = board[i];
                    boolean highlighted = (i == currentActive);

                    if(highlighted){
                        System.out.print(ACTIVE_COLOR);
                    }
                    if(currentBoard.isJoever()){
                        System.out.print(PLAYER_COLORS[currentBoard.getWinner()]);
                    }

                    System.out.print(currentBoard.rowToString(j) + RESET_COLOR + "|  ");
                }
                System.out.println();
            }
            System.out.println("-".repeat(fieldSize*(size+1)));
        }
    }

    private static int coordsToIndex(int size, int x, int y){
        return y * size + x;
    }

    private static boolean validateNumbers(String input){
        int length = input.length();
        if(length != 2){
            return false;
        }

        for(int i = 0; i < length; i++){
            if((input.charAt(i) < '0') || (input.charAt(i) > '9')) return false;
        }

        return true;
    }

    private String intToCoords(int c){
        int x = c % size;
        int y = c / size;

        return String.format("(%d/%d)", x, y);
    }
}
