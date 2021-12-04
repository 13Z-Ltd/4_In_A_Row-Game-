/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Balla
 */
public class CPU_ValueChecker extends CPU_Modes {
    int numberOfSteps;
    int coinsCounter;
    int possibleCoinsNumber;
    int selectedColumn;
    Coin lastCoin, currentCoin;
    Coin[] lops;
    Coin[] playerLops;
    String importentInfos;
    
    public ArrayList<Coin> bestStepsList;
    
    public CPU_ValueChecker(Coin[][] gameSpace, Coin[] listOfPossibleSteps) {
        cgs = gameSpace;
        lops = listOfPossibleSteps;
        numberOfSteps = 0;
        selectedColumn = 0;
        lastCoin = null;
        
        playerLops = new Coin[7];
        bestStepsList = new ArrayList<>();
    }

    @Override
    protected Coin cpuNextCoin() {
        if (numberOfSteps == 0) {
            int startColumn = (int) ((Math.random() * 3) + 2);
            numberOfSteps++;
            lastCoin = new Coin(searchRow(startColumn), startColumn, Colors.red);
            return lastCoin;
        }
        if (numberOfSteps == 1) {
            int direction = giveRandomDirection(3);
            if (lastCoin.column == 2) {
                if (direction == -1 && (lastCoin.row == 0 || cgs[0][3] != null))
                    direction = giveRandomDirection(2);
                if (direction == 0 && cgs[1][2] != null)
                    direction = 1;
                if (direction == 1 && cgs[1][3] != null)
                    direction = 0;
            }
            if (lastCoin.column == 4) {
                //if (direction == 1) {
                if (direction == 1 && (lastCoin.row == 0 || cgs[0][3] != null)) 
                    direction = (giveRandomDirection(2) - 1);
                
                if (direction == 0 && cgs[1][4] != null)
                    direction = -1;
                if (direction == -1 && cgs[1][3] != null)
                    direction = 0;
            }
            numberOfSteps++;
            lastCoin = new Coin(searchRow(lastCoin.column + direction), lastCoin.column + direction, Colors.red);
            return lastCoin;            
        }
        //Döntési mechanizmus
        //searchForTheBestDrop();
        lastCoin = new Coin(searchRow(selectedColumn), selectedColumn, Colors.red);
        return lastCoin;
    }
    
    public void searchForTheBestDrop() {
        int biggestValue = 0;
        int biggestAbsoluteValue = 0;
        int playerBiggestValue = 0;
        int randomColumn = 0;
        
        for (int i = 0; i < 7; i++) {
            if (biggestValue < lops[i].coinValue)
                biggestValue = lops[i].coinValue;
            
            if (biggestAbsoluteValue < lops[i].absoluteValue)
                biggestAbsoluteValue = lops[i].absoluteValue;
            
            if (playerBiggestValue < playerLops[i].coinValue)
                playerBiggestValue = playerLops[i].coinValue;
        }
        bestStepsList.clear();
        for (int i = 0; i < 7; i++) {
            if (biggestValue == lops[i].coinValue)
                bestStepsList.add(lops[i]);
        }
        
        importentInfos = "";
        
        if (biggestValue < 3 && playerBiggestValue > 2) {
            importentInfos += "VÉDEKEZÉÉÉÉS!!! \n";
            bestStepsList.clear();
            for (int i = 0; i < 7; i++) {
                if (playerBiggestValue == playerLops[i].coinValue)
                    bestStepsList.add(playerLops[i]);
            }
            if (bestStepsList.size() > 1) {
                importentInfos += bestStepsList.size() + " lehetősége van!\n";

                randomColumn = (int) (Math.random() * bestStepsList.size());
                selectedColumn = bestStepsList.get(randomColumn).column;
                importentInfos += "A választott oszlop:" + selectedColumn + "\n";
            } else {
                importentInfos += "A veszélyes oszlop: " + bestStepsList.get(0).column + "\n";
                selectedColumn = bestStepsList.get(0).column;
            }
        }
        else {
            if (bestStepsList.size() > 1) {
                importentInfos += bestStepsList.size() + " lehetőség közül választunk.\n";

                for (int i = 0; i < bestStepsList.size(); i++) {
                    importentInfos += "" + bestStepsList.get(i).column + ", ";
                }
                
                importentInfos += "A lehetőségek abszoluteértékei: \n";
                for (int i = 0; i < bestStepsList.size(); i++) {
                    importentInfos += "" + bestStepsList.get(i).absoluteValue + ", ";
                }
                
                importentInfos += "\n";
                
                //Abszolute érték legjobbjai körül választás
                bestStepsList.clear();
                for (int i = 0; i < 7; i++) {
                    if (biggestAbsoluteValue == lops[i].absoluteValue)
                    bestStepsList.add(lops[i]);
                }
                randomColumn = (int) (Math.random() * bestStepsList.size());
                selectedColumn = bestStepsList.get(randomColumn).column;
                importentInfos += "A random választott oszlop:" + selectedColumn + "\n";
                
            } else {
                importentInfos += "A legjobb oszlop: " + bestStepsList.get(0).column + "\n";
                selectedColumn = bestStepsList.get(0).column;
            }
        }
    }
    
