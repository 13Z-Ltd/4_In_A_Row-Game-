/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Models;

import java.awt.Graphics2D;

/**
 *
 * @author Balla
 */
public class CPU_LastDrop extends CPU_Modes{    
    Coin lastCoin;
    int randomDirection;

    public CPU_LastDrop(Coin[][] gameSpace) {
        cgs = gameSpace;
        lastCoin = null;
        randomDirection = 0;
    }    
    

    @Override
    protected Coin cpuNextCoin() {
        //first step
        if (lastCoin == null) {
            int startColumn = (int) ((Math.random() * 3) + 2);   //2-4 ig
            if(cgs[5][startColumn] == null) {
                int i;
                for (i = 0; i < 6; i++) {
                    if (cgs[i][startColumn] == null) {
                        break;
                    }
                }
                lastCoin = new Coin(i, startColumn, Colors.red);
                return lastCoin;
            }
            return null;
        } else {
           searchingProcess();
           return lastCoin;
        }
    }
    
    private void searchingProcess() {
        //hibakezelés kell majd még!!!
        randomDirection = (int) (Math.random() * 3) - 1;
        int attempts = 0;
        while (checkingDirection() == false && attempts < 9) {
            changeDirection();
            attempts++;
        }
        if (attempts >= 8) {
            lastCoin = randomCoin();
        } else {
            lastCoin.column += randomDirection;
            lastCoin.row = searchRow( lastCoin.column);
        }
    }
    
    private boolean checkingDirection() {
        int checkedColumn = lastCoin.column + randomDirection;
        if(checkedColumn >= 0 && checkedColumn <= 6 && cgs[5][checkedColumn] == null) {
            return true;
        } else
            return false;
    }
    
    private void changeDirection() {
        switch (randomDirection) {
            case -1:
                randomDirection = 0;
                break;
            case 0:
                randomDirection = 1;
                break;
            case 1:
                randomDirection = -1;
                break;
            default:
                randomDirection = -1;
        }
    }

    @Override
    protected String getInfo() {
        return "";
    }

    @Override
    protected String getInfoBeforeStep() {
        return "";
    }

    @Override
    protected void paintModelHints(Graphics2D g2d) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void makeChangesInTheModel(int x) {
        
    }
}
