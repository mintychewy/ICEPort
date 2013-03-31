package iceworld;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Map {
	Image tile;
	BufferedImage mapImage;

	/* in case we have more than one type of tile
	 * we can map which type of tile that position possesses
	int[][] tile_map = new int[][] {
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
	};
	*/
	
	int[][] tile_map = new int [10][10];
	int offset_x;
	int offsetY = 300;
	final int TILE_WIDTH = 64;
	final int TILE_HEIGHT = 32;
	int x;
	int y;
	
	public Map(){
		mapImage =  new BufferedImage(900,600,BufferedImage.TYPE_INT_ARGB);
		loadResources();
		drawMap();
	}

	public void loadResources(){
		URL url = this.getClass().getResource("/images/empty-tile.png");
			try {
				tile = ImageIO.read(new File(url.toString().substring(5)));
			} catch (IOException e) {
				e.printStackTrace();
			}	
	
	}
	
	/*
	 * Draw a diamond-shaped map using tiles onto the BufferedImage
	 */
	public void drawMap(){
		Graphics2D g = mapImage.createGraphics();
		for (int i = 0; i < tile_map.length; i++){
			for(int j = tile_map[i].length; j >= 0; j--){
				g.drawImage(tile, x=(j*TILE_WIDTH/2)+(i*TILE_WIDTH/2), y = (i*TILE_HEIGHT/2)-(j*TILE_HEIGHT/2)+offsetY, null);
			}
						
		}
	}
}
