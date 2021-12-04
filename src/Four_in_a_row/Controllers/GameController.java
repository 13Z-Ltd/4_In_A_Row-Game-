/*
 *
 */
package Four_in_a_row.Controllers;

import Four_in_a_row.Models.GameModel;
import Four_in_a_row.Views.MainWindow;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class GameController {
    MainWindow view;
    GameModel model;
    
    ActionListener buttonPressListener, comboBoxListener, gameSpeedBoxListener, depthOfTreeBoxListener;
    public Timer animationTimer;
    public Timer coinAnimationTimer;
    public int counter;
    public int animateCounter;
    public int animationSpeed;
    public int animationDistance;
    public boolean animationIsOn = false;
    public boolean cpuIsStarts = false;
    
    public int gameOver = 0;
    public int gameSpeedNumber = 6;
    
    public  GameController(GameModel model, MainWindow mainWindow) {
        this.model = model;
        this.view = mainWindow;
        
        view.gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    model.setCoinCoordinates(new Point(e.getX() - 32, e.getY() - 32));
                    model.playersCoin = true;
                    view.gamePanel.repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    int column = model.checkCoinLocation();
                    model.playersCoin = false;
                    view.gamePanel.repaint();
                    
                    if (column != -1) {
                        if (!animationIsOn)
                            animatePlayerAction(column);
                        //playerAction(column);
                    }                                        
                    model.playersCoin = false;             
                }
            }
        });
        
        view.gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
             @Override
             public void mouseDragged(MouseEvent e) {
                 if (MouseEvent.BUTTON1_DOWN_MASK != 0) {
                     model.setCoinCoordinates(new Point(e.getX() - 32, e.getY() - 32));
                     view.gamePanel.repaint();
                 }
             }
        });
        
        view.gamePanel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getPreciseWheelRotation() < 0) {
                    if (view.gameSpeedBox.getSelectedIndex() > 0) {
                        view.gameSpeedBox.setSelectedIndex(view.gameSpeedBox.getSelectedIndex() - 1);
                    }
                }
                if (e.getPreciseWheelRotation() > 0) {
                    if (view.gameSpeedBox.getSelectedIndex() < 4) {
                        view.gameSpeedBox.setSelectedIndex(view.gameSpeedBox.getSelectedIndex() + 1);
                    }
                }
            }
        });
        
        buttonPressListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton srcButton = (JButton)e.getSource();
                
                switch (srcButton.getName()){
                    case "Random":
                        model.cpuNextStep();
                        view.gamePanel.repaint();
                        break;
                    case "Drop":
                        droppingProcess();
                        break;
                    case "Restart":
                        newGameMethod();
                        break;
                    case "Hint":
                        model.btnHintisON = !model.btnHintisON;
                        //view.infoPanel.addLine(model.getInfoFromCGM());
                        view.gamePanel.repaint();
                        break;
                    case "Hint Plus":
                        view.infoPanel.addLine(model.getInfoFromCGM() + "\n");
                        view.gamePanel.repaint();
                        break;                        
                    case "Mode Selector":
                        if (view.btnModeSelector.getText() == "Delta") {
                            view.btnModeSelector.setText("Full Attack");
                            model.makeChangesInTheModel(1);
                            model.modeFullAttack = true;
                        }else {
                            view.btnModeSelector.setText("Delta");
                            model.makeChangesInTheModel(0);
                            model.modeFullAttack = false;
                        }
                        view.infoPanel.addLine(model.getInfoBeforeStep() + "\n");
                        view.gamePanel.repaint();
                        break;
                    case "NextStep":
                        animationTimer.stop();
                        cpuAction();
                        if (model.fourInARow)
                            startFourInARowMethod();
                        animationIsOn = false;
                        view.gamePanel.repaint();
                        break;
                    default:
                        break;
                }
            }
        };
        
        comboBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();                
                switch ((String)cb.getSelectedItem()){
                    case "2 Players":
                        model.currentGamingMode = "2 Players";
                        model.singlePlayer = false;
                        view.infoPanel.addLine("Kétjátékos mód!\n");
                        clearUnnecessaryControls();
                        break;
                    case "Last Coin AI":
                        model.currentGamingMode = "Last Coin AI";
                        model.singlePlayer = true;
                        view.infoPanel.addLine("ComboBox selected: " + (String)cb.getSelectedItem() + "!\n");
                        clearUnnecessaryControls();
                        newGameMethod();
                        break;
                    case "Value Checker AI":
                        model.currentGamingMode = "Value Checker AI";
                        model.singlePlayer = true;
                        view.infoPanel.addLine("ComboBox selected: " + (String)cb.getSelectedItem() + "!\n");
                        clearUnnecessaryControls();
                        newGameMethod();
                        //model.newGame();
                        break;
                    case "Game Field Value":
                        model.currentGamingMode = "Game Field Value";
                        model.singlePlayer = true;
                        view.infoPanel.addLine("ComboBox selected: " + (String)cb.getSelectedItem() + "!\n");
                        clearUnnecessaryControls();
                        newGameMethod();
                        break;
                    case "Min Max Algorithm":
                        model.currentGamingMode = "Min Max Algorithm";
                        model.singlePlayer = true;
                        view.infoPanel.addLine("ComboBox selected: " + (String)cb.getSelectedItem() + "!\n");
                        clearUnnecessaryControls();
                        view.btnHintPlus.setVisible(true);
                        view.mainWindowFrame.repaint();
                        newGameMethod();
                        break;
                    case "Min Max Pro":
                        model.currentGamingMode = "Min Max Pro";
                        model.singlePlayer = true;
                        view.infoPanel.addLine("ComboBox selected: " + (String)cb.getSelectedItem() + "!\n");
                        view.btnModeSelector.setVisible(true);
                        view.btnHintPlus.setVisible(false);
                        view.depthLabel.setVisible(true);
                        view.depthOfTreeBox.setVisible(true);
                        newGameMethod();
                        break;    
                    default:
                        
                        break;
                }
            }
        };
        
        gameSpeedBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();                
                switch ((String)cb.getSelectedItem()){
                    case "1":
                        gameSpeedNumber = 2;
                        break;
                    case "3":
                        gameSpeedNumber = 6;
                        break;
                    case "5":
                        gameSpeedNumber = 10;
                        break;
                    case "7":
                        gameSpeedNumber = 16;
                        break;
                    case "Stop":
                        gameSpeedNumber = 50;
                        break;
                }
            }
        };
        
        depthOfTreeBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();                
                switch ((int)cb.getSelectedItem()){
                    case 1:
                        model.makeChangesInTheModel(11);
                        model.modeTreeLevel = 1;
                        break;
                    case 2:
                        model.makeChangesInTheModel(12);
                        model.modeTreeLevel = 2;
                        break;
                    case 3:
                        model.makeChangesInTheModel(13);
                        model.modeTreeLevel = 3;
                        break;
                    default:
                       break;
                }
            }
        };
        
        view.comboBox.addActionListener(comboBoxListener);
        view.btnRestart.addActionListener(buttonPressListener);
        view.btnRandom.addActionListener(buttonPressListener);
        view.btnInsert.addActionListener(buttonPressListener);
        view.btnHint.addActionListener(buttonPressListener);
        view.btnHintPlus.addActionListener(buttonPressListener);
        view.btnModeSelector.addActionListener(buttonPressListener);
        view.gameSpeedBox.addActionListener(gameSpeedBoxListener);
        view.btnNextStep.addActionListener(buttonPressListener);
        
        view.depthOfTreeBox.addActionListener(depthOfTreeBoxListener);
    }
    
    public void playerAction(int column) {
        if (!animationIsOn){
            if (model.dropCoin(column) == 0){
                view.infoPanel.addLine("Player drop:  " + (column + 1) + "  (" + model.getDroppedCoinsNumber() + ")\n");
                               
                view.gamePanel.repaint();                
                if (model.fourInARow)
                    startFourInARowMethod();                
                else {
                    view.infoPanel.addLine(model.getInfoBeforeStep());
                    view.gamePanel.repaint();
                    if (model.singlePlayer) {
                        animationIsOn = true;                        
                        waitForCpuAction();
                        //cpuAction();
                    } else {
                        model.changePlayer();
                    }
                }
            }else {
                view.infoPanel.addLine("That column is full please choose another one!\n");
            }
            
            if (model.getDroppedCoinsNumber() == 42) {
                gameOverEvent();            
            }
        }
    }
    
    public void cpuAction() {
        int columnNumber = model.cpuNextStep();
        animateCPUCoin(columnNumber);
        
        view.infoPanel.addLine("CPU drop:  " + columnNumber + "  (" + model.getDroppedCoinsNumber() + ")\n");
        view.infoPanel.addLine(model.getInfoFromCGM());
        view.gamePanel.repaint();
    }
    
    public void droppingProcess() {
        int column = tryParse(view.textField.getText()) - 1;
        if (column > -1 && column < 7)
            model.dropCoin(column);
        
        view.textField.setText("");
        view.gamePanel.repaint();
    }
        
    
    public void newGameMethod() {
        model.newGame();
        view.infoPanel.clearTextArea();
        view.infoPanel.addLine("Start a new Game!\n");
        cpuIsStarts = !cpuIsStarts;
        if (cpuIsStarts &&  model.singlePlayer) {            
            animationIsOn = true;                        
            waitForCpuAction();
        }
        view.gamePanel.repaint();
    }
    
    public void gameOverEvent() {
        view.infoPanel.addLine("GameOver! Please start a new one!\n");
        int response = JOptionPane.showConfirmDialog(null, "Game Over\nThe game field is full!\nWould you like to start a new game?", "New Game",
                    JOptionPane.YES_NO_CANCEL_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            newGameMethod();
        }
    }
    
    public void startFourInARowMethod( ) {
        String dialogString = "4 in a Row!!!\nThe Winner is: " + model.winner + "\nWould you like to start a new game?";
        int response = JOptionPane.showConfirmDialog(null, dialogString, "Win",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            newGameMethod();
        }
    }
    
    public void waitForCpuAction(){        
         model.animatedCoinYellow = false;
        counter = 0;
        animationTimer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if (counter == gameSpeedNumber) {
                    ((Timer) e.getSource()).stop();
                    //animationIsOn = false;
                    animationTimer.stop();
                    cpuAction();
                    if (model.fourInARow)
                            startFourInARowMethod();
                }
            }
        });
        animationTimer.start();
    }
    
    public void animateCPUCoin(int columnForAnimation) {
        animateCounter = 0;
        model.coinCordinate_X = 900;
        model.coinCordinate_Y = 100;
        
        int rowForAnimation = model.coinToAdd.row;        
        int coinRowDistance = 600 - rowForAnimation*80;
        int coinDistance = (900 - (200 + columnForAnimation * 100)) / 30;
        int coinTarget_X = 200 + columnForAnimation * 100;
        
        model.animatedCoinVisible = true;
        model.animatedCoinYellow = false;
        
        animationSpeed = 30;
        animationDistance = 0;
        coinAnimationTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateCounter++;
                //model.coinCordinate_X = model.coinCordinate_X - coinDistance;
                if(animateCounter < 31) {
                    model.coinCordinate_X -= coinDistance;
                    if (animateCounter == 30)
                        model.coinCordinate_X = coinTarget_X;
                } else {
                    //if (model.coinCordinate_Y < coinRowDistance){
                    if ((model.coinCordinate_Y + animationSpeed) < coinRowDistance){
                        animationSpeed += 2;
                        model.coinCordinate_Y += animationSpeed;
                    }else {
                        model.coinCordinate_Y = coinRowDistance;
                        ((Timer) e.getSource()).stop();
                    
                        coinAnimationTimer.stop();
                        animationIsOn = false;
                        model.animatedCoinVisible = false;
                        
                        model.waitforAddCoin();                        
                    }
                }
                view.gamePanel.repaint();
                if (model.fourInARow)
                            startFourInARowMethod();
            }
        });
        coinAnimationTimer.start();
    }
    
    public void animatePlayerAction(int column) {
        animateCounter = 0;
        model.coinCordinate_X = 200 + column * 100;//900;
        model.coinCordinate_Y = 100;
        model.animatedCoinYellow = true;
        model.animatedCoinVisible = true;
        animationDistance = 0;
        animationSpeed = 30;
        animationIsOn = true;
        
        int i = 0;
        for (i = 0; i < 6; i++) {
            if (model.gameSpace[i][column] == null) {
                animationDistance = 600 - i*80;
                break;
            }
        }       
        
        coinAnimationTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateCounter++;
                if (model.coinCordinate_Y + animationSpeed < animationDistance) {
                        animationSpeed += 2;
                        model.coinCordinate_Y += animationSpeed;
                } else {
                    ((Timer) e.getSource()).stop();
                    
                    coinAnimationTimer.stop();
                    animationIsOn = false;
                    model.animatedCoinVisible = false;
                    playerAction(column);
                }
                view.gamePanel.repaint();
                //if (model.fourInARow)
                //startFourInARowMethod();
            }
        });
        coinAnimationTimer.start();
    }
    
    public int tryParse(String text) {
        try {
            int insertNumber = Integer.parseInt(view.textField.getText());            
                return insertNumber;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void clearUnnecessaryControls() {
        view.btnModeSelector.setVisible(false);
        view.btnHintPlus.setVisible(false);
        view.depthLabel.setVisible(false);
        view.depthOfTreeBox.setVisible(false);
    }
}