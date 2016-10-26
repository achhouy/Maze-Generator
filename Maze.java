// TCSS 342 
// Maze Generator 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author Arrunn Chhouy
 * @version 1.0
 * 
 * This class implements a maze using depth first search 
 */public class Maze {	
	/* Graph*/
	private Node[][] G;
	/* Width*/
	private int width;
	/* Depth*/
	private int depth;
	/* Whether or not debug is on*/
	private boolean debug;
	/* Reference to entrance of the maze*/
	private Node entrance;
	/* Reference to the exit */
	private Node exit;
	/* Representation of the maze with the walls*/
	private char[][] x;   
	/* Solution of the maze*/
	private char[][] solution; 
	/* collection to store solution path*/
	private ArrayList<int[]> path;
	
	/**
	 * Constructs the maze with given width, depth, and whether or not debug mode is on.
	 * @param width
	 * @param depth
	 * @param debug
	 */
	public Maze(int width, int depth, boolean debug) {
		this.width = width; 
		this.depth = depth;
		this.debug = debug;
		path = new ArrayList<int[]>();
		G = new Node[depth][width];
		x = new char[ 2 * depth + 1][2 * width + 1];
		solution = new char[ 2 * depth + 1][2 * width + 1];
		setArea(depth, width);
		buildMaze();
		solve();
	}
	
	/**
	 * Initializes the maze perimeters 
	 * 
	 * @param depth is an int of the depth of the maze
	 * @param width is an int of the widht of the maze
	 */
	private void setArea(int depth, int width) {
		for(int i = 0; i < depth; i++) {
			for (int j = 0; j < width; j++) {
				G[i][j] = new Node(i,j);
			}
		}
		for(int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (i % 2 != 0 && j % 2 != 0) {
					x[i][j] = ' ';
					solution[i][j] = ' ';
				} else {
					x[i][j] = 'o';
					solution[i][j] = 'o';
				}
			}
			System.out.println();
		}
		entrance = G[0][0];
		exit = G[depth - 1][width - 1];
	}
	
	/**
	 * Builds the maze with walls down to create paths 
	 */
	private void buildMaze() {
		Stack<Node> stack = new Stack<>();
		stack.push(entrance);
		solution[1][1] = 'V';
		solution[0][1] = ' ';
		solution[(depth - 1) * 2 + 2][(width - 1) * 2 + 1] = ' ';
		x[1][1] = 'V';
		x[0][1] = ' ';
		x[(depth - 1) * 2 + 2][(width - 1) * 2 + 1] = ' ';
		Node current = entrance;
		ArrayList<int[]> neighborLocation = new ArrayList<int[]>();
		while(!stack.isEmpty()) {
			current.setVisited(true);
			// Finds all the location of the neighbor nodes to the current
			neighborLocation = neighbors(current);
			
			// Remove elements that have been visited already
			for (Iterator<int[]> iterator = neighborLocation.iterator(); iterator.hasNext();) {
			    int[] temp = iterator.next();
			    if (G[temp[0]][temp[1]].visited) {
			        // Remove the current element from the iterator and the list.
			        iterator.remove();
			    }
			}
			int[] temp = null;
			
			if(!neighborLocation.isEmpty()) {
				// Grab location of the neighbor
				temp = neighborLocation.get(0);
				int[] exitPosition = exit.getPosition();
				if(temp[0] == exitPosition[0] && temp[1] == exitPosition[1]) {
				    gatherPath(stack);
				}
				// Adds to the stack
				stack.push(G[temp[0]][temp[1]]);
				x[temp[0] * 2 + 1][temp[1] * 2 + 1] = 'V';
				
				int[] cur = current.getPosition();
				// East 
				if(cur[0] == temp[0] && cur[1] < temp[1]) {
					x[temp[0] * 2 + 1][temp[1] * 2] = ' ';
					solution[temp[0] * 2 + 1][temp[1] * 2] = ' ';
				// West	
				} else if(cur[0] == temp[0] && cur[1] > temp[1]) {
					x[cur[0] * 2 + 1][cur[1] * 2] = ' ';
					solution[cur[0] * 2 + 1][cur[1] * 2] = ' ';
				// West	
				} else if(cur[0] < temp[0] && cur[1] == temp[1]) {
					x[temp[0] * 2][temp[1] * 2 + 1] = ' ';
					solution[temp[0] * 2][temp[1] * 2 + 1] = ' ';
				// North	
				} else if(cur[0] > temp[0] && cur[1] == temp[1]) {
					x[cur[0] * 2][cur[1] * 2 + 1 ] = ' ';
					solution[cur[0] * 2][cur[1] * 2 + 1 ] = ' ';
				}
				
				if(debug) {
					display();
				}
				// Sets current to the new neighbor node
				current = stack.peek();
			} else {
				stack.pop();
				if(!stack.isEmpty()) {
					current = stack.peek();
				}
			}
		}
	}
	
	/**
	 * Adds the location of all the nodes that was used to get to the end node
	 * @param stack
	 */
	private void gatherPath(Stack<Node> stack) {
		Stack<Node> temp = new Stack<Node>();
		int[] position;
		while(!stack.isEmpty()) {
			temp.push(stack.pop());
			position = temp.peek().getPosition();
			path.add(position);
		} 
		while(!temp.isEmpty()) {
			stack.push(temp.pop());
		}
	}
	
	/**
	 * Sets the path of the solution array
	 */
	private void solve() {
		int i;
		int[] temp;
		int[] exitPosition = exit.getPosition();
		ArrayList<int[]> neighbor = neighbors(exit);
		for(i = 0; i < path.size(); i++) {
			temp = path.get(i);
			System.out.println("Path : " + temp[0] + ", " + temp[1]);
			solution[temp[0] * 2 + 1][temp[1] * 2  + 1] = 'S';
			if(temp[0] == exitPosition[0] && temp[1] == exitPosition[1]) {
				solution[temp[0] * 2  + 1][temp[1] * 2 + 1] = 'S';
			}
		}
		solution[(depth - 1) * 2 + 1][(width - 1) * 2 + 1] = 'S';
		printSolution();
	}
	
	/**
	 * Prints the solution of the path to get to the end of the maze
	 */
	private void printSolution() {
		for ( int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (i % 2 != 0 && j % 2 != 0) {
					System.out.print("" + solution[i][j]);
				} else {
					System.out.print("" + solution[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Retrieve the nodes neighbor's positions
	 * @param current
	 * @return an arraylist of neighbors
	 */
	private ArrayList<int[]> neighbors(Node current) {
		ArrayList<int[]> location = new ArrayList<int[]>();
		int[] coordinates = current.getPosition();
		int[] temp = new int[2];
		int[] temp2 = new int[2];
		int[] temp3 = new int[2];
		int[] temp4 = new int[2];
		// Starting corner piece
		if(coordinates[0] == 0 && coordinates[1] == 0) {
			temp[0] = coordinates[0] + 1;
			temp[1] = coordinates[1];
			location.add(temp);
			temp2[0] = coordinates[0];
			temp2[1] = coordinates[1] + 1;
			location.add(temp2);
		// Top Row	
		} else if(coordinates[0] == 0 && coordinates[1] < width - 1) {
			temp[0] = coordinates[0] + 1;
			temp[1] = coordinates[1];
			location.add(temp);
			if(coordinates[1] + 1 < width) {
				temp2[0] = coordinates[0];
				temp2[1] = coordinates[1] + 1;
				location.add(temp2);
			}
			if(coordinates[1] - 1 >= 0) {
				temp3[0] = coordinates[0];
				temp3[1] = coordinates[1] - 1;
				location.add(temp3);
			}
		// Bottom row	
		} else if(coordinates[0] == depth - 1 && coordinates[1] == 0) {
			temp[0] = coordinates[0];
			temp[1] = coordinates[1] + 1;
			location.add(temp);
			if(coordinates[0] + 1 < depth - 1) {
				temp2[0] = coordinates[0] + 1;
				temp2[1] = coordinates[1];
				location.add(temp2);
			}
			if(coordinates[0] - 1 > 0) {
				temp3[0] = coordinates[0] - 1;
				temp3[1] = coordinates[1];
				location.add(temp3);
			}
		}
		// Bottom corner piece
		else if(coordinates[0] == depth - 1 && coordinates[1] == width - 1) {
			temp[0] = coordinates[0] - 1;
			temp[1] = coordinates[1];
			location.add(temp);
			temp2[0] = coordinates[0];
			temp2[1] = coordinates[1] - 1;
			location.add(temp2);
		// Non-border pieces	
		} else {
			if(coordinates[0] + 1 <= depth - 1) {
				temp[0] = coordinates[0] + 1;
				temp[1] = coordinates[1];
				location.add(temp);
			}
			if(coordinates[1] + 1 <= width - 1) {
				temp2[0] = coordinates[0];
				temp2[1] = coordinates[1] + 1;
				location.add(temp2);
			} 
			if(coordinates[0] - 1 >= 0) {
				temp3[0] = coordinates[0] - 1;
				temp3[1] = coordinates[1];
				location.add(temp3);
			}
			if(coordinates[1] - 1 >= 0) {
				temp4[0] = coordinates[0];
				temp4[1] = coordinates[1] - 1;
				location.add(temp4);
			}
		}
		Collections.shuffle(location);
		return location;
	}

	/**
	 * Prints out a string representation of the maze
	 */
	public void display() {
		for ( int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (i % 2 != 0 && j % 2 != 0) {
					System.out.print("" + x[i][j]);
				} else {
					System.out.print("" + x[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	

	/**
	 * A node class used to build the maze
	 * 
	 * @author Arrunn Chhouy
	 * @author Matthew Wu
	 */
	class Node {
		private int position[];// position of node in maze
		private boolean visited = false;
		private boolean isPath = false;		

		// Constructs a node with a specific position
		public Node(int x, int y) {
			position = new int[] { x, y };
		}

		// Sets the path for the node 
		public void setPath(boolean path) {
			this.isPath = path;
		}
		
		// Gets a node's path
		public boolean getPath() {
			return isPath;
		}
		
		// Sets whether or not the node has been visited 
		public void setVisited(boolean value) {
			visited = value;
		}
		
		// Gets the visit status of this node 
		public boolean visit() {
			return visited;
		}
		// Gets a node's position
		public int[] getPosition() {
			return position;
		}
	}
}