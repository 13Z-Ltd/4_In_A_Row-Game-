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
public class CPU_Random extends CPU_Modes{

    public CPU_Random(Coin[][] gameSpace) {
        cgs = gameSpace;
        //super.cgs = gameSpace;
    }

    @Override
    protected Coin cpuNextCoin() {
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
