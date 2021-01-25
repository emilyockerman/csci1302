package cs1302.game;

import java.lang.*;
import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

/**
 * Class for a game of Minesweeper.  First project in 1302, aka so time consuming.
 * I think I accidentally made a horcrux out of this program sometime in the past few
 * days so keep away from basilisk venom and/or the sword of griffindor.
 */
public class MinesweeperGame {
    //variable declarations
    private int inputRow;
    private int inputCol;
    private int inputNumMines;
    private int userRow;
    private int userCol;
    private int roundsCompleted;
    private int nearbyMines;
    private int emptySquares;
    private int minesMarked;
    private int e;
    private int num;
    private double score;
    private String command;
    private String format;
    private String f = "F";
    private String q = "?";
    private int[][] mineLocations;
    private String[][] board;
    private boolean[][] mineBoard;
    private boolean play = true;
    private boolean minesShown;

    /**
     * constructor for an instantiation of class MineSweeperGame.
     * @param seed string containing valid information to create the board
     * @catch NullPointerException if filename is null
     * @catch FileNotFoundException if a seed file cant be found
     * @catch InputMisMatchException if a seed file is a bad boi
     * @catch NumberFormatException if seed variable input is a bad boi
     */
    public void minesweeperGame(String seed) {
        try {
            File configFile = new File(seed);
            Scanner seedScan = new Scanner(configFile);
            inputRow = Integer.parseInt(seedScan.next());
            inputCol = Integer.parseInt(seedScan.next());
            if ((inputCol < 5) || (inputRow < 5)) {
                System.out.println();
                System.out.print("Seedfile Value Error: Cannot create a mine field with");
                System.out.println(" that many rows and/or columns!");
                System.exit(3);
            }
            inputNumMines = Integer.parseInt(seedScan.next());
            roundsCompleted = 0;
            mineLocations = new int[inputNumMines][2];
            for (int row = 0; row < mineLocations.length; row++) {
                for (int col = 0; col < mineLocations[row].length; col++) {
                    mineLocations[row][col] = Integer.parseInt(seedScan.next());
                } //col for
            } //row for
            board = new String[this.inputRow][this.inputCol];
            int n = this.board.length;
            double p = Math.ceil(Math.log10(n));
            e = (int)p;
            for (int i = 0; i < this.inputRow; i++) {
                for (int j = 0; j < this.inputCol; j++) {
                    format = " " + "%" + e + "s" + " ";
                    board[i][j] = String.format(format, "");
                } //for2
            } //for1
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            System.err.print("Seedfile Not Found Error: Cannot create game with " + seed +
                             ", because it cannot be be found ");
            System.err.print("or cannot be read due to permission.");
            System.out.println();
            System.exit(1);
        } catch (NullPointerException npe) {
            System.out.println(npe);
            System.err.println("the filename must be non-null");
        } catch (InputMismatchException ime) {
            System.out.println(ime);
            System.err.print("Seedfile Format Error: Cannot create game with" + ime +
                             ", because it is not formatted correctly.");
            System.out.println();
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
            System.err.print("Unable to interpret supplied command-line arguments.");
            System.out.println();
        } //final catch
    } //minesweeperGame


    /**
     * gets all the mines in any square near the one passed in by the user.
     * @param none
     */
    public void getMines() {
        this.nearbyMines = 0;
        if (this.userCol + 1 < this.mineBoard[this.userRow].length) {
            if (this.mineBoard[this.userRow][this.userCol + 1] == true) {
                this.nearbyMines++;
            }
        }
        if (this.userRow + 1 < this.mineBoard.length) {
            if (this.mineBoard[this.userRow + 1][this.userCol] == true) {
                this.nearbyMines++;
            }
        }
        if (this.userRow - 1 >= 0) {
            if (this.mineBoard[this.userRow - 1][this.userCol] == true) {
                this.nearbyMines++;
            }
        }
        if (this.userCol - 1 >= 0) {
            if (this.mineBoard[this.userRow][this.userCol - 1] == true) {
                this.nearbyMines++;
            }
        }
        if ((this.userRow + 1 < this.mineBoard.length) &&
            (this.userCol + 1 < this.mineBoard[this.userRow].length)) {
            if (this.mineBoard[this.userRow + 1][this.userCol + 1] == true) {
                this.nearbyMines++;
            }
        }
        if ((this.userRow + 1 < this.mineBoard.length) &&
            (this.userCol - 1 >= 0)) {
            if (this.mineBoard[this.userRow + 1][this.userCol - 1] == true) {
                this.nearbyMines++;
            }
        }
        if ((this.userRow - 1 >= 0) &&
            (this.userCol + 1 < this.mineBoard[this.userRow].length)) {
            if (this.mineBoard[this.userRow - 1][this.userCol + 1] == true) {
                this.nearbyMines++;
            }
        }
        if ((this.userRow - 1 >= 0) &&
            (this.userCol - 1 >= 0)) {
            if (this.mineBoard[this.userRow - 1][this.userCol - 1] == true) {
                this.nearbyMines++;
            }
        }
    }

