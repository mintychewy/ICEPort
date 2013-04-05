package iceworld;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import objects.Map;
import util.Camera;

public class Panner extends Camera{
	
	private final int DELTA_INC = 300;
	private final int UP = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;
	
	
	// public
	public int deltaX;
	public int deltaY;
	
	public Panner(){
		this.deltaX = 0;
		this.deltaX = 0;
	}
	
	public Panner(int deltaX, int deltaY){
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public boolean isDeltaInBound(int increment, int direction) {
		if (direction == 0) { // UP
			if (deltaY - increment >= 0)
				return true;
		} else if (direction == 1) { // DOWN
			if (deltaY + increment
					+ ICEWorldView.ICEWORLD_VIEWPORT_SIZE.height <= Map.WORLD_SIZE.height)
				return true;
		} else if (direction == 3) { // RIGHT
			if (deltaX + increment
					+ ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width <= Map.WORLD_SIZE.width)
				return true;
		} else { // LEFT
			if (deltaX - increment >= 0)
				return true;
		}
		return false;
	}
	
	public BufferedImage getWorldViewport(BufferedImage big){
		BufferedImage viewport = getSubImage(big,deltaX,deltaY);
		return viewport;
	}
	
	public void pan(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (isDeltaInBound(DELTA_INC, UP)) 
				deltaY -= DELTA_INC;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (isDeltaInBound(DELTA_INC, DOWN)) 
				deltaY += DELTA_INC;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (isDeltaInBound(DELTA_INC, RIGHT)) {
				deltaX += DELTA_INC;
			} else {
				// If next panning makes the camera view out of bound,
				// return the rightmost view possible
				deltaX = Map.WORLD_SIZE.width - ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (isDeltaInBound(DELTA_INC, LEFT)) {
				deltaX -= DELTA_INC;
			} else {
				// If next panning makes the camera view out of bound,
				// return the leftmost view possible
				deltaX = 0;
			}
		}
	}
}
