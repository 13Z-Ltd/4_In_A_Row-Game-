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
public class CPU_MinMaxPro extends CPU_Modes {
    public int numberOfSteps;
    public Coin currentCoin;
    public int fieldValueSum;
    public int possibleCoinsNumber;
    public int brosNumber;
    //public int directBrosNumber;    
    public int currentMinimum;
    public int currentMinimumIndex;
    
    public int[] cpuValuesList;
    public int[] playerValuesList;
    public int[] actualValuesList;
    public ArrayList<Coin> cpuCoinsList;
    public ArrayList<Coin> playerCoinsList;
    public PV_Tree pv_Tree;
    public boolean fullAttack;
    public int treeLevel;
    
    public String informations;
    
    public CPU_MinMaxPro(Coin[][] gameField, int modelTreeLevel, boolean modeFullAttack) {
        cgs = gameField;
        cpuCoinsList = new ArrayList<>();
        playerCoinsList = new ArrayList<>();
        actualValuesList = new int[7];
        treeLevel = modelTreeLevel;
        fullAttack = modeFullAttack;
        
        numberOfSteps = 0;
        informations = "";
    }
    
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
                //4 egy sorban ellen??rz??s
                if (check4InARow(coin, x, y, color))
                    //return 250;
                    return 500;
                else
                    return 50;
            }
            return 10;
        } else {
            return 0;
        }
    }
    
    public void recursivePossibleCoinsCounter(int currentX, int currentY, int x, int y, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            //4 n??l maximaliz??l??s kell bele
            if (possibleCoinsNumber >= 3) 
                return;            
            //if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == color) {
            if (cgs[currentX + x][currentY + y] == null || cgs[currentX + x][currentY + y].color == color) {
                //Tes??k sz??mol??sa
                if (cgs[currentX + x][currentY + y] != null){
                    brosNumber++;
                }
                possibleCoinsNumber++;
                recursivePossibleCoinsCounter(currentX + x, currentY + y, x, y, color);
            } else {  
                
            }
        }
    }
    
    public boolean check4InARow(Coin coin, int x, int y, Colors color) {
        int broCounter = 1;
        possibleCoinsNumber = 0;
        recursiveBroCounter(coin.row, coin.column, x, y, color);
        broCounter += possibleCoinsNumber;
        possibleCoinsNumber = 0;
        recursiveBroCounter(coin.row, coin.column, -x, -y, color);
        broCounter += possibleCoinsNumber;
        if (broCounter >= 4) {
            return true;
        }else {
            return false;
        }
    }
    
    public void recursiveBroCounter(int currentX, int currentY, int x, int y, Colors color) {
        if (currentX + x > -1 && currentX + x < 6 && currentY + y < 7 && currentY + y > -1) {
            //4 n??l maximaliz??l??s kell bele
            if (cgs[currentX + x][currentY + y] != null && cgs[currentX + x][currentY + y].color == color) {                
                possibleCoinsNumber++;
                recursiveBroCounter(currentX + x, currentY + y, x, y, color);
            } else {  
                
            }
        }
    }
    
    public int earlyStepsDecisions() {
        int returnIndex = 0;
        int yellowIndex = 0;
        while ((cgs[0][yellowIndex] == null || cgs[0][yellowIndex].color != Colors.yellow) && yellowIndex < 6) {
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
            //kell m??g           
            //pv_Tree.recursiveCalculations(pv_Tree.root, 1);
            pv_Tree.recursiveCalculations(pv_Tree.root, treeLevel);            
            returnIndex = pv_Tree.getMaxDeltaIndex();
        }
        return returnIndex;
    }

    @Override
    protected Coin cpuNextCoin() {
        //adott CGS b??l inditva a sz??m??t??sokat 
        //??llapotsz??ml??l?? fa l??trehoz??sa alap ??llapotb??l
        //a gy??k??r elembe k??szik majd fel az eredm??ny
        pv_Tree = new PV_Tree();
        
        //sz??m??t??s ind??t??sa
        if (numberOfSteps == 0) {
            int startColumn = (int) ((Math.random() * 5) + 1);
            numberOfSteps++;
            currentCoin = new Coin(searchRow(startColumn), startColumn, Colors.red);
            cpuCoinsList.add(currentCoin);
            return currentCoin;
        }
        
        //Decision-Making
        if (numberOfSteps > 0 && numberOfSteps < 4) {
            
            int index = earlyStepsDecisions();
            numberOfSteps++;
            //currentCoin = new Coin(searchRow(returnIndex), returnIndex, Colors.red);
            currentCoin = new Coin(searchRow(index), index, Colors.red);
            cpuCoinsList.add(currentCoin);            
            return currentCoin; 
        }
        //Fa sz??m??t??sainak ind??t??sa
        //pv_Tree.recursiveCalculations(pv_Tree.root, 2);        
        pv_Tree.recursiveCalculations(pv_Tree.root, treeLevel);
        
        //Vissza adjuk a legide??ll??sabnak v??lt coin hely??t
        //currentCoin = new Coin(searchRow(pv_Tree.getMaxIndex()), pv_Tree.getMaxIndex(), Colors.red);
        numberOfSteps++;
        currentCoin = new Coin(searchRow(pv_Tree.getMaxDeltaIndex()), pv_Tree.getMaxDeltaIndex(), Colors.red);
        cpuCoinsList.add(currentCoin);
        return currentCoin;
    }

    @Override
    protected String getInfo() {        
        return informations;
    }

    @Override
    protected String getInfoBeforeStep() {        
        return informations;
    }
    
    @Override
    protected void makeChangesInTheModel(int x) {        
        switch (x){
            case 0:
                fullAttack = false;
                break;
            case 1:
                fullAttack = true;
                break;
            case 11:
                treeLevel = 1;
                break;
            case 12:
                treeLevel = 2;
                break;
            case 13:
                treeLevel = 3;
                break;
            default:
                break;
        }
    }

    @Override
    protected void paintModelHints(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12)); 
        g2d.setColor(Color.white);
        for (Iterator<Coin> iterator = cpuCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            g2d.setColor(Color.white);
                //sor ??rt??kek ??rt??kek ki??r??sa
                g2d.drawString("[" + coin.V + "]", 220 + coin.column*100, 617 - coin.row*80);
                g2d.drawString("[" + coin.H + "]", 220 + coin.column*100, 635 - coin.row*80);
                g2d.drawString("[" + coin.NE + "]", 248 + coin.column*100, 625 - coin.row*80);
                g2d.drawString("[" + coin.NW + "]", 195 + coin.column*100, 625 - coin.row*80);
                
                g2d.setColor(Color.green);
                g2d.drawString("[" + coin.SUM + "]", 220 + coin.column*100, 655 - coin.row*80);
        }
        /*
        g2d.setColor(Color.PINK);
        g2d.setFont(new Font("SansSerif", Font.BOLD , 14));
        if (pv_Tree != null && pv_Tree.root.childNodes[0] != null) {
            g2d.drawString("CPU Max: ", 100, 700);
            for (int i = 0; i < 7; i++) {
                g2d.drawString("" + pv_Tree.root.childNodes[i].predictedMax, 220 + i*100, 700);
            }
        }
        */
        paintPlayerValues(g2d);
    }
    
    protected void paintPlayerValues(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD , 12)); 
        g2d.setColor(Color.white);
        for (Iterator<Coin> iterator = playerCoinsList.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            g2d.setColor(Color.white);
                //sor ??rt??kek ??rt??kek ki??r??sa
                g2d.drawString("[" + coin.V + "]", 220 + coin.column*100, 617 - coin.row*80);
                g2d.drawString("[" + coin.H + "]", 220 + coin.column*100, 635 - coin.row*80);
                g2d.drawString("[" + coin.NE + "]", 248 + coin.column*100, 625 - coin.row*80);
                g2d.drawString("[" + coin.NW + "]", 195 + coin.column*100, 625 - coin.row*80);
                
                g2d.setColor(Color.green);
                g2d.drawString("[" + coin.SUM + "]", 220 + coin.column*100, 655 - coin.row*80);
        }
        

        g2d.setColor(Color.PINK);
        g2d.setFont(new Font("SansSerif", Font.BOLD , 14));
        if (pv_Tree != null && pv_Tree.root.childNodes[0] != null) {
            g2d.drawString("CPU Max: ", 100, 700);
            for (int i = 0; i < 7; i++) {
                g2d.drawString("" + pv_Tree.root.childNodes[i].predictedMax, 220 + i*100, 700);
            }
        }

        g2d.setColor(new Color(180, 168, 24));   
        if (pv_Tree != null && pv_Tree.root.childNodes[1] != null) {
            g2d.drawString("Player Min: ", 100, 725);
            g2d.drawString("Delta: ", 100, 750);
            for (int i = 0; i < 7; i++) {
                g2d.drawString("" + pv_Tree.root.childNodes[i].predictedMin, 220 + i*100, 725);
                g2d.drawString("" + pv_Tree.root.childNodes[i].delta, 220 + i*100, 750);
            }
        }
        if (pv_Tree != null && pv_Tree.root.childNodes[1] != null) {
            g2d.drawString("The Best choice: ", 100, 780);
            g2d.drawString("" + pv_Tree.root.predictedMax + " (" + pv_Tree.root.predictedMaxIndex + ")", 220 + 100, 780);
            g2d.drawString("Delta: " + pv_Tree.root.delta + " (" + pv_Tree.root.deltaIndex + ")", 420, 780);
            g2d.drawString("Tree Level: " + treeLevel , 550, 780);
            if (fullAttack)
                g2d.drawString("Mode: Full Attack", 700, 780);
            else
                g2d.drawString("Mode: Delta Calculator" , 700, 780);
        }
    }
    
    
    public class PV_Tree {
        //Fa tartalmazza a gy??k??r elemet illetve a sz??m??t??sokhoz sz??ks??ges egy??bb adatokat 
        PV_Node root;
        ;
        
        public PV_Tree() {
            root = new PV_Node();
            //fullAttack = false;
        }
        
        //recurziv sz??m??t??s a fa elemein
        public void recursiveCalculations(PV_Node currentNode, int counter) {
            counter--;
            //??j gyerekek l??trehoz??sa
            for (int i = 0; i < 7; i++) {
                PV_Node newNode = new PV_Node();
                newNode.parentNode = currentNode;
                currentNode.childNodes[i] = newNode;
                
                //lehets??ges ??rt??keik kisz??m??t??sa - tov??bb??t??sa a sz??l??nek amennyiben lehet m??g oda tennni
                calculatePredictedValues(currentNode.childNodes[i], i);
                //treeLevelMultiplier(currentNode.childNodes[i], counter);
                treeLevelMultiplier(currentNode.childNodes[i], counter + 1);
                
                //Ha sz??ks??ges rek??rz??v hiv??sa az elj??r??snak az adott elemre - azaz ha a predictedMax < 1000
                //if(currentNode.childNodes[i].predictedMax < 1000 && currentNode.childNodes[i].predictedMin >= -1000 && counter > 0) {
                if(currentNode.childNodes[i].predictedMax < 2000 && currentNode.childNodes[i].predictedMin >= -2000 && counter > 0) {
                    if (cgs[5][i] == null) {
                        //korongok hozz??ad??sa
                        int addedRedIndex = currentNode.childNodes[i].predictedMaxIndex;
                        int addedRedRow = searchRow(addedRedIndex);
                        int addedYellowIndex = currentNode.childNodes[i].predictedMinIndex;
                        int addedYellowRow = searchRow(addedYellowIndex);
                        cgs[addedRedRow][addedRedIndex] = new Coin(addedRedRow, addedRedIndex, Colors.red);
                        cpuCoinsList.add(cgs[addedRedRow][addedRedIndex]);
                        cgs[addedYellowRow][addedYellowIndex] = new Coin(addedYellowRow, addedYellowIndex, Colors.yellow);
                        playerCoinsList.add(cgs[addedYellowRow][addedYellowIndex]);
                        
                        //rek??rziv sz??m??t??s
                        recursiveCalculations(currentNode.childNodes[i], counter);
                        
                        //hozz??adot elemek t??rl??se
                        cpuCoinsList.remove(cpuCoinsList.size()-1);
                        cgs[addedRedRow][addedRedIndex] = null;
                        playerCoinsList.remove(playerCoinsList.size()-1);
                        cgs[addedYellowRow][addedYellowIndex] = null;
                    }
                }                
                //S??rg??k helyrerak??sa
                makePlayerCoinsList();
                //sz??l??nek ??tadni -- csak a max ??sszeg??t a gyerek sz??m??t a v??g??n kikeress??k majd neeem neeem
                setParentValues(currentNode.childNodes[i], currentNode, i);                              
            }            
            //??lehets??ges ??rt??keik kisz??m??t??sa - tov??bb??t??sa a sz??l??nek??
            
            //Ha sz??ks??ges rek??rz??v hiv??sa az elj??r??snak az adott elemre
            //korongok hozz??ad??s??val
            
            //esetlegesen m??dos??lt sz??mok feljuttat??sa a sz??l??h??z
            
            //hozz??adot korongok t??rl??se            
        }
        
        public void setParentValues(PV_Node child, PV_Node parent, int index) {
            if (fullAttack) { //csak a + sz??m??t
                if(child.predictedMin <= -2000 && child.predictedMax < 2000) {
                //if(child.predictedMin <= -1000 && child.predictedMax < 1000) {
                    //parent.predictedMax = -1;
                    //parent.predictedMaxIndex = index;
                    //??ll??tsuk delt??ra a fenti keres?? kiv??nalma alapj??n
                    //parent.delta = -1000000;
                    //parent.deltaIndex = index;
                } else {                    
                    if(child.predictedMax > parent.predictedMax){
                        parent.predictedMax = child.predictedMax;                    
                        parent.predictedMaxIndex = index;
                        parent.delta = child.predictedMax;
                        parent.deltaIndex = index;

                        parent.predictedMin = child.predictedMin;
                        parent.predictedMinIndex = child.predictedMinIndex;
                    }
                }
                //delta ??rt??kek ??tad??sa a sz??l??nek megt??rt??nt
            } else {
                //if(child.predictedMax >= 1000 * treeLevel) {
                if(child.predictedMax >= 2000 * treeLevel) {
                    parent.predictedMax = child.predictedMax;
                    parent.delta = 2000 * treeLevel;
                    parent.deltaIndex = index;
                } else {
                    if(child.delta > parent.delta) {
                        parent.delta = child.delta;
                        parent.deltaIndex = index;
                    }
                }
            }
        }
        
        public void treeLevelMultiplier(PV_Node child, int treeLevel) {
            //if(treeLevel >= 1 && child.predictedMax > 1000) {
            if(treeLevel >= 1 && child.predictedMax > 2000) {
                //child.predictedMax = child.predictedMax * 2;
                child.predictedMax = child.predictedMax * treeLevel;
            }
            //if(treeLevel >= 1 && child.predictedMin < 1000) {
            if(treeLevel >= 1 && child.predictedMin < 2000) {
                child.predictedMin = child.predictedMin * treeLevel;
            }
        }
        
        public int getMaxIndex() {
            return root.predictedMaxIndex;
        }
        
        public int getMaxDeltaIndex() {
            return root.deltaIndex;
        }
        
        public void calculatePredictedValues(PV_Node node, int redIndex) {
            //Cpu Max calculation
            //currentColumn = redIndex ugyib??r
            if (cgs[5][redIndex] == null) {
                int currentRow = searchRow(redIndex);

                //hozz??adni egy coint ha lehets??ges
                cgs[currentRow][redIndex] = new Coin(currentRow, redIndex, Colors.red);
                cpuCoinsList.add(cgs[currentRow][redIndex]);

                //kisz??mol??s
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
                node.predictedMax = fieldValueSum;
                node.predictedMaxIndex = redIndex;

                //T??rl??s el??tt megn??zz??k ebben a helyzetben a lehets??ges minuszokat
                calculatePredictedMinimums(node);

                //delta kisz??m??t??sa
                node.delta = node.predictedMax + node.predictedMin;

                //t??r??lni a hozz??adott red coin-t
                cpuCoinsList.remove(cpuCoinsList.size()-1);
                cgs[currentRow][redIndex] = null;
            } else {
                node.predictedMax = -1;
                node.predictedMaxIndex = redIndex;
                node.predictedMin = 1;
                node.predictedMinIndex = redIndex;
                node.delta = -1000000;
                node.deltaIndex = redIndex;
            }            
        }
        
        public void calculatePredictedMinimums(PV_Node node) {
            int minimumColumnIndex = 0;
            int minimumRowIndex = 0;
            
            //Listing the player's coins
            makePlayerCoinsList();
                        
            for (int j = 0; j < 7; j++) {
                if (cgs[5][j] == null) {        
                    minimumColumnIndex = j;
                    minimumRowIndex = searchRow(minimumColumnIndex);                
                    //hozz??adni egy coint
                    cgs[minimumRowIndex][minimumColumnIndex] = new Coin(minimumRowIndex, minimumColumnIndex, Colors.yellow);
                    playerCoinsList.add(cgs[minimumRowIndex][minimumColumnIndex]);

                    //kisz??mol??s
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
                    actualValuesList[j] = fieldValueSum;

                    playerCoinsList.remove(playerCoinsList.size()-1);
                    cgs[minimumRowIndex][minimumColumnIndex] = null;
                } else {
                    actualValuesList[j] = 1;
                }
            }
            
            for (int k = 0; k < 7; k++) {
                if (actualValuesList[k] < node.predictedMin) {
                    node.predictedMin = actualValuesList[k];
                    node.predictedMinIndex = k;                    
                }
            }
        }
        
        public void makePlayerCoinsList() {
            playerCoinsList.clear();
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 6; y++) {
                    if(cgs[y][x] != null && cgs[y][x].color == Colors.yellow)
                        playerCoinsList.add(cgs[y][x]);
                }
            }
        }
        
    }
    
    //Fa lev??lelemeinek oszt??lya
    public class PV_Node {
        int predictedMax;
        int predictedMaxIndex;
        int predictedMin;
        int predictedMinIndex;
        int delta;
        int deltaIndex;
        
        PV_Node parentNode;
        PV_Node[] childNodes;
            
        public PV_Node() {
            predictedMax = predictedMaxIndex = 0;
            predictedMin = predictedMinIndex = 0;
            delta = -1000000;
            deltaIndex = 0; //vagy -sok hogy hiba eset??n h??t??l szerepeljen
            
            parentNode = null;
            childNodes = new PV_Node[7];
            //childNodes[7] = null;
        }
    }

}