    /**
     * method to play the game.
     * @param null
     */
    public void play() {
        this.printWelcome();
        mineBoard = new boolean[inputRow][inputCol];
        for (int row = 0; row < mineLocations.length; row++) {
            for (int col = 0; col < mineLocations[row].length - 1; col++) {
                int r = mineLocations[row][col];
                int c = mineLocations[row][col + 1];
                mineBoard[r][c] = true;
            }
        }
        while (play) {
            this.promptUser();
        }

    }

    /**
     * uses seed information and gameplay updates to build a board based off of an array of stored
     * values.
     * @param null
     */
    public void printMinefield() {
        System.out.println();
        System.out.println("Rounds Completed: " + this.roundsCompleted);
        for (int row = 0; row < this.board.length; row++) {
            System.out.println();
            System.out.printf(format, row);
            System.out.print("|");
            for (int col = 0; col < this.board[row].length; col++) {
                System.out.print(this.board[row][col]);
                System.out.print("|");
            }
        }
        System.out.println();
        System.out.printf(format, "");
        for (int i = 0; i < this.board.length; i++) {
            System.out.print(String.format(format, i));
            System.out.print(" ");
        }
        System.out.println();
    }


    /**
     * prints a temp board to the screen showing all current moves as well as
     * <> surrounding the bombs on the board, regardless of whether the user has
     * found them yet.
     * @param null
     */
    public void showMines() {
        String[][] tempBoard = new String[this.inputRow][this.inputCol];
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[i].length; j++) {
                if ((this.mineBoard[i][j] == true) &&
                    this.board[i][j].equals(String.format(format, ""))) {
                    String temp = "<%" + e + "s>";
                    tempBoard[i][j] = String.format(temp, "");
                } else if (this.mineBoard[i][j] == true &&
                    this.board[i][j].equals(String.format(format, f))) {
                    String temp = "<%" + e + "s>";
                    tempBoard[i][j] = String.format(temp, "F");
                } else if (this.mineBoard[i][j] == true &&
                           this.board[i][j].equals(String.format(format, q))) {
                    String temp = "<%" + e + "s>";
                    tempBoard[i][j] = String.format(temp, q);
                } else {
                    tempBoard[i][j] = this.board[i][j];
                }
            }
        }
        System.out.println();
        System.out.println("Rounds Completed: " + this.roundsCompleted);
        for ( int i = 0; i < tempBoard.length; i++) {
            System.out.println();
            System.out.printf(format, i);
            System.out.print("|");
            for (int j = 0; j < tempBoard[i].length; j++) {
                System.out.print(tempBoard[i][j]);
                System.out.print("|");
            }
        }
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < this.board.length; i++) {
            System.out.print(String.format(format, i));
        }
        System.out.println();
    }

