/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
/**
 *
 * @author Balla
 */

public class GameModel {
    public Coin[][] gameSpace;
    public Coin[] listOfPossibleSteps;
    boolean firstPlayer = true;
    public boolean singlePlayer = true;
    public boolean playersCoin = false;
    public boolean fourInARow = false;
    public boolean btnHintisON = false;
    public String currentGamingMode;
    public String winner;    
    //Min Max Pro mode
    public int modeTreeLevel = 2;
    public boolean modeFullAttack = false;
    
    private CPU_Modes cgm;  //currentGameMode    
    private int coinX, coinY;
    private int droppedCoinsNumber;
    
    public Coin coinToAdd;    
    
    public int coinCordinate_X, coinCordinate_Y;
    public boolean animatedCoinVisible = false;
    public boolean animatedCoinYellow = false;
    
    public GameModel() {
        gameSpace = new Coin[6][7];
        listOfPossibleSteps = new Coin[7];
        //currentGamingMode = "Min Max Algorithm";
        currentGamingMode = "Min Max Pro";
        droppedCoinsNumber = 0;
        //this.cgm = new CPU_MinMaxAlgorithm(gameSpace);
        this.cgm = new CPU_MinMaxPro(gameSpace, modeTreeLevel, modeFullAttack);
    }
    
    public void newGame() {
        gameSpace = new Coin[6][7];
        firstPlayer = true;
        fourInARow = false;
        droppedCoinsNumber = 0;
        
        if (currentGamingMode == "Random" || currentGamingMode == "2 Players"){
            cgm = new CPU_Random(gameSpace);
        }
        if (currentGamingMode == "Last Coin AI") {
            cgm = new CPU_LastDrop(gameSpace);
        }
        if (currentGamingMode == "Value Checker AI") {
            cgm = new CPU_ValueChecker(gameSpace, listOfPossibleSteps);
        }            
        if (currentGamingMode == "Game Field Value") {
            cgm = new CPU_GameFieldValue(gameSpace);
        }
        if (currentGamingMode == "Min Max Algorithm") {
            cgm = new CPU_MinMaxAlgorithm(gameSpace);
        }
        if (currentGamingMode == "Min Max Pro") {
            cgm = new CPU_MinMaxPro(gameSpace, modeTreeLevel, modeFullAttack);
        }
    }
    
    public Point getCoinCoordinates() {
        return new Point(coinX, coinY);
    }
    
    public void setCoinCoordinates(Point coordinates) {
        coinX = coordinates.x;
        coinY = coordinates.y;
    }
    
    public int getDroppedCoinsNumber() {
        return droppedCoinsNumber;
    }   
    
    public int cpuNextStep() {
        //KAVARÁSSS
        coinToAdd = cgm.cpuNextCoin();
        if (coinToAdd != null) {
            //addCoinPlayer2(cpuCoin.row, cpuCoin.column);        
            return coinToAdd.column;
        }
        else {
            return -1;
        }
    }
    
    public void waitforAddCoin() {
        addCoinPlayer2(coinToAdd.row, coinToAdd.column);        
    }
    
    public void changePlayer() {
        firstPlayer = !firstPlayer;
    }
    
    public void addCoin(int row, int column) {
        gameSpace[row][column] = new Coin(row, column, Colors.yellow);
        droppedCoinsNumber++;
        updateNeighboringCoinsNumber(row, column);
    }    
    
    public void addCoinPlayer2(int row, int column) {
        gameSpace[row][column] = new Coin(row, column, Colors.red);
        droppedCoinsNumber++;
        updateNeighboringCoinsNumber(row, column);
    }
    
    public int dropCoin(int column) {
        if (gameSpace[5][column] == null) {
            int i = 0;
            for (i = 0; i < 6; i++) {
                if (gameSpace[i][column] == null) {
                    break;
                }
            }
            if (firstPlayer)
                addCoin(i, column);
            else
                addCoinPlayer2(i, column);
            
            return 0;
        }
        else 
            return -1;
    }
    
