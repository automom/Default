package edu.wm.cs.cs301.rxing.falstad;

import edu.wm.cs.cs301.rxing.falstad.Robot.Direction;
import edu.wm.cs.cs301.rxing.falstad.Robot.Turn;

/**
 * Wizard is an implementation of RobotDriver that extends DriverTemplate
 * Wizard is a RobotDriver that can cheat to solve the maze. Wizard has access to the distance matrix, which
 * allows it to see which paths will provide the shortest distance to the exit. Wizard observes each cell
 * next to it that it can move to, and looks for the shortest distance within these cells.
 * 
 * @author R
 *
 */
public class Wizard extends DriverTemplate{
	private int[] distances = new int[3];	
	private int[] currPosition = new int[2];
	private int[] currDirection = new int[2];
	

	@Override
	public void PrepToMove() {
		setUpWizard();
		distanceToExit();
		
		//Rotates around if there are walls on all three sides, and the robot cannot move to any of them
		if (distances[0] == Integer.MAX_VALUE && distances[1] == Integer.MAX_VALUE && distances[2] == Integer.MAX_VALUE){
			try {
				thisRobot.rotate(Turn.AROUND);
			} catch (Exception e) {
				thisRobot.hasStopped();
			}
		}
		//Rotates left if moving left has the shortest distance to the exit
		else if (distances[0] != Integer.MAX_VALUE && distances[0] <= distances [1] && distances[0] <= distances[2]){
			try {
				thisRobot.rotate(Turn.LEFT);
			} catch (Exception e) {
				thisRobot.hasStopped();
			}
		}
		//Does not rotate If moving forward has the shortest distance to the exit
		else if (distances[1] != Integer.MAX_VALUE && distances[1] <= distances[0] && distances[1] <= distances[2]){
		}
		//Rotates right if moving right has the shortest distance to the exit
		else{
			try {
				thisRobot.rotate(Turn.RIGHT);
			} catch (Exception e) {
				thisRobot.hasStopped();
			}
		}
		
		
	}
	
	/**
	 * Helper method that sets up the distance array and currentPosition array that will be used in the Wizard Driver
	 */
	private void setUpWizard() {
		/* Array of distances allow us to compare each of the possible movements, we do not need to move back
		 * because the algorithm should have already moved from that position, however in case the 
		 * maze starts off with the robot facing wrong direction, the algorithm will still account for this
		 * Index 0 represents the left move, index 1 represents forward, index 2 represents right
		 */
		distances[0] = Integer.MAX_VALUE;
		distances[1] = Integer.MAX_VALUE;
		distances[2] = Integer.MAX_VALUE;
		
		try {
			currPosition = thisRobot.getCurrentPosition();
		} catch (Exception e) {
			e.printStackTrace();
		}
		currDirection = thisRobot.getCurrentDirection();
	}
	
	/**
	 * Helper methods that determine the distances from each cell that the robot is able to move to
	 * if the robot is not able to move to a certain cell, the distance value remains at Integer.MAX_VALUE
	 * so that the robot can compare which distance is closer
	 */
	private void distanceToExit() {
		//UP
		if (currDirection[0] == 0 && currDirection[1] == 1){
			if (thisRobot.distanceToObstacle(Direction.LEFT) != 0){
				distances[0] = distance.getDistance(currPosition[0] - 1, currPosition[1]);
			}
			if (thisRobot.distanceToObstacle(Direction.FORWARD) != 0){
				distances[1] = distance.getDistance(currPosition[0], currPosition[1] + 1);
			}
			if (thisRobot.distanceToObstacle(Direction.RIGHT) != 0){
				distances[2] = distance.getDistance(currPosition[0] + 1, currPosition[1]);
			} 
		}
		//Down
		else if (currDirection[0] == 0 && currDirection[1] == -1){
			if (thisRobot.distanceToObstacle(Direction.LEFT) != 0){
				distances[0] = distance.getDistance(currPosition[0] + 1, currPosition[1]);
			}
			if (thisRobot.distanceToObstacle(Direction.FORWARD) != 0){
				distances[1] = distance.getDistance(currPosition[0], currPosition[1] - 1);
			}
			if (thisRobot.distanceToObstacle(Direction.RIGHT) != 0){
				distances[2] = distance.getDistance(currPosition[0] - 1, currPosition[1]);
			}
		}
		//RIGHT
		else if (currDirection[0] == 1 && currDirection[1] == 0){
			if (thisRobot.distanceToObstacle(Direction.LEFT) != 0){
				distances[0] = distance.getDistance(currPosition[0], currPosition[1] + 1);
			}
			if (thisRobot.distanceToObstacle(Direction.FORWARD) != 0){
				distances[1] = distance.getDistance(currPosition[0] + 1, currPosition[1]);
			}
			if (thisRobot.distanceToObstacle(Direction.RIGHT) != 0){
				distances[2] = distance.getDistance(currPosition[0], currPosition[1] - 1);
			}	
		}
		//LEFT
		else if (currDirection[0] == -1 && currDirection[1] == 0){
			if (thisRobot.distanceToObstacle(Direction.LEFT) != 0){
				distances[0] = distance.getDistance(currPosition[0], currPosition[1] - 1);
			}
			if (thisRobot.distanceToObstacle(Direction.FORWARD) != 0){
				distances[1] = distance.getDistance(currPosition[0] - 1, currPosition[1]);
			}
			if (thisRobot.distanceToObstacle(Direction.RIGHT) != 0){
				distances[2] = distance.getDistance(currPosition[0], currPosition[1] + 1);
			}		
		}
	}
}