package iceworld;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import objects.World;

public class Panner {

	private BufferedImage fullImage;
	
	private int DELTA_INC;
	private final int UP = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;

	public Panner(BufferedImage fullImage) {
		DELTA_INC = (int)(100*ICEWorldView.zoom_factor);
		this.fullImage = fullImage;
	}


	public boolean isDeltaInBound(int increment, int direction) {
		if (direction == 0) { // UP
			if (ICEWorldView.deltaY - increment >= 0)
				return true;
		} else if (direction == 1) { // DOWN
			if (ICEWorldView.deltaY + increment + ICEWorldView.ICEWORLD_VIEWPORT_SIZE.height <= World.WORLD_SIZE.height)
				return true;
		} else if (direction == 3) { // RIGHT
			if (ICEWorldView.deltaX + increment + ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width)
				return true;
		} else { // LEFT
			if (ICEWorldView.deltaX - increment >= 0)
				return true;
		}
		return false;
	}

	public BufferedImage getWorldViewport() {
		BufferedImage viewport = fullImage.getSubimage(ICEWorldView.deltaX, ICEWorldView.deltaY,ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width,ICEWorldView.ICEWORLD_VIEWPORT_SIZE.height);
		return viewport;
	}

	
	public void pan(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (isDeltaInBound(DELTA_INC, UP))
				ICEWorldView.deltaY -= DELTA_INC;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (isDeltaInBound(DELTA_INC, DOWN))
				ICEWorldView.deltaY += DELTA_INC;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (isDeltaInBound(DELTA_INC, RIGHT)) {
				ICEWorldView.deltaX += DELTA_INC;
			} else {
				// If next panning makes the camera view out of bound,
				// return the rightmost view possible
				ICEWorldView.deltaX = World.WORLD_SIZE.width
						- ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (isDeltaInBound(DELTA_INC, LEFT)) {
				ICEWorldView.deltaX -= DELTA_INC;
			} else {
				// If next panning makes the camera view out of bound,
				// return the leftmost view possible
				ICEWorldView.deltaX = 0;
			}
		}
	}
}
