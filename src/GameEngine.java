import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Scanner;

/**
 * A Class to process different actions in game
 *
 * @author The Feij
 * @since 2022.1.20
 */
public class GameEngine {

    //player's score in game
    private int score;


    /**
     * Constructor to create GameEngine
     */
    public GameEngine(){
        score = 0;
    }


    /**
     * A method to get score
     * @return score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * A method to start a new map for player
     * @param candies matrix of candies in map
     */
    public void startMap(Candy[][] candies){
        fill(candies);
        while (true){
            adjustMap(candies);
            fill(candies);
            remove(candies);

            boolean isMapFull = true;
            for(int i = 0 ; i < 10 ; i++){
                for(int j = 0 ; j < 10 ; j++){
                    if(candies[i][j].isDestroyed())
                        isMapFull = false;
                }
            }
            if (isMapFull)
                break;
        }
        score = 0;
    }

    /**
     * A method to fill map with simple candies
     * @param candies matrix of candies in map
     */
    private void fill(Candy[][] candies){
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                if(candies[i][j] == null){
                    candies[i][j] = getRandomCandy();
                    candies[i][j].setType("SC");
                    candies[i][j].setImage();
                    continue;
                }
                if(candies[i][j].isDestroyed()){
                    candies[i][j] = getRandomCandy();
                }
                candies[i][j].setType("SC");
                candies[i][j].setImage();
            }
        }
    }

    /**
     * a method to read/fill map from file
     * @param candies matrix of candies in map
     * @param fileAddress address of the file
     */
    public void fillMapFromFile(Candy[][] candies, String fileAddress){
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(fileAddress));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        score = Integer.parseInt(scanner.nextLine());

        for (int i = 0 ; i < 10 ; i++){
            String line = scanner.nextLine();
            int index = 0;
            for (int j = 0 ; j < 10 ; j++){
                Candy newCandy = new Candy();
                if(line.charAt(index) == 'R'){
                    newCandy.setColor("Red");
                }
                else if(line.charAt(index) == 'B'){
                    newCandy.setColor("Blue");
                }
                else if(line.charAt(index) == 'Y'){
                    newCandy.setColor("Yellow");
                }
                else if(line.charAt(index) == 'G'){
                    newCandy.setColor("Green");
                }

                newCandy.setType(line.substring(index + 1, index + 3));
                newCandy.setPoints();
                newCandy.setImage();
                newCandy.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                candies[i][j] = newCandy;
                index += 3;
                while (true){
                    if(line.length() == index)
                        break;
                    if(line.charAt(index) == '\n')
                        break;
                    if(line.charAt(index) == ',' || line.charAt(index) == ' ')
                        index++;
                    else
                        break;
                }

            }

        }

        scanner.close();
    }

    /**
     * A method to get a random candy
     * @return a new random candy created by this method
     */
    public Candy getRandomCandy(){
        SecureRandom randomGenerator = new SecureRandom();
        int randNum = randomGenerator.nextInt(4);
        Candy newCandy = new Candy();
        if(randNum == 0){
            newCandy.setColor("Red");
        }
        else if(randNum == 1){
            newCandy.setColor("Blue");
        }
        else if(randNum == 2){
            newCandy.setColor("Yellow");
        }
        else if(randNum == 3){
            newCandy.setColor("Green");
        }

        randNum = randomGenerator.nextInt(5);
        if(randNum < 4){
            newCandy.setType("SC");
        }
        else if(randNum == 4){
            randNum = randomGenerator.nextInt(5);
            if(randNum <= 1)
                newCandy.setType("LR");
            else if(randNum <= 3)
                newCandy.setType("LC");
            else if(randNum == 4)
                newCandy.setType("RC");

        }
        newCandy.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        newCandy.setImage();
        return newCandy;
    }

    /**
     * a method get a hint in game
     * @param candies matrix of candies in map
     * @return return true if there is a possible move
     * in game and marks the candies to be moved,
     * otherwise returns false
     */
    public boolean findHint(Candy[][] candies){
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                if(i < 9){
                    if(isMovePossible(i, j, i + 1, j, candies)){
                        candies[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                        candies[i + 1][j].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                        return true;
                    }
                }
                if(j < 9){
                    if(isMovePossible(i, j, i, j + 1, candies)){
                        candies[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                        candies[i][j + 1].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * A method to check if a certain move is possible or not
     * @param i1 x position of first candy
     * @param j1 y position of first candy
     * @param i2 x position of second candy
     * @param j2 y position of second candy
     * @param candies matrix of candies in map
     * @return true, if it's possible
     */
    public boolean isMovePossible(int i1, int j1, int i2, int j2, Candy[][] candies){
        Candy temp = candies[i1][j1];
        candies[i1][j1] = candies[i2][j2];
        candies[i2][j2] = temp;

        boolean result;

        result = areMoreThan3Candies(i1, j1, candies) != null
                    || areMoreThan3Candies(i2, j2, candies) != null;

        temp = candies[i1][j1];
        candies[i1][j1] = candies[i2][j2];
        candies[i2][j2] = temp;

        return result;
    }

    /**
     * a method to check if there are more than 2 candies of same color
     * in a row or column near a given candy
     * @param i x position of candy
     * @param j y position of candy
     * @param candies matrix of candies in map
     * @return arraylist of candies in a row or column
     */
    public ArrayList<Candy> areMoreThan3Candies(int i, int j, Candy[][] candies){

        ArrayList<Candy> cCandies = new ArrayList<>();

        int index = i - 1;
        while (index >= 0){
            if(candies[i][j].isDestroyed() || candies[index][j].isDestroyed())
                break;
            if(candies[i][j].getColor().equals(candies[index][j].getColor()))
                cCandies.add(candies[index][j]);
            else
                break;
            index--;
        }
        Collections.reverse(cCandies);
        cCandies.add(candies[i][j]);
        index = i + 1;
        while (index <= 9){
            if(candies[i][j].isDestroyed() || candies[index][j].isDestroyed())
                break;
            if(candies[i][j].getColor().equals(candies[index][j].getColor()))
                cCandies.add(candies[index][j]);
            else
                break;
            index++;
        }


        ArrayList<Candy> rCandies = new ArrayList<>();
        index = j - 1;
        while (index >= 0){
            if(candies[i][j].isDestroyed() || candies[i][index].isDestroyed())
                break;
            if(candies[i][j].getColor().equals(candies[i][index].getColor()))
                rCandies.add(candies[i][index]);
            else
                break;
            index--;
        }
        Collections.reverse(rCandies);
        rCandies.add(candies[i][j]);
        index = j + 1;
        while (index <= 9){
            if(candies[i][j].isDestroyed() || candies[i][index].isDestroyed())
                break;
            if(candies[i][j].getColor().equals(candies[i][index].getColor()))
                rCandies.add(candies[i][index]);
            else
                break;
            index++;
        }

        Collections.reverse(cCandies);
        if(cCandies.size() >= 3 || rCandies.size() >= 3){
            if(cCandies.size() > rCandies.size())
                return cCandies;
            else
                return rCandies;
        }
        else
            return null;

    }

    /**
     * A method to remove a random group of candies with same color
     * @param candies matrix of candies in map
     */
    public void remove(Candy[][] candies){
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                ArrayList<Candy> toBeRemoved = areMoreThan3Candies(i, j, candies);
                if(toBeRemoved != null){
                    remove(candies, toBeRemoved);
                }
            }
        }
    }

    /**
     * A method to remove a certian group of candies with same color
     * @param candies matrix of candies in map
     * @param toBeRemoved list of candies to be removed
     */
    private void remove(Candy[][] candies, ArrayList<Candy> toBeRemoved){
        for (Candy candy : toBeRemoved){

            if(candy.getType().equals("SC") && !candy.isDestroyed()){
                candy.setDestroyed();
                score += candy.getPoints();
            }
            else {
                int i1, j1;
                i1 = j1 = -1;
                for (int i = 0 ; i < 10 ; i++){
                    for(int j  = 0 ; j < 10 ; j++){
                        if(candy.equals(candies[i][j])){
                            i1 = i;
                            j1 = j;
                        }
                    }
                }
                if(candy.getType().equals("LR")){
                    for(int j = 0 ; j < 10 ; j++){
                        if(!toBeRemoved.contains(candies[i1][j]) && !candy.isDestroyed()){
                            candies[i1][j].setDestroyed();
                            score += candies[i1][j].getPoints();
                        }
                    }
                }
                else if(candy.getType().equals("LC")){
                    for(int i = 0 ; i < 10 ; i++){
                        if(!toBeRemoved.contains(candies[i][j1]) && !candy.isDestroyed()){
                            candies[i][j1].setDestroyed();
                            score += candies[i][j1].getPoints();
                        }
                    }
                }
                else if(candy.getType().equals("RC")){
                    for (int i = 0 ; i < 10 ; i++){
                        for(int j  = 0 ; j < 10 ; j++){
                            if(Math.abs(i1 - i) <= 2 && Math.abs(j1 - j) <= 2){
                                if(!toBeRemoved.contains(candies[i][j]) && !candy.isDestroyed()){
                                    candies[i][j].setDestroyed();
                                    score += candies[i][j].getPoints();
                                }
                            }
                        }
                    }
                }

                candy.setDestroyed();
                score += candy.getPoints();

            }

        }
        if(toBeRemoved.size() == 4){
            toBeRemoved.get(0).setEnabled(true);
            toBeRemoved.get(0).setDestroyed(false);
            SecureRandom random = new SecureRandom();
            int rand = random.nextInt(2);
            if(rand == 0)
                toBeRemoved.get(0).setType("LR");
            else
                toBeRemoved.get(0).setType("LC");
            toBeRemoved.get(0).setImage();
        }
        else if(toBeRemoved.size() >= 5){
            toBeRemoved.get(0).setEnabled(true);
            toBeRemoved.get(0).setDestroyed(false);
            toBeRemoved.get(0).setType("RC");
            toBeRemoved.get(0).setImage();
        }
    }

    /**
     * A method to adjust map, in other words fill free spaces in map, caused
     * by removed candies with upper candies if possible
     * @param candies matrix of candies in map
     */
    public void adjustMap(Candy[][] candies){
        for(int i = 9 ; i >= 0 ; i--){
            for(int j = 9 ; j >= 0 ; j--){
                if(candies[i][j].isDestroyed()){
                    int index = i - 1;
                    while (index >= 0){
                        if(!candies[index][j].isDestroyed()){
                            Candy temp = candies[i][j];
                            candies[i][j] = candies[index][j];
                            candies[index][j] = temp;
                            break;
                        }
                        index--;
                    }
                }
            }
        }
    }

    /**
     * A method used to fill the map during the game
     * @param candies matrix of candies in map
     */
    public void refillMap(Candy[][] candies) {
        for (int i = 9 ; i >= 0 ; i--){
            for(int j = 9 ; j >= 0 ; j--){
                if(candies[i][j].isDestroyed()){
                    Candy candy = getRandomCandy();
                    candies[i][j] = candy;
                }
            }
        }
    }

    /**
     * A method to save result of the game
     */
    public void saveResults(){
        ArrayList<Integer> scores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader("ScoreBoard.txt"));
            while (scanner.hasNext()){
                scores.add(scanner.nextInt());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        scores.add(score);
        Collections.sort(scores);
        Collections.reverse(scores);

        try {
            Formatter formatter = new Formatter(new FileWriter("ScoreBoard.txt"));
            for(int i = 0 ; i < 5 && i < scores.size() ; i++)
                formatter.format("%d\n", scores.get(i));
            formatter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
