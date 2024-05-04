package logic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static logic.Globals.*;

public class SuperTicTacToe {

    private int size;

    private int fieldSize;

    private int currentActive;

    private int playerTurn;

    private Set<Integer> availableGames;

    private TicTacToe[] board;

    private TicTacToe bgBoard;

    public SuperTicTacToe(int size){
        this.size = size;
        this.fieldSize = size * size;
        this.board = new TicTacToe[fieldSize];
        this.playerTurn = (int) Math.round(Math.random());
        this.availableGames = new HashSet<>();
        this.bgBoard = new TicTacToe(this.size, false);

        for(int i = 0; i < fieldSize; i++){
            this.availableGames.add(i);
        }

        this.initBoard();
    }

    private void initBoard(){
        for(int i = 0; i < fieldSize; i++){
            this.board[i] = new TicTacToe(this.size, false);
        }
        int initialActive = (int) Math.floor(fieldSize * Math.random());
        this.currentActive = initialActive;
    }

    public void playGame(){
        System.out.println("Welcome to Super Tic-Tac-Toe!");
        System.out.println("The game starts on the highlighted field " + intToCoords(this.currentActive));
        this.printGame();
        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        while(playing){
            TicTacToe currentGame = this.board[currentActive];
            boolean validTurn = false;
            int nextActive = -1;
            int x = -1;
            int y = -1;
            while(!validTurn) {
                System.out.printf("Player %c's turn on board %s\n", SYMBOLS[this.playerTurn + 1],
                        intToCoords(this.currentActive));
                String input = scanner.nextLine();
                if(validateNumbers(input)){
                    x = input.charAt(0) - '0';
                    y = input.charAt(1) - '0';
                    validTurn = currentGame.doTurn(this.playerTurn, x, y);
                    nextActive = coordsToIndex(this.size, x, y);
                }
            }

            x = currentActive % this.size;
            y = currentActive / this.size;
            //Joever check
            if(currentGame.checkFull()){
                currentGame.setJoever(true);
                this.availableGames.remove(currentActive);
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
                this.availableGames.remove(currentActive);
            }

            if(this.board[nextActive].isJoever() && (!this.availableGames.isEmpty())){
                System.out.println("Game " + intToCoords(nextActive) + " is already over.\n" +
                        "You can choose one of the available games left.");
                for(int i : this.availableGames){
                    System.out.print(intToCoords(i) + " ");
                }
                boolean validInput = false;
                while(!validInput){
                    String input = scanner.nextLine();
                    if(validateNumbers(input)){
                        x = input.charAt(0) - '0';
                        y = input.charAt(1) - '0';
                        int choice = coordsToIndex(this.size, x, y);
                        validInput = this.availableGames.contains(choice);
                        nextActive = choice;
                    }
                }
            }

            currentActive = nextActive;
            this.printGame();

            //Full game check
            if(bgBoard.checkWinner()){
                System.out.printf("Player %c won Super Tic-Tac-Toe!\n", SYMBOLS[playerTurn+1]);
                playing = false;
            }

            if(bgBoard.checkFull()){
                if(!bgBoard.checkWinner()){
                    System.out.println("Tie!");
                    playing = false;
                }
            }
            this.playerTurn ^= 1;

        }
    }

    public void printGame(){
        for(int r = 0; r < this.fieldSize; r += size){
            for(int j = 0; j < size; j++){
                for(int i = r; i < r + size; i++){
                    TicTacToe currentBoard = this.board[i];
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
        int x = c % this.size;
        int y = c / this.size;

        return String.format("(%d/%d)", x, y);
    }
}
