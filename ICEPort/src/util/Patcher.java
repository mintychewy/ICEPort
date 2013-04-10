package util;

import java.awt.image.BufferedImage;

import objects.World;

public class Patcher {
	final int width;
	final int height;
	BufferedImage sourceImage; 
	
	public Patcher(){
	 this.width = World.WORLD_SIZE.width;
	 this.height = World.WORLD_SIZE.height;
	}
	
	public Patcher(BufferedImage sourceImage, int width, int height){
		this.sourceImage = sourceImage;
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage getPatchImage(int originX, int originY){
		BufferedImage patch = sourceImage.getSubimage(originX, originY, width, height);
		return patch;
	}

}