    public void updateNeighboringCoinsNumber(int row, int column) {
        Colors currentColor = gameSpace[row][column].color;
        
        //Horizontal
        int leftBroNumbers = 0;
        if(column > 0 && gameSpace[row][column - 1] != null && gameSpace[row][column - 1].color == currentColor) {
            leftBroNumbers = gameSpace[row][column - 1].horizontal;
        }
        int righBroNumbers = 0;
        if(column < 6 && gameSpace[row][column + 1] != null && gameSpace[row][column + 1].color == currentColor) {
            righBroNumbers = gameSpace[row][column + 1].horizontal;
        }
        //mindegyik beállítása
        for (int i = 0; i <= leftBroNumbers + righBroNumbers; i++) {
            gameSpace[row][column - leftBroNumbers + i].horizontal = leftBroNumbers + righBroNumbers +1;
            //if (gameSpace[row][column - leftBroNumbers + i].horizontal == 4)
            if (gameSpace[row][column - leftBroNumbers + i].horizontal >= 4) {
                if (currentColor == Colors.red)
                    gameSpace[row][column - leftBroNumbers + i].color = Colors.green_red;
                else
                    gameSpace[row][column - leftBroNumbers + i].color = Colors.green_yellow;
            }
        }        
        
        //Vertical
        if (row > 0 && gameSpace[row - 1][column].color == currentColor) {
            int underneatBroNumbers = gameSpace[row - 1][column].vertical;
            gameSpace[row][column].vertical = underneatBroNumbers;
            
            for (int i = 0; i <= underneatBroNumbers; i++) {
                gameSpace[row - i][column].vertical++;
                //gameSpace[row][column].vertical++;
                if(gameSpace[row - i][column].vertical == 4)
                    if (currentColor == Colors.red)
                        gameSpace[row - i][column].color = Colors.green_red;
                    else
                        gameSpace[row - i][column].color = Colors.green_yellow;
            }
        }
        
        //NortWest
        leftBroNumbers = 0;
        if(column > 0 && row < 5 && gameSpace[row + 1][column - 1] != null && gameSpace[row + 1][column - 1].color == currentColor) {
            leftBroNumbers = gameSpace[row + 1][column - 1].northWest_BF;
        }
        righBroNumbers = 0;
        if(column < 6 && row > 0 && gameSpace[row - 1][column + 1] != null && gameSpace[row - 1][column + 1].color == currentColor) {
            righBroNumbers = gameSpace[row - 1][column + 1].northWest_BF;
        }
        //mindegyik beállítása
        for (int i = 0; i <= leftBroNumbers + righBroNumbers; i++) {
            gameSpace[row + leftBroNumbers - i][column - leftBroNumbers + i].northWest_BF = leftBroNumbers + righBroNumbers + 1;
            if (gameSpace[row + leftBroNumbers - i][column - leftBroNumbers + i].northWest_BF >= 4)
                //gameSpace[row + leftBroNumbers - i][column - leftBroNumbers + i].color = Colors.green;
                if (currentColor == Colors.red)
                    gameSpace[row + leftBroNumbers - i][column - leftBroNumbers + i].color = Colors.green_red;
                else
                    gameSpace[row + leftBroNumbers - i][column - leftBroNumbers + i].color = Colors.green_yellow;
        }
        
        //NorthEast
        leftBroNumbers = 0;
        if(column > 0 && row > 0 && gameSpace[row - 1][column - 1] != null && gameSpace[row - 1][column - 1].color == currentColor) {
            leftBroNumbers = gameSpace[row - 1][column - 1].northEast_JF;
        }
        righBroNumbers = 0;
        if(column < 6 && row < 5 && gameSpace[row + 1][column + 1] != null && gameSpace[row + 1][column + 1].color == currentColor) {
            righBroNumbers = gameSpace[row + 1][column + 1].northEast_JF;
        }
        for (int i = 0; i <= leftBroNumbers + righBroNumbers; i++) {
            gameSpace[row - leftBroNumbers + i][column - leftBroNumbers + i].northEast_JF = leftBroNumbers + righBroNumbers + 1;
            if (gameSpace[row - leftBroNumbers + i][column - leftBroNumbers + i].northEast_JF >= 4)
                //gameSpace[row - leftBroNumbers + i][column - leftBroNumbers + i].color = Colors.green;
                if (currentColor == Colors.red)
                    gameSpace[row - leftBroNumbers + i][column - leftBroNumbers + i].color = Colors.green_red;
                else
                    gameSpace[row - leftBroNumbers + i][column - leftBroNumbers + i].color = Colors.green_yellow;
        }
        
        //ellenörzés
        if (gameSpace[row][column].horizontal >= 4 || gameSpace[row][column].vertical == 4 
                || gameSpace[row][column].northWest_BF >= 4 || gameSpace[row][column].northEast_JF >= 4) {
            fourInARow = true;
            if (currentColor == Colors.yellow) {
                winner = "Player 1";
            } else {
                if (singlePlayer) {
                    winner = "Computer!";
                } else {
                    winner = "Player 2";
                }
            }            
        }
    }
    
