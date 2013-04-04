package util;

import java.awt.Point;

import iceworld.Constants;
import iceworld.States;

public class Constraints {
	
	public static boolean isDeltaInBound(int increment, int direction) {
		if (direction == 0) { // UP
			if (States.deltaY - increment >= 0)
				return true;
		} else if (direction == 1) { // DOWN
			if (States.deltaY + increment
					+ Constants.ICEWORLD_VIEWPORT_SIZE.height <= Constants.WORLD_SIZE.height)
				return true;
		} else if (direction == 3) { // RIGHT
			if (States.deltaX + increment
					+ Constants.ICEWORLD_VIEWPORT_SIZE.width <= Constants.WORLD_SIZE.width)
				return true;
		} else { // LEFT
			if (States.deltaX - increment >= 0)
				return true;
		}
		return false;
	}
	
	public static boolean isGoingIntoTartarus(Point p){
		if(p.x < 0 || p.x > 99 || p.y < 0 || p.y > 99)
			return true;
		return false;
	}

	
	
	
}
