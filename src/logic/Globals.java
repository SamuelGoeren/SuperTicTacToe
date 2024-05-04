package logic;

public class Globals {
    public static final char[] SYMBOLS = {'□', 'x', 'o'};

    // ANSI escape code for golden color
    public static final String ACTIVE_COLOR = "\u001B[38;5;178m";
    // ANSI escape code to reset color
    public static final String RESET_COLOR = "\u001B[0m";

    public static final String BLUE_COLOR = "\u001B[34m";
    // ANSI escape code for red color
    public static final String RED_COLOR = "\u001B[31m";

    public static final String[] PLAYER_COLORS = {RED_COLOR, BLUE_COLOR};
}
