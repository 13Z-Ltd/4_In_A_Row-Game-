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
import java.util.Iterator;

/**
 *
 * @author Balla
 */
public class CPU_MinMaxAlgorithm extends CPU_Modes {
    public int numberOfSteps;
    public Coin currentCoin;
    public int fieldValueSum;
    public int possibleCoinsNumber;
    public int brosNumber;
    public int directBrosNumber;
    
    public int currentMinimum;
    
    public int[] cpuValuesList;
    public int[] playerValuesList;
    public int[] actualValuesList;
    public ArrayList<Coin> cpuCoinsList;
    public ArrayList<Coin> playerCoinsList;
    
    public String informations;
    
    public CPU_MinMaxAlgorithm(Coin[][] gameField) {
        cgs = gameField;
        numberOfSteps = 0;
        
        cpuValuesList = new int[7];
        playerValuesList = new int[7];
        actualValuesList = new int[7];
        cpuCoinsList = new ArrayList<>();
        playerCoinsList = new ArrayList<>();
    }

    @Override
    protected Coin cpuNextCoin() {
        if (numberOfSteps == 0) {
            int startColumn = (int) ((Math.random() * 5) + 1);
            numberOfSteps++;
            currentCoin = new Coin(searchRow(startColumn), startColumn, Colors.red);
            cpuCoinsList.add(currentCoin);
            return currentCoin;
        }
        
        //Decision-Making
        if (numberOfSteps == 1) {
            int returnIndex = 0;
            int yellowIndex = 0;
            while (cgs[0][yellowIndex] == null || cgs[0][yellowIndex].color != Colors.yellow) {
                yellowIndex++;
            }
            if (yellowIndex > 0 && yellowIndex < 5 && cgs[0][yellowIndex-1] == null &&
                    cgs[0][yellowIndex + 1] != null && cgs[0][yellowIndex + 1].color == Colors.yellow && cgs[0][yellowIndex + 2] == null ) {
                if (yellowIndex < 4) {
                    returnIndex = yellowIndex + 2;                
                } else {
                    returnIndex = yellowIndex - 1;                
                }
            } else {          
                int max = 0;
                returnIndex = 0;
                for (int i = 0; i < 7; i++) {
                    if (cpuValuesList[i] > max) {
                        max = cpuValuesList[i];
                        returnIndex = i;
                    }
                }
            }
            
            numberOfSteps++;
            currentCoin = new Coin(searchRow(returnIndex), returnIndex, Colors.red);
            cpuCoinsList.add(currentCoin);
            return currentCoin;
            
        }

        if (numberOfSteps > 1) {
            //fillMaxValueList();
            
            //Faja! Egész Faja!!!
            int max = 0;
            int index = -1;
            for (int i = 0; i < 7; i++) {
                if (cpuValuesList[i] > max && (cpuValuesList[i] > 1000 || playerValuesList[i] > -1000)) {
                    max = cpuValuesList[i];
                    index = i;
                }
            }
            
            if (index == -1) {            
                max = 0;
                index = 0;
                for (int i = 0; i < 7; i++) {
                    if (cpuValuesList[i] > max) {
                        max = cpuValuesList[i];
                        index = i;
                    }
                }
            }
                        
            numberOfSteps++;
            currentCoin = new Coin(searchRow(index), index, Colors.red);
            cpuCoinsList.add(currentCoin);
            return currentCoin;
        }
        
        return null;
        
    }
    
