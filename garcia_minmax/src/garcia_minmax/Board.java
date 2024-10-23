package garcia_minmax;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Board {
	// these are the attributes of the class Board
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GridPane map;
	private int[][] board; // this will contain the Board
	private ArrayList<ImageView> cardCells;
	private ArrayList<Tile> Tile; // it contains 3 Tile; empty, O, X
	private State currentState;
	public int l;
	public int k; //Index
	public int type;
	public int move;

	// attributes for the UI dimensions
	public final static int MAX_CELLS = 9;
	public final static int MAP_NUM_ROWS = 3;
	public final static int MAP_NUM_COLS = 3;
	public final static int MAP_WIDTH = 350;
	public final static int MAP_HEIGHT = 350;
	public final static int CELL_WIDTH = 100;
	public final static int CELL_HEIGHT = 100;
	public final static int WINDOW_WIDTH = 400;
	public final static int WINDOW_HEIGHT = 600;

	public final Image bg = new Image("images/bg.jpg", 400, 600, false, false);
	
	//======================================================================================================================
	// constructor for class Board
	public Board() {
		this.root = new Group();
		this.scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT, Color.GRAY);
		this.canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.map = new GridPane();
		this.cardCells = new ArrayList<ImageView>();
		this.board = new int[MAP_NUM_ROWS][MAP_NUM_COLS];
		this.Tile = new ArrayList<Tile>();
		for (int i = 0; i < 3; i++) this.Tile.add(new Tile(0, 0, Integer.toString(i)));
	}
	
	//FOR SETTING THE UI
	//======================================================================================================================
	// method to add the stage elements
	public void setStage(Stage stage, int type) {		
		this.stage = stage;
		this.gc.drawImage(bg, 0, 0); // draw the background to the canvas at location x=0, y=0
		this.type = type+1;
		
		if(type==0) {//Type 0 - user first
			this.initGameBoard(); // initializing the boards, Board and goal arrays
			this.move = 2;
		}else {//Type 1 - AI first	 
			this.initGameBoard2();
			this.move = 1;
		}
		this.createMap();
		this.setGridPaneProperties();
		this.addGridPaneConstraints();
		if(type==1) disableEventHandler(this.cardCells.get(this.k*3+this.l));
		this.setCurrentState(null);

		// loop to add each imageView element to the gridpane/map
		for (ImageView cardCell : cardCells) this.map.getChildren().add(cardCell);

		// set stage elements here
		this.root.getChildren().add(canvas);
		this.root.getChildren().add(map);
		this.stage.setTitle("Tic-Tac-Toe");
		this.stage.setScene(this.scene);
		this.stage.show();
	}
	
	//BOARD INITIALIZATION AND UI PROPERTIES
	//======================================================================================================================
	// method to initialize the board Board and goal condition
	private void initGameBoard() {
		for(int i=0; i<MAP_NUM_ROWS; i++) {
			for(int j=0; j<MAP_NUM_COLS; j++) this.board[i][j] = 0;
		}
	}
	
	private void initGameBoard2() {
		Random rand = new Random();		//if player choose to play O, the program will randomly assigned the first move X 
		this.k = rand.nextInt(3);
		this.l = rand.nextInt(3);
		for(int i=0; i<MAP_NUM_ROWS; i++) {
			for(int j=0; j<MAP_NUM_COLS; j++) {
				if(i==k && j==l) this.board[i][j] = 1;
				else this.board[i][j] = 0;
			}
		}
	}

	// this will create imageViews for the image/Tile
	private void createMap() {
		for (int i = 0; i < MAP_NUM_ROWS; i++) {
			for (int j = 0; j < MAP_NUM_COLS; j++) {
				ImageView iv = new ImageView();
				addToStage(this.Tile.get(this.board[i][j]), iv);
				iv.setPreserveRatio(true);
				iv.setFitWidth(CELL_WIDTH);
				iv.setFitHeight(CELL_HEIGHT);
				iv.setId(Integer.toString(i) + "-" + Integer.toString(j)); // set id of the image view to i-j (e.g. id:
																			// 1-0 for element in row 1 col 0 )
				this.cardCells.add(iv); // add each image view created to the array list cardCells
			}
		}
	}

	// render the Tile on the stage
	private void addToStage(Tile elem, ImageView iv) {
		elem.render(iv);
		this.addMouseEventHandler(elem, iv); // add a mouse event handler to each tile
	}

	// method to set size and location of the grid pane
	private void setGridPaneProperties() {
		this.map.setPrefSize(MAP_WIDTH, MAP_HEIGHT);
		this.map.setLayoutX(WINDOW_WIDTH * .085);
		this.map.setLayoutY(WINDOW_WIDTH * .20);
	}

	// method to add row and column constraints of the grid pane
	private void addGridPaneConstraints() {
		// set number of rows
		for (int i = 0; i < MAP_NUM_ROWS; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(40);
			this.map.getRowConstraints().add(row);
		}

		for (int i = 0; i < MAP_NUM_COLS; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(40);
			this.map.getColumnConstraints().add(col);
		}

		// loop that will add the image views / land images to the gridpane
		int counter = 0;
		for (int row = 0; row < MAP_NUM_ROWS; row++) {
			for (int col = 0; col < MAP_NUM_COLS; col++) {
				GridPane.setConstraints(cardCells.get(counter), col, row);
				counter++;
			}
		}
	}

	// method to change the image view image, this is used for swapping images
	private void changeCellImage(ImageView iv, Tile elem) {
		Image i = elem.getImage();
		iv.setImage(i);
	}
	
	//HELPER FUNCTIONS
	//======================================================================================================================
	// This method will prompt the user whether they WIN or LOSE
	public void showPrompt(int Status) {
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if(Status==0) {
					Alert prompt = new Alert(AlertType.INFORMATION, "Draw!");
					prompt.show();
				}else if(Status==1){
					Alert prompt = new Alert(AlertType.INFORMATION, "You Win!");
					prompt.show();
				}else {
					Alert prompt = new Alert(AlertType.INFORMATION, "You Lose!");
					prompt.show();
				}
			}
		});
		pause.play();
	}
	
	//Same as showPrompt, the only difference is this will be used by the AI
	public void showPrompt2(int Status) {
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if(Status==0) {
					Alert prompt = new Alert(AlertType.INFORMATION, "Draw!");
					prompt.show();
				}else if(Status==1){
					Alert prompt = new Alert(AlertType.INFORMATION, "You Lose!");
					prompt.show();
				}else {
					Alert prompt = new Alert(AlertType.INFORMATION, "AI Win!");
					prompt.show();
				}
			}
		});
		pause.play();
	}
	
	//Saving the current state of the game
	public void setCurrentState(State parent) {
		State current = new State(this.board, parent);
		this.currentState = current;
	}
	
	//When a tile is clicked, it must no be clicked again
	public void disableEventHandler(ImageView iv) {
		iv.setOnMouseClicked(null);
	}
	
	//When AI or Player wins, the board must not be clickable
	public void disableAll() {
		for(int i=0; i<9; i++) this.cardCells.get(i).setOnMouseClicked(null);
	}
	
	// This method checks if the goal is already achieved
	public int checkGoal(int[][] board) {
		int zeroCounter = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(i==0) {
					if(j==0) {
						if(board[i][j]!=0 && board[i][j]==board[i+1][j+1] && board[i][j]==board[i+2][j+2]) return 1;
					}else if(j==2) {
						if(board[i][j]!=0 && board[i][j]==board[i+1][j-1] && board[i][j]==board[i+2][j-2]) return 1;
					}
					if(board[i][j]!=0 && board[i][j]==board[i+1][j] && board[i][j]==board[i+2][j]) return 1;
				}
				if(j==0) {
					if(board[i][j]!=0 && board[i][j]==board[i][j+1] && board[i][j]==board[i][j+2]) return 1;
				}
				if(board[i][j]==0) zeroCounter++;
			}
		}
		if(zeroCounter==0) return 0;
		return -1;
	}
	
	//If the player or AI wins, the player has an option to reset the game
	public void reset() {
		StartGame game = new StartGame();
		game.setStage(stage);
	}
	
	// this method counts all the possible move in the given board
	public int countMoves(int[][] board) {
		int counter = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) if(board[i][j]==0) counter++;
		}
		return counter;
	}
	
	// creating new boards for all possible actions to the current state
	public State createBoard(State current, int i, int type){
		int[][] nextState = new int[3][3];
		int zeroCount=0;
		int index_i = 0; int index_j = 0;
		for(int j=0; j<3; j++) {
			for(int k=0; k<3; k++) {
				if(current.board[j][k] == 0) {
					if(i==zeroCount) {
						nextState[j][k] = type;
						index_i = j;
						index_j = k;
					}else nextState[j][k] = current.board[j][k];
					zeroCount++;
				}else nextState[j][k] = current.board[j][k];
			}
		}
		State temp = new State(nextState, current, index_i, index_j);
		return temp;
	}
	
	// ALGORITHMS
	//======================================================================================================================
	//FINDING THE MAXIMUM VALUE
	public State maxValue(State current) {
		State maximum = new State(); 					//this will hold the maximum state
		int status = checkGoal(current.board);			//checks if the goal is achieved given the current state
		if(status!=-1) {								//if status is 0 (Draw) or 1 (Win), return the state
			current.utility = status*-1;				//since it is a terminal state, change utility
			current.isReturning = true;					//this is an indicator to be used during backtracking
			return current;
		}else {											//if it is not a terminal state, explore further
			int moveCount = countMoves(current.board); 	//count all possible moves
			State bestState = new State();
			bestState = current;
			State max = new State();
			max = current;
			State temp = new State();					//this will hold the child of the current state
			for(int i=0; i<moveCount; i++) {			// this loop will visit all the children of the current node and expands it
				temp = createBoard(current, i, this.move);
				if(max.isReturning) {					//this is for backtracking
					//the succeeding if blocks are for changing the alpha, beta and utility of the state
					if(max.utility>bestState.utility) {
						bestState.utility = max.utility;
						current.children = max;
						maximum = bestState;
					}
					if(max.utility>=bestState.beta) {	//this is for ALPHA-BETA PRUNNING
						bestState.isReturning = true;
						return maximum;					//if true, return the state and stop exploration
					}
					if(max.utility>bestState.alpha) bestState.alpha = max.utility;
				}
				temp.alpha = bestState.alpha;
				temp.beta = bestState.beta;
				temp.utility = 9999;
				
				max = minValue(temp);
			}
			// After the loop, we are sure that we got the maximum value
			// However, we still need to compare it to its parent to change its values
			if(max.utility>bestState.utility) {
				bestState.utility = max.utility;
				current.children = max;
				maximum = bestState;
			}
			if(max.utility>=bestState.beta) { 	//this is for ALPHA-BETA PRUNNING
				bestState.isReturning = true;
				return maximum;
			}
			if(max.utility>bestState.alpha) bestState.alpha = max.utility;
			bestState.isReturning = true;
			return maximum; 					//return the maximum state
		}
	}
	
	//FINDING THE MINIMUM VALUE
	//This method is similar to maxValue. Relational operators are reversed and some values have negative values
	//It works just like how the max works.
	public State minValue(State current) {
		State minimum = new State();
		int status = checkGoal(current.board);
		if(status!=-1) {
			current.utility = status;
			current.isReturning = true;
			return current;
		}else {
			int moveCount = countMoves(current.board);
			State bestState = new State();
			bestState = current;
			State min = new State();
			min = current;
			State temp = new State();
			for(int i=0; i<moveCount; i++) {
				temp = createBoard(current, i, this.type);
				if(min.isReturning) {
					if(min.utility<bestState.utility) {
						bestState.utility = min.utility;
						current.children = min;
						minimum = bestState;
					}
					if(min.utility<=bestState.alpha) { //this is for ALPHA-BETA PRUNNING
						bestState.isReturning = true;
						return minimum;
					}
					if(min.utility<bestState.beta) bestState.beta = min.utility;
				}
				temp.alpha = bestState.alpha;
				temp.beta = bestState.beta;
				temp.utility = -9999;
				
				min = maxValue(temp);
			}
			if(min.utility<bestState.utility) {
				bestState.utility = min.utility;
				current.children = min;
				minimum = bestState;
			}
			if(min.utility<=bestState.alpha) { //this is for ALPHA-BETA PRUNNING
				bestState.isReturning = true;
				return minimum;
			}
			if(min.utility<bestState.beta) bestState.beta = min.utility;
			bestState.isReturning = true;
			return minimum;
		}
	}
	
	// this method is the entry point for the minimax algorithm
	public void minimax() {
		State maximum = maxValue(this.currentState); //this will return the same state, however, it already know which children will give the maximum
		State temp = new State();
		temp = maximum.children;	//this will have the maximum state
		changeCellImage(this.cardCells.get(temp.i*3+temp.j), Tile.get(this.move)); 	//change the image corresponding to the action
		disableEventHandler(this.cardCells.get(temp.i*3+temp.j));					//disabling the event in that position
		board[temp.i][temp.j] = this.move;	//update the board
		setCurrentState(this.currentState);	//create a 	new state that will contain the default values and the current board
		int status = checkGoal(board); 		//checks if the goal is achieved
		if(status!=-1) {					//if yes, prompt the user
			showPrompt2(status);
			disableAll();
			Button reset = new Button("Play Again");
			reset.setLayoutX(125);
			reset.setLayoutY(500);
			reset.setPrefSize(150, 15);
			addEvent(reset);
			root.getChildren().add(reset);
		}
	}
	
	//EVENT HANDLERS
	//======================================================================================================================
	// event handler whenever a tile is clicked
	public void addMouseEventHandler(Tile elem, ImageView iv) {
		final ImageView tempIV = iv;
		iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				String[] indices = tempIV.getId().split("-");
				int i = Integer.parseInt(indices[0]);
				int j = Integer.parseInt(indices[1]);
				
				//this will reflect the action of the player
				changeCellImage(iv, Tile.get(type));
				board[i][j] = type;
				setCurrentState(currentState);
				disableEventHandler(iv);
				
				int status = checkGoal(board); 	//checks if the goal is achieved
				if(status!=-1) {				//player Won or Draw
					showPrompt(status);
					disableAll();
					Button reset = new Button("Play Again");
					reset.setLayoutX(125); reset.setLayoutY(500);
					reset.setPrefSize(150, 15);
					addEvent(reset);
					root.getChildren().add(reset);
				}else {							//the game is not yet over so its time for AI to do its move
					//pause for awhile to let the player see its move, then AI computes for the best action
					PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
					pause.setOnFinished(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent arg0) {
							minimax();
						}
					});
					pause.play();
				}		
			} // end of handle()
		});
	}
	
	// event handler for the play again button
	public void addEvent(Button reset) {
		reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				reset();
			}
		});
	}
}
//END OF CODE
//##########################################################################################################################