    public int checkCoinLocation() {
        if(159 < coinX && coinX < 850 && coinY < 200) {
            return (coinX - 159) / 100;
        } else {
            return -1;
        }
    }
    
    public void paintGameSpace(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        ////g2d.setColor(new Color(145, 25, 0)); Régi piros keret
        //g2d.setColor(new Color(153, 26, 0)); Régi piros
        //g2d.setColor(new Color(180, 168, 24)); Régi sárga keret
        //g2d.setColor(new Color(211, 198, 29)); Régi sárga
        
        if (animatedCoinVisible) {
            if (!animatedCoinYellow) {
                g2d.setColor(new Color(121, 12, 9));
                g2d.fillOval(coinCordinate_X, coinCordinate_Y, 64, 64);
                //g2d.setColor(Color.BLACK);
                g2d.setColor(new Color(79, 79, 79));
                g2d.fillOval(coinCordinate_X + 5, coinCordinate_Y + 5, 54, 54);
                //g2d.setColor(new Color(159, 15, 11));
                g2d.setColor(new Color(140, 13, 10));
                g2d.fillOval(coinCordinate_X + 6, coinCordinate_Y + 6, 52, 52);
            } else {
                g2d.setColor(new Color(175, 163, 20));
                g2d.fillOval(coinCordinate_X, coinCordinate_Y, 64, 64);
                            //g2d.setColor(Color.BLACK);
                g2d.setColor(new Color(79, 79, 79));
                g2d.fillOval(coinCordinate_X + 5, coinCordinate_Y + 5, 54, 54);
                g2d.setColor(new Color(205, 190, 25));
                g2d.fillOval(coinCordinate_X + 6, coinCordinate_Y + 6, 52, 52);
            }
        }

        //g2d.fillRect(182, 190, 700, 483);
        //g2d.setColor(new Color(36, 84, 251));
        g2d.setColor(new Color(36, 90, 251));   //blue
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(181, 670 - 6*80, 875, 670 - 6*80);
        //g2d.drawLine(181 + 700, 670, 181 + 700, 200);
        
        g2d.setStroke(new BasicStroke(16));
        for (int i = 0; i < 7; i++) {
                g2d.drawLine(181, 670 - i*80, 875, 670 - i*80);
        }
        g2d.setStroke(new BasicStroke(17));
        for (int i = 0; i < 8; i++) {
            if(i == 0) {
                g2d.drawLine(181 + i*100, 670, 181 + i*100, 200);
                g2d.drawLine(189 + i*100, 670, 189 + i*100, 200);
            }
            else {
                g2d.drawLine(171 + i*100, 670, 171 + i*100, 200);
                if (i != 7)
                    g2d.drawLine(188 + i*100, 670, 188 + i*100, 200);
            }
        }
        
        g2d.setStroke(new BasicStroke(14));
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                g2d.drawOval(191 + j*100, 591 - i*80, 78, 78);
            }
        }
        g2d.setColor(new Color(36, 74, 248));
        g2d.setStroke(new BasicStroke(4));
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                g2d.drawOval(199 + j*100, 599 - i*80, 64, 64);
            }
        }
        
        g2d.setColor(new Color(36, 76, 250));
        g2d.fillRect(146, 673, 768, 38);        
        g2d.fillRect(170, 190, 12, 483);
        g2d.fillRect(876, 190, 12, 483);
                
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (gameSpace[i][j] != null) {
                    if(gameSpace[i][j].color == Colors.yellow) {
                        g2d.setColor(new Color(175, 163, 20));
                        g2d.fillOval(200 + j*100, 600 - i*80, 64, 64);                
                        //g2d.setColor(Color.BLACK);
                        g2d.setColor(new Color(79, 79, 79));
                        g2d.fillOval(205 + j*100, 605 - i*80, 54, 54);
                        g2d.setColor(new Color(205, 190, 25));
                        g2d.fillOval(206 + j*100, 606 - i*80, 52, 52);
                    }
                    if(gameSpace[i][j].color == Colors.red) {
                        g2d.setColor(new Color(121, 12, 9));
                        g2d.fillOval(200 + j*100, 600 - i*80, 64, 64);
                        //g2d.setColor(Color.BLACK);
                        g2d.setColor(new Color(70, 70, 70));
                        g2d.fillOval(205 + j*100, 605 - i*80, 54, 54);
                        g2d.setColor(new Color(140, 13, 10));
                        g2d.fillOval(206 + j*100, 606 - i*80, 52, 52);
                    }
                    if(gameSpace[i][j].color == Colors.green_red) {
                        g2d.setColor(new Color(44, 169, 1));
                        g2d.fillOval(200 + j*100, 600 - i*80, 64, 64);
                        g2d.setColor(new Color(79, 79, 79));
                        g2d.fillOval(205 + j*100, 605 - i*80, 54, 54);
                        g2d.setColor(new Color(140, 13, 10));
                        g2d.fillOval(206 + j*100, 606 - i*80, 52, 52);
                    }
                    if(gameSpace[i][j].color == Colors.green_yellow) {
                        g2d.setColor(new Color(44, 169, 1));
                        g2d.fillOval(200 + j*100, 600 - i*80, 64, 64);
                        g2d.setColor(new Color(79, 79, 79));
                        g2d.fillOval(205 + j*100, 605 - i*80, 54, 54);                 
                        g2d.setColor(new Color(205, 190, 25));
                        g2d.fillOval(206 + j*100, 606 - i*80, 52, 52);
                    }
                }
            }
        }
        
        if (btnHintisON) {
            drawHints(g2d);
        }        
        // Next palyer
        if (!singlePlayer){
            if (firstPlayer) {
                g2d.setColor(new Color(205, 190, 25));
            } else {
                g2d.setColor(new Color(159, 15, 11));
            }
            g2d.fillOval(20, 20, 120, 40);
            g2d.setColor(Color.black);
            g2d.drawString("Next Player", 45, 45);
        } 
    }
    
    public void drawHints(Graphics2D g2d) {        
        cgm.paintModelHints(g2d);
    }
    
    public void paintPlayersCoin(Graphics2D g2d) {
        if (playersCoin){
            if (firstPlayer) {
                g2d.setColor(new Color(175, 163, 20));
                g2d.fillOval(coinX, coinY, 64, 64);                
                g2d.setColor(new Color(79, 79, 79));        //Dark Gray
                g2d.fillOval(coinX + 5, coinY + 5, 54, 54);
                g2d.setColor(new Color(205, 190, 25));
                g2d.fillOval(coinX + 6, coinY + 6, 52, 52);
            } else {
                g2d.setColor(new Color(121, 12, 9));
                g2d.fillOval(coinX, coinY, 64, 64);
                g2d.setColor(new Color(79, 79, 79));    //Dark Gray
                g2d.fillOval(coinX + 5, coinY + 5, 54, 54);
                g2d.setColor(new Color(159, 15, 11));
                g2d.fillOval(coinX + 6, coinY + 6, 52, 52);
            }
        }
    }
    
    public String getInfoFromCGM() {
        return cgm.getInfo();
    }
    
    public String getInfoBeforeStep() {
        return cgm.getInfoBeforeStep();
    }
    
    public void makeChangesInTheModel(int x) {
        cgm.makeChangesInTheModel(x);
    }
}
