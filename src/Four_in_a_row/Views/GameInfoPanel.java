/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Four_in_a_row.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


public class GameInfoPanel extends JPanel {
    JTextArea textArea;
    
    public GameInfoPanel() {
        setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Game infos:"));
        setBackground(Color.white);
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.ITALIC, 14));
        textArea.setForeground(new Color(37, 38, 39));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setPreferredSize(new Dimension(250, 730));
        areaScrollPane.setBorder(null);
        add(areaScrollPane);                
    }
    
    public void addLine(String line) {
        textArea.append(line);
    }
    
    public void clearTextArea() {
        textArea.setText("");
    }
    
}

