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
public class CPU_GameFieldValue extends CPU_Modes {
    
    public int numberOfSteps;
    public int fieldValueSum;
    public int possibleCoinsNumber;
    public int counterOfTheBros;
    public Coin currentCoin;
    public String importentInfos;
    
    public int[] fieldValuesList;
    public int[] playerFieldValueList;
    public ArrayList<Coin> droppedCoinsList;
    public ArrayList<Coin> playerCoinsList;
    
    public CPU_GameFieldValue(Coin[][] gameField) {
        cgs = gameField;
        numberOfSteps = 0;
        fieldValueSum = 0;
        
        fieldValuesList = new int[7];
        playerFieldValueList = new int[7];
        droppedCoinsList = new ArrayList<>();
        playerCoinsList = new ArrayList<>();
    }

    @Override
    protected Coin cpuNextCoin() {        
        if (numberOfSteps == 0) {
            int startColumn = (int) ((Math.random() * 3) + 2);
            numberOfSteps++;
            //lastCoin = new Coin(searchRow(startColumn), startColumn, Colors.red);
            droppedCoinsList.add(new Coin(searchRow(startColumn), startColumn, Colors.red));
            return droppedCoinsList.get(numberOfSteps - 1);
        }
        
        //currentCoin = droppedCoinsList.get(numberOfSteps - 1);
        if (numberOfSteps == 1) {
            currentCoin = droppedCoinsList.get(numberOfSteps - 1);
            int direction = giveRandomDirection(3);
            if (currentCoin.column == 2) {
                if ((currentCoin.row == 0 || cgs[0][3] != null))
                    direction = giveRandomDirection(2);
                if (direction == 0 && cgs[1][2] != null)
                    direction = 1;
                if (direction == 1 && cgs[1][3] != null)
                    direction = 0;
            }
            if (currentCoin.column == 4) {
                //if (direction == 1) {
                if (direction == 1 && (currentCoin.row == 0 || cgs[0][3] != null)) 
                    direction = (giveRandomDirection(2) - 1);
                
                if (direction == 0 && cgs[1][4] != null)
                    direction = -1;
                if (direction == -1 && cgs[1][3] != null)
                    direction = 0;
            }
            numberOfSteps++;
            //droppedCoinsList.add(new Coin(searchRow(startColumn), startColumn, Colors.red));
            //return droppedCoinsList.get(numberOfSteps - 1);
            droppedCoinsList.add(new Coin(searchRow(currentCoin.column + direction), currentCoin.column + direction, Colors.red));
            return droppedCoinsList.get(numberOfSteps - 1);            
        }
        //Védekezés!!        
        fillPlayerFieldValueList();        
        fillFieldValueList();
        
        //Select the best and drop it!!!
        int max = 0;
        int index = 0;        
        
        for (int i = 0; i < 7; i++) {
            if (fieldValuesList[i] > max) {
                max = fieldValuesList[i];
                index = i;
            }
        }
        
        int playerMin = 0;
        int playerIndex = 0;
        for (int i = 0; i < 7; i++) {
            if (playerFieldValueList[i] < playerMin) {
                playerMin = playerFieldValueList[i];
                playerIndex = i;
            }
        }
        if (max < 1001 && playerMin < -1001) {
            index = playerIndex;
        }
        
        numberOfSteps++;
        currentCoin = new Coin(searchRow(index), index, Colors.red);
        droppedCoinsList.add(currentCoin);
        return currentCoin;
        
        //int randomCollumn = (int) (Math.random() * 7);
    }
    
    public int giveRandomDirection(int directions) {
        int randomDirection = (int) (Math.random() * directions) - 1;
        if (directions == 2)
            randomDirection++;
        return randomDirection;
    }
    
