package iceworld;

import java.awt.event.KeyEvent;

import util.Constraints;

public class Panning {
	public static void pan(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (Constraints.isDeltaInBound(Constants.DELTA_INC, Constants.UP)) {
				States.deltaY -= Constants.DELTA_INC;
			} else {
				System.out.println("States.deltaY out of bound");
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (Constraints.isDeltaInBound(Constants.DELTA_INC, Constants.DOWN)) {
				States.deltaY += Constants.DELTA_INC;
			} else {
				System.out.println("States.deltaY out of bound");
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (Constraints.isDeltaInBound(Constants.DELTA_INC, Constants.RIGHT)) {
				States.deltaX += Constants.DELTA_INC;
			} else {
				System.out.println("States.deltaX out of bound");
				// If next panning makes the camera view out of bound,
				// return the rightmost view possible
				States.deltaX = Constants.WORLD_SIZE.width - 900;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (Constraints.isDeltaInBound(Constants.DELTA_INC, Constants.LEFT)) {
				States.deltaX -= Constants.DELTA_INC;
			} else {
				System.out.println("States.deltaX out of bound");
				// If next panning makes the camera view out of bound,
				// return the leftmost view possible
				States.deltaX = 0;
			}
		}
	}
}
