/*
 * 
 */
package Four_in_a_row.Models;

/**
 *
 * @author Balla
 */
enum Colors {
    red, yellow, green_red, green_yellow;
}

public class Coin {
    public int column;  //oszlop
    public int row;     //sor
    public Colors color;
    
    //number of the neighbours
    public int horizontal, vertical;
    public int northEast_JF, northWest_BF;
    public int coinValue;
    public int absoluteValue;
    //possible numbers
    public int left, left_down, down, right_down, right, left_up, right_up;
    public int H, V, NE, NW, SUM;
    
    
    public Coin (int row, int column, Colors color) {        
        this.row = row;
        this.column = column;
        this.color = color;
        
        horizontal = vertical = northEast_JF = northWest_BF = 1;
        //coinValue = 1;
        coinValue = 0;
        absoluteValue = 0;
    }
    
    public Coin (int row, int column, int player) {        
        this.row = row;
        this.column = column;
        if (player == 1)
            this.color = Colors.yellow;
        else
            this.color = Colors.red;
        
        horizontal = vertical = northEast_JF = northWest_BF = 1;
        coinValue = 1;
    }
}