    public void fillFieldValueList() {
        int currentColumn = 0;
        int currentRow = 0;
        
        for (int i = 0; i < 7; i++) {
            if (cgs[5][i] == null) {        
                currentColumn = i;
                currentRow = searchRow(currentColumn);
                
                //hozzáadni egy coint
                cgs[currentRow][currentColumn] = new Coin(currentRow, currentColumn, Colors.red);
                droppedCoinsList.add(cgs[currentRow][currentColumn]);

                //kiszámolás
                fieldValueSum = 0;
                for (Iterator<Coin> iterator = droppedCoinsList.iterator(); iterator.hasNext();) {
                    Coin coin = iterator.next();
                    coin.SUM = 0;                    
                    
                    coin.H = calculateCoinNumbers(coin, 0, 1, Colors.yellow);
                    coin.SUM += coin.H;
                    coin.V = calculateCoinNumbers(coin, 1, 0, Colors.yellow);
                    coin.SUM += coin.V;
                    coin.NE = calculateCoinNumbers(coin, 1, 1, Colors.yellow);
                    coin.SUM += coin.NE;            
                    coin.NW = calculateCoinNumbers(coin, 1, -1, Colors.yellow);
                    coin.SUM += coin.NW;

                    fieldValueSum += coin.SUM;
                }
                fieldValuesList[i] = fieldValueSum;
                
                droppedCoinsList.remove(droppedCoinsList.size()-1);
                cgs[currentRow][currentColumn] = null;
            } else {
                fieldValuesList[i] = -100;
            }
            
            //törölni a coint
            //droppedCoinsList.remove(droppedCoinsList.size()-1);
            //cgs[currentRow][currentColumn] = null;
        }
    }
    
    public void fillPlayerFieldValueList() { 
        int currentColumn = 0;
        int currentRow = 0;
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
                    
                    coin.H = -calculateCoinNumbers(coin, 0, 1, Colors.red);
                    coin.SUM += coin.H;
                    coin.V = -calculateCoinNumbers(coin, 1, 0, Colors.red);
                    coin.SUM += coin.V;
                    coin.NE = -calculateCoinNumbers(coin, 1, 1, Colors.red);
                    coin.SUM += coin.NE;            
                    coin.NW = -calculateCoinNumbers(coin, 1, -1, Colors.red);
                    coin.SUM += coin.NW;

                    fieldValueSum += coin.SUM;
                }
                playerFieldValueList[i] = fieldValueSum;
                
