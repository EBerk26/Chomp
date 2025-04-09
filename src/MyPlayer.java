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
        //file reading version
        /*try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("/Users/eliberk/Desktop/CS2/Chomp/chompSolutions.ser")))) {
            solutions = (Hashtable<ArrayList<Integer>, orderedPair>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();*/

        listGameStates(new ArrayList<Integer>(),boardLength);
        totalGameStates = gameStates.size();
        hashtable = new Hashtable<>();
        hashtable.put(gameStates.get(0),true);
        hashtable.put(gameStates.get(1),false);
        listOfMoves = new orderedPair[totalGameStates];
        listOfMoves[0] = new orderedPair(-1,-1);
        listOfMoves[1] = new orderedPair(0,0);
        for(int x=2; x<totalGameStates;x++){
            if(listOfMoves[x] == null) {
                listOfMoves[x] = chooseBestMove(gameStates.get(x));
            }
        }
        for(int x = 0; x<listOfMoves.length;x++){
            solutions.put(gameStates.get(x),listOfMoves[x]);
        }
    }
    boolean canMakeV(ArrayList<Integer> gameState){
        if(gameState.get(1)<2){
            return false;
        }
        numberInFirstColumn = gameState.get(0);
        for (int x =0;x<gameState.size();x++){
            if(x<=numberInFirstColumn-1){
                if(gameState.get(x)<1){
                    return false;
                }
            } else {
                if (gameState.get(x)!=0){
                    return false;
                }
            }
        }
        return true;
    }

    void listGameStates(ArrayList<Integer> previousColumns, int max) {
        if (previousColumns.size() == boardLength) {
            gameStates.add(previousColumns);
        } else {
            for (int x = 0; x <= max; x++) {
                ArrayList<Integer> nextColumns = new ArrayList<>(previousColumns);
                nextColumns.add(x);
                listGameStates(nextColumns, x);
            }
        }
    }
    orderedPair chooseBestMove(ArrayList<Integer> gameState) {
        if(canMakeV(gameState)){
            hashtable.put(gameState,true);
            return new orderedPair(1,1);
        }
        totalPossibleMoves = 0;
        for(int i: gameState){
            totalPossibleMoves+=i;
        }
        possibleOrderedPairs = new orderedPair[totalPossibleMoves];
        int i =0;
        for (int x = 0; x < gameState.size(); x++) {
            for (int y = 0; y< gameState.get(x); y++) {
                possibleOrderedPairs[i] = new orderedPair(x,y);
                i++;
            }
        }
        for (orderedPair c : possibleOrderedPairs) {
            gameStatePossibility = new ArrayList<>(gameState);
            arrayVersion = gameStatePossibility.toArray();
            for (int x = c.x; x < gameState.size(); x++) {
                if ((int) (arrayVersion[x]) >= c.y) {
                    arrayVersion[x] = c.y;
                }
            }
            if(!hashtable.get(new ArrayList<>(Arrays.asList(arrayVersion)))){//if the choice has been previously marked as a loss state, play it and mark this one as a win.
                hashtable.put(gameState,true);
                //System.out.println("Solved: Game State "+(whichGameState+1)+"/"+totalGameStates+" total game states.");
                //go through and find any game state larger than this where the same move brings you to the same place
               /*for(int x = whichGameState+1;x<gameStates.size();x++){
                   if(listOfMoves[x]!=null){
                       if(moveGivesSameResult(c,gameStates.get(x))){
                           listOfMoves[x] = c;
                       }
                   }
               }*/ //this slows you down because of the searching
                //mirrorImage(gameState,c,true); this also slows you down
                return c;
            }
        }
        hashtable.put(gameState,false);
        //play the move that is first, highest, and second, furthest to the right - this basically removes the fewest possible chips if you know you're going to lose
        highestY = gameState.get(0);
        for (int x = 0;x<gameState.size();x++){
            if(gameState.get(x)<highestY){
                //System.out.println("Solved: Game State "+(whichGameState+1)+"/"+totalGameStates+" total game states.");
                leastRemoval = new orderedPair(x-1,highestY-1);
                return leastRemoval;
            }
        }
        //System.out.println("Solved: Game State "+(whichGameState+1)+"/"+totalGameStates+" total game states.");
        return new orderedPair(0,0);
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
