package util;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class Constraints {

	
	public static boolean isGoingIntoTartarus(Point p){
		if(p.x < 0 || p.x > 99 || p.y < 0 || p.y > 99)
			return true;
		return false;
	}
	


	
	
	
}
