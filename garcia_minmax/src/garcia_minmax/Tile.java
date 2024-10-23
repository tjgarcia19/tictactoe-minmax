package garcia_minmax;

public class Tile extends Sprite{
	private String name;

	public Tile(double xPos, double yPos, String name) {
		super(xPos,yPos);
		this.name = name;
		this.loadImage("images/"+name+".jpg",Board.CELL_WIDTH,Board.CELL_HEIGHT);
	}

	//getter method
	String getName(){
		return this.name;
	}
}
