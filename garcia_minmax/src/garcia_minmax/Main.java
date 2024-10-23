package garcia_minmax;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage){
		StartGame game = new StartGame();
		game.setStage(stage);
	}

}
