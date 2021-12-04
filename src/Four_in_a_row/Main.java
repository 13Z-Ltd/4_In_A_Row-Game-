/*
 *
 */
package Four_in_a_row;

import Four_in_a_row.Models.GameModel;
import Four_in_a_row.Views.MainWindow;
import Four_in_a_row.Controllers.GameController;

/**
 *
 * @author Balla
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GameModel gameModel = new GameModel();
        MainWindow mainWindow = new MainWindow(gameModel);
        GameController controller = new GameController(gameModel, mainWindow);
    }
    
}