/**
 *  requirements:
 * - all the squares are uncovered (none have just white space).
 * - all the mines are marked with an "f".
 * @return boolean; true if the game has been won, false if it has not
 */
    public boolean isWon() {
        minesMarked = 0;
        emptySquares = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.mineBoard[i][j] == true) {
                    if (this.board[i][j].equals(String.format(format, f))) {
                        this.minesMarked++;
                    }
                }
                if (this.mineBoard[i][j] == false) {
                    if (this.board[i][j].equals(String.format(format, "")) ||
                        this.board[i][j].equals(String.format(format, f)) ||
                        this.board[i][j].equals(String.format(format, q))) {
                        emptySquares++;
                    }
                }
            }
        }
        if ((emptySquares == 0) && (minesMarked == this.inputNumMines)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * marks the user input's space with an f.
     */
    public void markWithF() {
        String str = String.format(format, f);
        this.board[this.userRow][this.userCol] = str;
    } //markWithF

    /**
     * marks the user input's space with a "?".
     */
    public void markWithQ() {
        String str = String.format(format, q);
        this.board[this.userRow][this.userCol] = str;
    }

    /**
     * marks the user input's space with the number of adjacent mines.
     */
    public void markWithNum() {
        this.getMines();
        num = this.nearbyMines;
        String str = String.format(format, num);
        this.board[this.userRow][this.userCol] = str;
    }

    /**
     * prompts the user for their input and designates methods for
     * their output.
     */
    public void promptUser() {
        boolean valid = true;
        Scanner in = new Scanner(System.in);
        this.printMinefield();
        System.out.println();
        System.out.print("minesweeper-alpha: ");
        command = in.next();
        do {
            valid = true;
            if ((command.equalsIgnoreCase("mark") || command.equalsIgnoreCase("m")) &&
                ((in.nextInt() < this.board.length))) {
                this.userRow = in.nextInt();
                this.userCol = in.nextInt();
                this.isLost();
                this.markWithF();
                this.roundsCompleted++;
            } else if ((command.equalsIgnoreCase("guess") || command.equalsIgnoreCase("g")) &&
                       ((in.nextInt() < this.board.length))) {
                this.userRow = in.nextInt();
                this.userCol = in.nextInt();
                this.isLost();
                this.markWithQ();
                this.roundsCompleted++;
            } else if ((command.equalsIgnoreCase("reveal") || command.equalsIgnoreCase("r")) &&
                       ((in.nextInt() < this.board.length))) {
                this.userRow = in.nextInt();
                this.userCol = in.nextInt();
                this.isLost();
                this.markWithNum();
                this.roundsCompleted++;
            } else if (command.equalsIgnoreCase("nofog")) {
                this.showMines();
            } else if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h")) {
                this.printCommandList();
            } else if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q")) {
                System.out.println("Quitting the Game...");
                System.out.println("Bye!");
                System.exit(0);
            } else {
                System.out.println();
                System.out.println("Input Error: Command not recognized!");
                System.out.println();
                valid = false;
                this.promptUser();
            } // else
        } while (!valid);
        if (this.isLost()) {
            this.printLoss();
        } // if isLost
        if (this.isWon()) {
            this.printWin();
        } // if isWon
    } // promptUser

    /**
     * returns {@code true} if the user has hit a mine. returns {@code false} if not.
     * @return true if game is lost, false if not.
     */
    public boolean isLost() {
        if ((this.mineBoard[this.userRow][this.userCol] == true) &&
            (this.command.equalsIgnoreCase("r") ||
             this.command.equalsIgnoreCase("reveal"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * prints out the game over old ascii art banner.
     * @print loser banner.
     */
    public void printLoss() {
        System.out.println("Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/");
        System.exit(0);
    } //printLoss

    /**
     * prints out welcome banner. ONCE!
     * @print welcome banner.
     */
    public void printWelcome() {
        System.out.println("        _");
        System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.print(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\");
        System.out.println("'__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                 A L P H A   E D I T I O N |_| v2020.sp");
    } //printWelcome

    /**
     * prints out command list per wish of the user.
     * @print command list.
     */
    public void printCommandList() {
        System.out.println("Commands Available...");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" -   Mark: m/mark   row col");
        System.out.println(" -  Guess: g/guess  row col");
        System.out.println(" -   Help: h/help");
        System.out.println(" -   Quit: q/quit");
    } //printCommandList

    /**
     * prints out the score.
     * @return a double value of the user's final score.
     */
    public double calculateScore() {
        this.score = 100.0 * this.inputRow * this.inputCol / this.roundsCompleted;
        return this.score;
    } //calculateScore

    /**
     * prints out the winning banner to the user.
     * @print magical winning doge.
     */
    public void printWin() {
        System.out.println();
        System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ " + "So Doge");
        System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
        System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ " + "Such Score");
        System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
        System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ " + "Much Minesweeping");
        System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
        System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ " + "Wow");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
        System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
        System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
        System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
        System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
        System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
        System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
        System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
        System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ " + "CONGRATULATIONS!");
        System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ " + "YOU HAVE WON!");
        System.out.print(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ ");
        System.out.print("SCORE: ");
        System.out.printf("%.2f", this.calculateScore());
        System.out.println();
        System.exit(0);
    } //printWin
} //CLASS
