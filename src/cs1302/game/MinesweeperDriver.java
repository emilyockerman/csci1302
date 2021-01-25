package cs1302.game;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import cs1302.game.MinesweeperGame;

public class MinesweeperDriver{

    //handles command line args
    public static void main(String[] args){
        try {
        String seed = args[1];
        if (args[0].equalsIgnoreCase("--gen")){
                System.err.println("Seedfile generation not supported.");
                System.exit(2);
            }
        Scanner input = null;
        //creates an instance of MSG
        MinesweeperGame ms = new MinesweeperGame();
        ms.minesweeperGame(seed);
        ms.play();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("Unable to interpret supplied command-line arguments.");
            System.exit(1);
        }
    }//MAIN METHOD
}//CLASS
