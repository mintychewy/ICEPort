package util;

import java.awt.image.BufferedImage;
import objects.Map;

public class Patcher extends Camera {
	final int x;
	final int y;
	
	public Patcher(){
	 this.x = Map.WORLD_SIZE.width;
	 this.y = Map.WORLD_SIZE.height;
	}
	
	public Patcher(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public BufferedImage patch(BufferedImage sourceImage){
		BufferedImage patch = getSubImage(sourceImage, x, y);
		return patch;
	}

}