    public void fillMaxValueList() {
        int currentColumn = 0;
        int currentRow = 0;
        
        informations = "";
        //Listing the player's coins
        playerCoinsList.clear();
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if(cgs[y][x] != null && cgs[y][x].color == Colors.yellow)
                    playerCoinsList.add(cgs[y][x]);
            }
        }
        
        for (int i = 0; i < 7; i++) {
            if (cgs[5][i] == null) {        
                currentColumn = i;
                currentRow = searchRow(currentColumn);
                
                //hozzáadni egy coint
                cgs[currentRow][currentColumn] = new Coin(currentRow, currentColumn, Colors.red);
                cpuCoinsList.add(cgs[currentRow][currentColumn]);

                //kiszámolás
                fieldValueSum = 0;
                for (Iterator<Coin> iterator = cpuCoinsList.iterator(); iterator.hasNext();) {
                    Coin coin = iterator.next();
                    coin.SUM = 0;                    
                    
                    coin.H = calculateCoinNumbers(coin, 0, 1, Colors.red);
                    coin.SUM += coin.H;
                    coin.V = calculateCoinNumbers(coin, 1, 0, Colors.red);
                    coin.SUM += coin.V;
                    coin.NE = calculateCoinNumbers(coin, 1, 1, Colors.red);
                    coin.SUM += coin.NE;            
                    coin.NW = calculateCoinNumbers(coin, 1, -1, Colors.red);
                    coin.SUM += coin.NW;

                    fieldValueSum += coin.SUM;
                }
                cpuValuesList[i] = fieldValueSum;
                
                //Törlés elött megnézzük ebben a helyzetben a lehetséges minuszokat
                fillMinValueList();
                playerValuesList[i] = currentMinimum;                
                
                cpuCoinsList.remove(cpuCoinsList.size()-1);
                cgs[currentRow][currentColumn] = null;
            } else {
                cpuValuesList[i] = -1;
            }
        }
        informations += "\n";
    }
    
    public void fillMinValueList() {
        int currentColumn = 0;
        int currentRow = 0;
        
        for (int i = 0; i < 7; i++) {
            if (cgs[5][i] == null) {        
                currentColumn = i;
                currentRow = searchRow(currentColumn);                
                //hozzáadni egy coint
                cgs[currentRow][currentColumn] = new Coin(currentRow, currentColumn, Colors.yellow);
                playerCoinsList.add(cgs[currentRow][currentColumn]);

                //kiszámolás
                fieldValueSum = 0;
                for (Iterator<Coin> playerIterator = playerCoinsList.iterator(); playerIterator.hasNext();) {
                    Coin coin = playerIterator.next();
                    coin.SUM = 0;                    
                    
                    coin.H = -calculateCoinNumbers(coin, 0, 1, Colors.yellow);
                    coin.SUM += coin.H;
                    coin.V = -calculateCoinNumbers(coin, 1, 0, Colors.yellow);
                    coin.SUM += coin.V;
                    coin.NE = -calculateCoinNumbers(coin, 1, 1, Colors.yellow);
                    coin.SUM += coin.NE;            
                    coin.NW = -calculateCoinNumbers(coin, 1, -1, Colors.yellow);
                    coin.SUM += coin.NW;

                    fieldValueSum += coin.SUM;
                }
                actualValuesList[i] = fieldValueSum;
                                
                playerCoinsList.remove(playerCoinsList.size()-1);
                cgs[currentRow][currentColumn] = null;
            } else {
                actualValuesList[i] = 1;
            }
        }
            int min = 0;
            int minIndex = 0;
            for (int j = 0; j < 7; j++) {
                if (actualValuesList[j] < min) {
                    min = actualValuesList[j];
                    minIndex = j;
                }
            }
            currentMinimum = min;
            informations += currentMinimum + ", ";
    }
    
    /*
    public void fillMinValueList() {
        int currentColumn = 0;
        int currentRow = 0;
        //Listing the player's coins
        playerCoinsList.clear();
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if(cgs[y][x] != null && cgs[y][x].color == Colors.yellow)
                    playerCoinsList.add(cgs[y][x]);
            }
        }
        
        for (int i = 0; i < 7; i++) {
            if (cgs[5][i] == null) {        
                currentColumn = i;
                currentRow = searchRow(currentColumn);                
                //hozzáadni egy coint
                cgs[currentRow][currentColumn] = new Coin(currentRow, currentColumn, Colors.yellow);
                playerCoinsList.add(cgs[currentRow][currentColumn]);

                //kiszámolás
                fieldValueSum = 0;
                for (Iterator<Coin> iterator = playerCoinsList.iterator(); iterator.hasNext();) {
                    Coin coin = iterator.next();
                    coin.SUM = 0;                    
                    
                    coin.H = -calculateCoinNumbers(coin, 0, 1, Colors.yellow);
                    coin.SUM += coin.H;
                    coin.V = -calculateCoinNumbers(coin, 1, 0, Colors.yellow);
                    coin.SUM += coin.V;
                    coin.NE = -calculateCoinNumbers(coin, 1, 1, Colors.yellow);
                    coin.SUM += coin.NE;            
                    coin.NW = -calculateCoinNumbers(coin, 1, -1, Colors.yellow);
                    coin.SUM += coin.NW;

                    fieldValueSum += coin.SUM;
                }
                playerValuesList[i] = fieldValueSum;  
                
                playerCoinsList.remove(playerCoinsList.size()-1);
                cgs[currentRow][currentColumn] = null;
            } else {
                playerValuesList[i] = 1;
            }
        }
    }
    */
    public int calculateCoinNumbers(Coin coin, int x, int y, Colors color) {
        int positive = 0;
        int negative = 0;
        brosNumber = 1;
        possibleCoinsNumber = 0;
        
        recursivePossibleCoinsCounter(coin.row, coin.column, x, y, color);
        positive = possibleCoinsNumber;
        possibleCoinsNumber = 0;
        recursivePossibleCoinsCounter(coin.row, coin.column, -x, -y, color);
        negative = possibleCoinsNumber;
        
        if ((positive + negative) > 2) {
            if (brosNumber == 2)
                return 20;
            if (brosNumber == 3)
                return 40;
            if (brosNumber > 3) {
                //if (test4InARow(coin.row, coin.column, x, y, color)){
                    return 250;
                //} else {
                //    return 40;
                //}
            }
            return 10;
        } else {
            return 0;
        }
    }
    
    public void recursivePossibleCoinsCounter(int currentX, int currentY, int x, int y, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            //4 nél maximalizálás kell bele
            if (possibleCoinsNumber >= 3) 
                return;            
            //if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == color) {
            if (cgs[currentX + x][currentY + y] == null || cgs[currentX + x][currentY + y].color == color) {
                //Tesók számolása
                if (cgs[currentX + x][currentY + y] != null){
                    brosNumber++;
                }
                possibleCoinsNumber++;
                recursivePossibleCoinsCounter(currentX + x, currentY + y, x, y, color);
            } else {  
                
            }
        }
    }
    /*
    public boolean test4InARow(int currentX, int currentY, int x, int y, Colors color) {
        //int plus = 0;
        //int minus = 0;
        directBrosNumber = 1;
        //possibleCoinsNumber = 0;
        immediateNeighborCounter(currentX, currentY, x, y, directBrosNumber, color);
        //directBrosNumber += possibleCoinsNumber;
        //possibleCoinsNumber = 0;
        immediateNeighborCounter(currentX, currentY, -x, -y, directBrosNumber, color);
        //minus = possibleCoinsNumber;
        
        if ((directBrosNumber) > 3) {
            return true;
        } else {
            return false;
        }
    }
    
    public void immediateNeighborCounter(int currentX, int currentY, int x, int y, int counter, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {            
            if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == color) {            
                counter++;
                immediateNeighborCounter(currentX + x, currentY + y, x, y, counter, color);
            } else {
                
            }
        }
    }
    */

    @Override
    protected String getInfo() {
        return informations;
    }

    @Override
    protected String getInfoBeforeStep() {
        fillMaxValueList();
        //fillMinValueList();
        return "Fill Max and Min";
    }

    @Override
    protected void paintModelHints(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12)); 
        g2d.setColor(Color.white);
        for (Iterator<Coin> iterator = cpuCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            g2d.setColor(Color.white);
                //sor értékek értékek kiírása
                g2d.drawString("[" + coin.V + "]", 220 + coin.column*100, 617 - coin.row*80);
                g2d.drawString("[" + coin.H + "]", 220 + coin.column*100, 635 - coin.row*80);
                g2d.drawString("[" + coin.NE + "]", 248 + coin.column*100, 625 - coin.row*80);
                g2d.drawString("[" + coin.NW + "]", 195 + coin.column*100, 625 - coin.row*80);
                
                g2d.setColor(Color.green);
                g2d.drawString("[" + coin.SUM + "]", 220 + coin.column*100, 655 - coin.row*80);
                //g2d.drawString("("+ coin.SUM + ")", 220 + i * 100, 700);
        }

        g2d.setColor(Color.PINK);
        g2d.setFont(new Font("SansSerif", Font.BOLD , 14));
        //g2d.drawString("Current Value:    " + fieldValueSum + "!", 100, 700);
        g2d.drawString("Field Values: ", 100, 700);
        for (int i = 0; i < 7; i++) {
            g2d.drawString("" + cpuValuesList[i], 220 + i*100, 700);
        }
        paintPlayerValues(g2d);
    }
    
    protected void paintPlayerValues(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12)); 
        g2d.setColor(Color.white);
        for (Iterator<Coin> iterator = playerCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            g2d.setColor(Color.white);
                //sor értékek értékek kiírása
                g2d.drawString("[" + coin.V + "]", 220 + coin.column*100, 617 - coin.row*80);
                g2d.drawString("[" + coin.H + "]", 220 + coin.column*100, 635 - coin.row*80);
                g2d.drawString("[" + coin.NE + "]", 248 + coin.column*100, 625 - coin.row*80);
                g2d.drawString("[" + coin.NW + "]", 195 + coin.column*100, 625 - coin.row*80);
                
                g2d.setColor(Color.green);
                g2d.drawString("[" + coin.SUM + "]", 220 + coin.column*100, 655 - coin.row*80);
                //g2d.drawString("("+ coin.SUM + ")", 220 + i * 100, 700);
        }

        g2d.setColor(new Color(180, 168, 24));
        g2d.setFont(new Font("SansSerif", Font.BOLD , 14));
        g2d.drawString("Player Min: ", 100, 740);
        for (int i = 0; i < 7; i++) {
            g2d.drawString("" + playerValuesList[i], 220 + i*100, 740);
        }
    }

    @Override
    protected void makeChangesInTheModel(int x) {
        
    }
}
