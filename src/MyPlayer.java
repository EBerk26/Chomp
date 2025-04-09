import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class MyPlayer {
    public Chip[][] gameBoard;
    public int[] columns = new int[10];
    int boardLength = 10;
    ArrayList<ArrayList<Integer>> gameStates = new ArrayList<>();
    int totalGameStates;
    orderedPair[] listOfMoves;
    ArrayList<Integer> gameStatePossibility;
    Object[] arrayVersion;
    orderedPair[] possibleOrderedPairs;
    int totalPossibleMoves;
    int highestY;
    orderedPair leastRemoval;
    int index;
    int numberInFirstColumn;
    Hashtable<ArrayList<Integer>,Boolean> hashtable;
    Hashtable<ArrayList<Integer>,orderedPair> solutions = new Hashtable<>();

    public MyPlayer() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("/Users/eliberk/Desktop/CS2/Chomp/chompSolutions.ser")))) {
            solutions = (Hashtable<ArrayList<Integer>, orderedPair>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
        /***
         * This code will run just once, when the game opens.
         * Add your code here.
         */
    public Point move(Chip[][] pBoard) {
        System.out.println("MyPlayer Move");
        int column = 0;
        int row = 0;

        row = 1;
        column = 1;
        //convert the board into the format that my code knows how to read

        ArrayList<Integer> gameState = new ArrayList<>();
        int n;
        for(int p = 0;p<pBoard.length;p++){
            n=0;
            for(int q = 0;q<pBoard[p].length;q++){
                if(pBoard[p][q].isAlive){
                    n++;
                }
            }
            gameState.add(n);
        }

        orderedPair answer = this.solutions.get(gameState);
        Point point = new Point(answer.x, answer.y);
        return point;



        //check what move is the right one


        /***
         * This code will run each time the "MyPlayer" button is pressed.
         * Add your code to return the row and the column of the chip you want to take.
         * You'll be returning a data type called Point which consists of two integers.
         */
    }
}
class orderedPair implements Serializable{
    int x;
    int y;

    public orderedPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void printInfo() {
        System.out.println("(" + x + "," + y + ")");
    }

    orderedPair flip() {
        //noinspection SuspiciousNameCombination
        return new orderedPair(y, x);
    }
}
