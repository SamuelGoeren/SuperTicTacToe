package logic;

import java.util.Scanner;

import static logic.Globals.*;

public class TicTacToe {

    private int size;

    private int fieldSize;

    private int playerTurn;

    private int[] board;

    private int winner;

    private boolean joever;

    public TicTacToe(int size){
        this.size = size;
        this.fieldSize = size * size;
        this.playerTurn = (int) Math.round(Math.random());
        this.joever = false;
        this.initBoard(fieldSize);
    }

    private void initBoard(int fieldSize){
        this.board = new int[fieldSize];

        for(int x = 0; x < fieldSize; x++){
            this.board[x] = -1;
        }

    }

    protected int getHigherOccupancy(){
        int x = 0;
        int o = 0;
        for(int i = 0; i < this.fieldSize; i++){
            if(this.board[i] == 0) x++;
            if(this.board[i] == 1) o++;
        }

        if(x > o) return 0;
        if(o > x) return 1;

        return -1;
    }

    protected boolean doTurn(int playerTurn, int x, int y){
        if((x < 0) || (y < 0)){
            return false;
        }

        if((x >= this.size) || (y >= this.size)){
            return false;
        }

        int index = y * size + x;

        if(this.board[index] < 0){
            this.board[index] = playerTurn;
            return true;
        }

        return false;
    }

    protected boolean checkFull(){
        for(int i = 0; i < fieldSize; i++){
            if(this.board[i] < 0) return false;
        }

        return true;
    }

    protected boolean checkWinner(){
        return (checkColumns() || checkRows() || checkDiagonals());
    }

    private boolean checkDiagonals(){
        //top left to down right
        boolean hasWinner = true;
        if(this.board[0] >= 0) {
            int player = this.board[0];
            for (int i = size + 1; i < fieldSize; i += size + 1) {
                if(this.board[i] != player){
                    hasWinner = false;
                    break;
                }
            }
            if(hasWinner) return true;
        }


        hasWinner = true;
        if(this.board[size-1] >= 0){
            int player = this.board[size-1];
            for(int i = 2 * (size-1); i < fieldSize - 1; i += size - 1){
                if(this.board[i] != player){
                    hasWinner = false;
                    break;
                }
            }
            if(hasWinner) return true;
        }

        return false;
    }

    private boolean checkColumns(){
        for(int c = 0; c < size; c++){
            boolean hasWinner = true;
            if(this.board[c] >= 0){
                int player = this.board[c];
                for(int i = 1; i < size; i++){
                    if(this.board[c + i * size] != player){
                        hasWinner = false;
                        break;
                    }
                }
                if(hasWinner) return true;
            }
        }
        return false;
    }

    private boolean checkRows(){
        for(int row = 0; row < this.fieldSize; row += size){
            boolean hasWinner = true;
            if(this.board[row] >= 0){
                int player = this.board[row];
                for(int i = 1; i < size; i++){
                    if(this.board[row+i] != player){
                        hasWinner = false;
                        break;
                    }
                }
                if(hasWinner) return true;
            }
        }

        return false;
    }

    protected String rowToString(int row){
        if((row < 0) || (row >= this.size)){
            throw new IllegalArgumentException("This row doesn't exist.");
        }

        StringBuilder sb = new StringBuilder();
        int start = row * this.size;
        for(int i = start; i < start + size; i++){
            int occ = this.board[i];

            char symbol = SYMBOLS[occ + 1];
            sb.append(symbol).append("  ");
        }

        return sb.toString();
    }

    public void fillBoard(int p){
        for(int i = 0; i < this.fieldSize; i++){
            this.board[i] = p;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.size; i++){
            sb.append(rowToString(i));
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setJoever(boolean joever) {
        this.joever = joever;
    }

    public boolean isJoever() {
        return joever;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }
    /*/ONLY NEEDED FOR SINGLE TIC TAC TOE
    public void playGame(){
        boolean playing = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type in coordinates back to back (e.g. 00 for top-left corner)");
        this.printGame();
        while(playing) {
            boolean validTurn = false;
            while(!validTurn) {
                System.out.printf("Player %c's turn:\n", SYMBOLS[this.playerTurn + 1]);
                String input = scanner.nextLine();
                if(validateNumbers(input)){
                    int x = input.charAt(0) - '0';
                    int y = input.charAt(1) - '0';
                    validTurn = doTurn(this.playerTurn, x, y);
                }
            }
            this.printGame();
            if(checkFull()){
                playing = false;
                if(!checkWinner()){
                    System.out.println("Tie!");
                }
            }
            if(checkWinner()){
                System.out.printf("Player %c won!\n", SYMBOLS[this.playerTurn + 1]);
                playing = false;
            }

            this.playerTurn ^= 1;
        }

        this.joever = true;

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
*/

    /*
    public void printGame(){
        if(this.active){
            System.out.print(ACTIVE_COLOR);
        }
        System.out.print(this + RESET_COLOR);
    }*/
}
