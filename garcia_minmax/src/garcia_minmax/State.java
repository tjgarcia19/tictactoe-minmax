package garcia_minmax;

public class State {
	public int[][] board;
	public State parent;
	public State children;
	public int utility;
	public int alpha;
	public int beta;
	public int i;
	public int j;
	public boolean isReturning;
	
	public State(int[][] puzz, State parent) {
		this.board = new int[3][3];
		this.board = puzz;
		this.parent = parent;
		this.children = null;
		this.utility = -9999;
		this.alpha = -9999;
		this.beta = 9999;
		this.isReturning = false;
	}
	
	public State(int[][] puzz, State parent, int i, int j) {
		this.board = new int[3][3];
		this.board = puzz;
		this.parent = parent;
		this.children = null;
		this.utility = -9999;
		this.alpha = -9999;
		this.beta = 9999;
		this.i = i;
		this.j = j;
		this.isReturning = false;
	}

	public State() {}
}