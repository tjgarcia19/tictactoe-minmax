package garcia_minmax;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartGame {
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	public final static int WINDOW_WIDTH = 400;
	public final static int WINDOW_HEIGHT = 600;

	public final Image bg = new Image("images/bg.jpg", 400, 600, false, false);
	public final Image logo = new Image("images/logo.png", 350, 450, false, false);
	
	public StartGame() {
		this.root = new Group();
		this.scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT, Color.GRAY);
		this.canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
	}
	
	//This is the initial game interface, allowing the user to choose what to play between O and X
	public void setStage(Stage stage) {
		Image x = new Image("images/1.jpg", 100, 100, false, false); // using images as buttons
		Image o = new Image("images/2.jpg", 100, 100, false, false);
		ImageView x_img = new ImageView(x);
		ImageView o_img = new ImageView(o);
		x_img.setLayoutX(75); o_img.setLayoutX(225);
		x_img.setLayoutY(450); o_img.setLayoutY(450);
		
		x_img.setOnMouseClicked(new EventHandler<MouseEvent>(){ // adding event handlers to the images
			public void handle(MouseEvent e) {
				startGame(0);
			}
		});
		
		o_img.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				startGame(1);
			}
		});
		
		this.stage = stage;
		this.gc.drawImage( bg, 0, 0 );
		this.gc.drawImage(logo, 25, 0);
		//set stage elements here
		this.root.getChildren().add(canvas);
		this.root.getChildren().add(x_img);
		this.root.getChildren().add(o_img);
		this.stage.setTitle("Tic-Tac-Toe");
		this.stage.setScene(this.scene);
		this.stage.show();
	}
	
	public void startGame(int type) {
		Board board = new Board();
		board.setStage(stage, type);
	}
}