    public void checkingLOPS() {
        for (int i = 0; i < 7; i++) {
            int rowNumber = searchRow(i);
            if (rowNumber != -1) {
                lops[i] = new Coin(rowNumber, i, Colors.red);
                //lops[i] = new Coin(rowNumber, i, Colors.yellow);
                currentCoin = lops[i];
                setCoinPossibleValues(Colors.red);
            } else {
                lops[i].coinValue = -1;
                lops[i].absoluteValue = -1;
            }
        }
    }
    
    public void checkingPlayerLOPS() {
        for (int i = 0; i < 7; i++) {
            int rowNumber = searchRow(i);
            if (rowNumber != -1) {
                playerLops[i] = new Coin(rowNumber, i, Colors.yellow);
                currentCoin = playerLops[i];
                setCoinPossibleValues(Colors.yellow);
            } else {
                playerLops[i].coinValue = -1;
            }
        }
    }
    
    public void setCoinPossibleValues(Colors colorToCheck) {
        currentCoin.right_up = checkingNeighborsNumber(1, 1, colorToCheck);
            currentCoin.coinValue = currentCoin.right_up;
        currentCoin.right = checkingNeighborsNumber(0, 1, colorToCheck);
            if (currentCoin.coinValue < currentCoin.right)
                currentCoin.coinValue = currentCoin.right;       
        currentCoin.right_down = checkingNeighborsNumber(-1, 1, colorToCheck);
            if (currentCoin.coinValue < currentCoin.right_down)
                currentCoin.coinValue = currentCoin.right_down;
        currentCoin.down = checkingNeighborsNumber(-1, 0, colorToCheck);
            if (currentCoin.coinValue < currentCoin.down)
                currentCoin.coinValue = currentCoin.down;
        currentCoin.left_down = checkingNeighborsNumber(-1, -1, colorToCheck);
            if (currentCoin.coinValue < currentCoin.left_down)
                currentCoin.coinValue = currentCoin.left_down;
        currentCoin.left = checkingNeighborsNumber(0, -1, colorToCheck);
            if (currentCoin.coinValue < currentCoin.left)
                currentCoin.coinValue = currentCoin.left;
        currentCoin.left_up = checkingNeighborsNumber(1, -1, colorToCheck);
            if (currentCoin.coinValue < currentCoin.left_up)
                currentCoin.coinValue = currentCoin.left_up;
            
        if (currentCoin.coinValue < currentCoin.left + currentCoin.right) 
            currentCoin.coinValue = currentCoin.left + currentCoin.right;
        if (currentCoin.coinValue < currentCoin.left_down + currentCoin.right_up) 
            currentCoin.coinValue = currentCoin.left_down + currentCoin.right_up;
        if (currentCoin.coinValue < currentCoin.left_up + currentCoin.right_down) 
            currentCoin.coinValue = currentCoin.left_up + currentCoin.right_down;
        
        if (currentCoin.right_up > 0)
            currentCoin.absoluteValue += currentCoin.right_up;
        if (currentCoin.right > 0)
            currentCoin.absoluteValue += currentCoin.right;
        if (currentCoin.right_down > 0)
            currentCoin.absoluteValue += currentCoin.right_down;
        if (currentCoin.down > 0)
            currentCoin.absoluteValue += currentCoin.down;
        if (currentCoin.left_down > 0)
            currentCoin.absoluteValue += currentCoin.left_down;
        if (currentCoin.left > 0)
            currentCoin.absoluteValue += currentCoin.left;
        if (currentCoin.left_up > 0)
            currentCoin.absoluteValue += currentCoin.left_up;
    }
    
