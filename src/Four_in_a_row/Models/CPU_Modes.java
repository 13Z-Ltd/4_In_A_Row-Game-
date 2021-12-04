/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Models;

//import Four_in_a_row.Models.Coin;

import java.awt.Graphics2D;


/**
 *
 * @author Balla
 */
abstract class CPU_Modes {
    protected Coin[][] cgs;    //currentGameSpace;
    
    //public CPU_Modes(Coin[][] gameSpace) {
    protected CPU_Modes() {
        //this.cgs = null;
    }

    //protected abstract void nextStep();
    
    protected abstract Coin cpuNextCoin();
    
    protected abstract String getInfo();
    protected abstract String getInfoBeforeStep();
    protected abstract void paintModelHints(Graphics2D g2d);
    protected abstract void makeChangesInTheModel(int x);
    
    public Coin randomCoin() {
        int i,j;
        int max = 500;
        while (max > 0) {
            j = (int) ((Math.random() * 7) );   //6 ig
            if(cgs[5][j] == null) {
                i = 0;
                for (i = 0; i < 6; i++) {
                    if (cgs[i][j] == null) {
                        break;
                    }
                }                
                return new Coin(i, j, Colors.red);
            } 
            max--;
        }
        return null;
    }
    
    public int searchRow(int column) {
        if (cgs[5][column] == null) {
            int i;
            for (i = 0; i < 6; i++) {
                if (cgs[i][column] == null) {
                    break;
                }
            }
            return i;
        } else 
            return -1; 
    }
}