                playerCoinsList.remove(playerCoinsList.size()-1);
                cgs[currentRow][currentColumn] = null;
            } else {
                playerFieldValueList[i] = -1;
            }
        }
    }
    
    public int calculateCoinNumbers(Coin coin, int x, int y, Colors color) {
        int positive = 0;
        int negative = 0;
        counterOfTheBros = 1;
        possibleCoinsNumber = 0;
        
        //recursivePossibleCoinsCounter(coin.row, coin.column, x, y);
        recursivePossibleCoinsCounter(coin.row, coin.column, x, y, color);
        positive = possibleCoinsNumber;
        possibleCoinsNumber = 0;
        recursivePossibleCoinsCounter(coin.row, coin.column, -x, -y, color);
        negative = possibleCoinsNumber;
        
        if ((positive + negative) > 2) {
            if (counterOfTheBros == 2)
                return 20;
            if (counterOfTheBros == 3)
                return 40;
            if (counterOfTheBros > 3) {
                if (testFourInaRow(coin.row, coin.column, x, y, color)){
                    return 250;
                } else {
                    return 40;
                }
            }
                //return 10 * counterOfTheBros *(counterOfTheBros/2);
                //return 10 * counterOfTheBros;
            return 10;
        } else {
            return 0;
        }
    }
    
    public void recursivePossibleCoinsCounter(int currentX, int currentY, int x, int y, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            //if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == Colors.yellow) {
            
            if (possibleCoinsNumber >= 3) 
                return;
            
            if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == color) {
                
            } else {
                //4 nél maximalizálás kell bele
                
                //Tesók számolása
                if (cgs[currentX + x][currentY + y] != null){
                    counterOfTheBros++;
                }
                possibleCoinsNumber++;
                recursivePossibleCoinsCounter(currentX + x, currentY + y, x, y, color);
            }
        }
    }
    
    public boolean testFourInaRow(int currentX, int currentY, int x, int y, Colors color) {
        int plus = 0;
        int minus = 0;
        //possibleCoinsNumber = 0;
        possibleCoinsNumber = 0;
        immediateNeighborCounter(currentX, currentY, x, y, plus, color);
        plus = possibleCoinsNumber;
        possibleCoinsNumber = 0;
        immediateNeighborCounter(currentX, currentY, -x, -y, minus, color);
        minus = possibleCoinsNumber;
        
        if ((plus + minus) >= 3) {
            return true;
        } else {
            return false;
        }
    }
    
    public void immediateNeighborCounter(int currentX, int currentY, int x, int y, int counter, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {            
            if (cgs[currentX + x][currentY + y] == null || cgs[currentX + x][currentY + y].color == color) {
            } else {
                possibleCoinsNumber++;
                immediateNeighborCounter(currentX + x, currentY + y, x, y, counter, color);
            }
        }
    }
    
    /*
    public void recursivePossibleCoinsCounter(int currentX, int currentY, int x, int y) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == Colors.yellow) {
                
            } else {
                //4 nél maximalizálás kell bele
                
                //Tesók számolása
                if (cgs[currentX + x][currentY + y] != null){
                    counterOfTheBros++;
                }
                possibleCoinsNumber++;
                recursivePossibleCoinsCounter(currentX + x, currentY + y, x, y);
            }
        }
    }
    */
    
    public void fieldValueInfos() {        
        //kiszámolni
        fieldValueSum = 0;
        for (Iterator<Coin> iterator = droppedCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            coin.H = coin.V = coin.NE = coin.NW = coin.SUM = 0;
        }
        
        for (Iterator<Coin> iterator = droppedCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            coin.H = calculateCoinNumbers(coin, 0, 1, Colors.yellow);
            coin.SUM += coin.H;
            
            coin.V = calculateCoinNumbers(coin, 1, 0, Colors.yellow);
            coin.SUM += coin.V;
            
            coin.NE = calculateCoinNumbers(coin, 1, 1, Colors.yellow);
            coin.SUM += coin.NE;            
            coin.NW = calculateCoinNumbers(coin, 1, -1, Colors.yellow);
            coin.SUM += coin.NW;
            
            fieldValueSum += coin.SUM;
        }
    }
    

    @Override
    protected String getInfo() {
        //fillFieldValueList();
        fieldValueInfos();
        /*
        importentInfos = "Coins list: ";
        for (Iterator<Coin> iterator = droppedCoinsList.iterator(); iterator.hasNext();) {
            Coin next = iterator.next();
            importentInfos += "(" + next.row + ", " + next.column + "); ";            
        }
        importentInfos += "\n";
        return importentInfos;
        */
        return "";
    }

    @Override
    protected String getInfoBeforeStep() {        
        //return getInfo();
        fillFieldValueList();
        return "";//"gameField before step check!\n";
    }

    @Override
    protected void paintModelHints(Graphics2D g2d) {
        
        fillPlayerFieldValueList();
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12));
        //int i = 0;        
        g2d.setColor(Color.white);
        for (Iterator<Coin> iterator = droppedCoinsList.iterator(); iterator.hasNext();) {
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
        
        
        g2d.setFont(new Font("SansSerif", Font.BOLD , 14));
        g2d.drawString("Current Value:    " + fieldValueSum + "!", 100, 700);
        g2d.drawString("Field Values: ", 100, 730);
        for (int i = 0; i < 7; i++) {
            g2d.drawString("" + fieldValuesList[i], 220 + i*100, 730);
        }
        g2d.setColor(Color.red);
        g2d.drawString("Player Values: ", 100, 750);
        for (int i = 0; i < 7; i++) {
            g2d.drawString("" + playerFieldValueList[i], 220 + i*100, 750);
        }
    }

    @Override
    protected void makeChangesInTheModel(int x) {
        
    }
    
}
