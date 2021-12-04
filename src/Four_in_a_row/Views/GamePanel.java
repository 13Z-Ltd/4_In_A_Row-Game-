/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Views;

import Four_in_a_row.Models.GameModel;
import java.awt.*;
import java.awt.Graphics2D;
import javax.swing.JPanel;

//import Four_in_a_row.Views.MainWindow;

/**
 *
 * @author Balla
 */
public class GamePanel extends JPanel {
    public GameModel model;
    
    public GamePanel(GameModel model){
        this.model = model;
        setSize(800, 600);
        //setPreferredSize(new Dimension(800, 600));
        //setBackground(new Color(29, 190, 252));      
        setBackground(new Color(75, 76, 76));
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //paintAnElement(g2d);
        model.paintGameSpace(g2d);
        model.paintPlayersCoin(g2d);        
    }
    
    
    /*
    public void paintAnElement(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        g2d.setColor(Color.red);
        g2d.fillOval(700, 400, 60, 60);
        g2d.setColor(Color.white);
        g2d.setFont(new Font("SansSerif", Font.BOLD , 15));
        g2d.drawString("10", 720, 430);
    }
*/
}
