// TCSS 342 
// Maze Generator 

/**
 * @author Arrunn Chhouy
 * @version 1.0
 * 
 * This class is the driver class. 
 */
public class Main {
	
	// Main generates three mazes. First maze is the one specified by the requirements. Second maze is a larger 
	// maze to test scale. Third maze is a non square maze to test non regular sizing. 
	// For each maze, the solution path is also printed out so that we can test the correctness and accuracy 
	// of solution. 
	public static void main(String[] args) {
		
		// Generate first maze of specified size 5x5
		Maze test1 = new Maze(5, 5, true);
		
		// Generate a larger maze 
		Maze test2 = new Maze(10, 10, false);
		
		// Generate a rectangular maze for non standard size testing 
		Maze test3 = new Maze(10,20, false);
	}
}