    public int checkingNeighborsNumber(int x, int y, Colors colorToCheck) {
        coinsCounter = 0;
        recursiveCounter(currentCoin, x, y, colorToCheck);
        
        if (colorToCheck == Colors.red && y != 0) {
            int positiveDirection, negativeDirection;
            
            possibleCoinsNumber = 0;
            recursivePossibleCoinsCounter(currentCoin.row, currentCoin.column, x, y);
            positiveDirection = possibleCoinsNumber;
            
            possibleCoinsNumber = 0;
            recursivePossibleCoinsCounter(currentCoin.row, currentCoin.column, -x, -y);
            negativeDirection = possibleCoinsNumber;
            
            if ((positiveDirection + negativeDirection) < 3 ) {
                coinsCounter = -1;
            }
        }
        return coinsCounter;
    }
    
    public void recursiveCounter(Coin coin, int x, int y, Colors colorToCheck) {
        if (coin.row + x > -1 && coin.row + x < 6 && coin.column + y < 7 && coin.column + y > -1 
                && cgs[coin.row + x][coin.column + y] != null && cgs[coin.row + x][coin.column + y].color == colorToCheck) {
                //&& cgs[coin.row + x][coin.column + y] != null && cgs[coin.row + x][coin.column + y].color == Colors.red) {
            coinsCounter++;
            recursiveCounter(cgs[coin.row + x][coin.column + y], x, y, colorToCheck);
        }
    }
    
    //public void recursivePossibleCoinsCounter(Coin coin, int x, int y) {
    public void recursivePossibleCoinsCounter(int currentX, int currentY, int x, int y) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == Colors.yellow) {
                
            } else {
                possibleCoinsNumber++;
                recursivePossibleCoinsCounter(currentX + x, currentY + y, x, y);
            }
        }
    }
    
    public int giveRandomDirection(int directions) {
        int randomDirection = (int) (Math.random() * directions) - 1;
        if (directions == 2)
            randomDirection++;
        return randomDirection;
    }

    @Override
    protected String getInfo() {
        checkingLOPS();
        //return "A legjobb oszlopok számai: " + importentInfos + "!\n";
        return importentInfos;
    }

    @Override
    protected String getInfoBeforeStep() {
        if (numberOfSteps > 1) {
            checkingLOPS();
            //
            checkingPlayerLOPS();
            //
            searchForTheBestDrop();
            return importentInfos;
        }
        checkingLOPS();
        return "";
        //return "Nincs elég lépés!\n";
    }

    @Override
    protected void paintModelHints(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12));      
        for (int i = 0; i < 7; i++) {
            if (playerLops[i] != null) {
                /*
                g2d.drawString("("+ playerLops[i].left + ")", 200 + i * 100, 730);
                g2d.drawString("("+ playerLops[i].left_down + ")", 210 + i * 100, 745);
                g2d.drawString("("+ listOfPossibleSteps[i].down + ")", 220 + i * 100, 660 - listOfPossibleSteps[i].row * 80);
                g2d.drawString("("+ listOfPossibleSteps[i].right_down + ")", 230 + i * 100, 645 - listOfPossibleSteps[i].row * 80);
                g2d.drawString("("+ listOfPossibleSteps[i].right + ")", 240 + i * 100, 630 - listOfPossibleSteps[i].row * 80);
                g2d.drawString("("+ listOfPossibleSteps[i].left_up + ")", 210 + i * 100, 618 - listOfPossibleSteps[i].row * 80);
                g2d.drawString("("+ listOfPossibleSteps[i].right_up + ")", 230 + i * 100, 618 - listOfPossibleSteps[i].row * 80);
                */
                g2d.setColor(Color.red);
                g2d.drawString("("+ playerLops[i].coinValue + ")", 220 + i * 100, 700); //- playerLops[i].row * 80);
            }
        }
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12));      
        for (int i = 0; i < 7; i++) {
            g2d.setColor(Color.white);
            if (lops[i] != null) {
                g2d.drawString("("+ lops[i].left + ")", 200 + i * 100, 630 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].left_down + ")", 210 + i * 100, 645 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].down + ")", 220 + i * 100, 660 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].right_down + ")", 230 + i * 100, 645 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].right + ")", 240 + i * 100, 630 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].left_up + ")", 210 + i * 100, 618 - lops[i].row * 80);
                g2d.drawString("("+ lops[i].right_up + ")", 230 + i * 100, 618 - lops[i].row * 80);
            
                g2d.setColor(Color.red);
                g2d.drawString("("+ lops[i].coinValue + ")", 220 + i * 100, 605 - lops[i].row * 80);
            }
        }
        
    }

    @Override
    protected void makeChangesInTheModel(int x) {
        
    }
    
}
