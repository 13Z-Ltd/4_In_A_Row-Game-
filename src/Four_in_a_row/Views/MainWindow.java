/*
 * 
 */
package Four_in_a_row.Views;

import Four_in_a_row.Models.GameModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Balla
 */
public class MainWindow {
    public GameModel gameModel;
    public GamePanel gamePanel;
    public GameInfoPanel infoPanel;
    public JFrame mainWindowFrame;
    
    public JButton btnInsert;
    public JButton btnRandom;
    public JButton btnRestart;
    public JButton btnHint;
    public JButton btnHintPlus;
    public JButton btnModeSelector;
    public JButton btnNextStep;
    public JTextField textField;
    public JComboBox comboBox, gameSpeedBox, depthOfTreeBox;
    public JLabel depthLabel;
    
    public String GamingModes[] = {"Last Coin AI" ,"Value Checker AI", "Game Field Value", "Min Max Algorithm", "Min Max Pro", "2 Players"};
    public String gameSpeeds[] = {"1", "3", "5", "7", "Stop"};    
    
    public MainWindow(GameModel model) {
        this.gameModel = model;
        
        mainWindowFrame = new JFrame("4 in a Row game");
        mainWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindowFrame.setLayout(new BorderLayout());                
        mainWindowFrame.setSize(new Dimension(1400, 900));
        mainWindowFrame.setLocationRelativeTo(null);
        mainWindowFrame.setBackground(Color.WHITE);
        //mainWindowFrame.add(gamePanel, BorderLayout.CENTER);
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Options"));
        optionsPanel.setBackground(Color.white);
        JLabel label = new JLabel("Key: ");
        
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(100,40));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        btnRestart = new JButton("New Game");
        btnRestart.setName("Restart");
        btnRestart.setFocusable(false);
        btnRandom = new JButton("Random");
        btnRandom.setName("Random");
        btnRandom.setFocusable(false);
        btnInsert = new JButton("Drop");
        btnInsert.setName("Drop");        
        btnInsert.setFocusable(false);
        
        btnHint = new JButton("Hint");
        btnHint.setName("Hint");
        btnHint.setFocusable(false);
        btnHintPlus = new JButton("Hint Plus");
        btnHintPlus.setName("Hint Plus");
        btnHintPlus.setFocusable(false);
        btnHintPlus.setVisible(false);
        btnModeSelector = new JButton("Delta");
        btnModeSelector.setName("Mode Selector");
        btnModeSelector.setFocusable(false);
        btnModeSelector.setVisible(true);
        btnNextStep = new JButton("Next Step");
        btnNextStep.setName("NextStep");
        btnNextStep.setFocusable(false);
        
        depthLabel = new JLabel(" Depth: ");
        depthOfTreeBox = new JComboBox();
        depthOfTreeBox.setName("depthOfTreeBox");
        depthOfTreeBox.addItem(1);
        depthOfTreeBox.addItem(2);
        depthOfTreeBox.addItem(3);
        depthOfTreeBox.setSelectedItem(2);
        
        JLabel speedLabel = new JLabel("   Speed: ");
        
        gameSpeedBox = new JComboBox(gameSpeeds);
        gameSpeedBox.setName("speedBox");
        gameSpeedBox.setSelectedIndex(1);
        gameSpeedBox.setBounds(50, 50, 90, 30);
        
        //btnInsert.setMnemonic(KeyEvent.VK_I);
        //btnInsert.setToolTipText("The inserted key must be an integer between 0 and 999!");
        optionsPanel.add(btnRestart);
        //optionsPanel.add(btnRandom);
        //optionsPanel.add(btnInsert);
        //optionsPanel.add(label);
        //optionsPanel.add(textField);
        optionsPanel.add(new JLabel("    "));
        optionsPanel.add(btnHint);
        optionsPanel.add(btnHintPlus);
        optionsPanel.add(new JLabel("    "));
        optionsPanel.add(btnModeSelector);
        optionsPanel.add(depthLabel);
        optionsPanel.add(depthOfTreeBox);
        optionsPanel.add(speedLabel);
        optionsPanel.add(gameSpeedBox);
        optionsPanel.add(btnNextStep);
        
        comboBox = new JComboBox(GamingModes);
        comboBox.setName("comboBox");
        //comboBox.setBounds(50, 50, 90, 50);
        comboBox.setFont(new Font("SansSerif", Font.BOLD, 16));
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        comboBox.setRenderer(listRenderer);
        comboBox.setSelectedIndex(4);
        infoPanel = new GameInfoPanel();
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(comboBox);
        rightPanel.add(infoPanel);
        
        gamePanel = new GamePanel(gameModel);
        
        mainWindowFrame.add(gamePanel, BorderLayout.CENTER);
        mainWindowFrame.add(optionsPanel, BorderLayout.SOUTH);
        mainWindowFrame.add(rightPanel, BorderLayout.EAST);       
        //mainWindowFrame.add(infoPanel, BorderLayout.EAST);               
        //mainWindowFrame.pack();
        mainWindowFrame.setVisible(true);        
    }    
}
