package objects;

import iceworld.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Map {
	Image tile;
	public BufferedImage mapImage;

	/* in case we have more than one type of tile
	 * we can map which type of tile that position possesses
	int[][] tile_map = new int[][] {
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
	};
	*/
	
	int x;
	int y;
	
	public Map(){
		mapImage =  new BufferedImage(Constants.WORLD_SIZE.width,Constants.WORLD_SIZE.height,BufferedImage.TYPE_INT_ARGB);
		loadResources();
		drawMap();
	}

	public void loadResources(){
		URL url = this.getClass().getResource("/images/green-tile.png");
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
		g.setColor(Color.PINK);
		g.fillRect(0, 0, Constants.WORLD_SIZE.width, Constants.WORLD_SIZE.height);
		g.setColor(Color.RED);
		
		/*
		for (int i = 0; i < tile_map.length; i++){
			for(int j = tile_map[i].length; j >= 0; j--){
				g.drawImage(tile, x=(j*Constants.TILE_WIDTH/2)+(i*Constants.TILE_WIDTH/2)+offsetX, 
						y = (i*Constants.TILE_HEIGHT/2)-(j*Constants.TILE_HEIGHT/2)+offsetY, null);
				// Show tile indices
				g.drawString(i+","+j, x+ Constants.TILE_WIDTH/2-10, y+Constants.TILE_HEIGHT/2+5);
			}
						
		}
		*/
		
		//alternate rendering method
		int offsetX = Constants.OFFSET_FROM_ORIGIN_X - Constants.TILE_HEIGHT;
		int offsetY = Constants.OFFSET_FROM_ORIGIN_Y;
		for (int a=0; a < Constants.NUM_TILES; a++)
			  for (int b=0; b < Constants.NUM_TILES; b++) {
			    x = a * Constants.TILE_WIDTH/2 - b * Constants.TILE_WIDTH/2+ offsetX;
			    y = a * Constants.TILE_HEIGHT/2 + b * Constants.TILE_HEIGHT/2 + offsetY;
			
			   	g.drawImage(tile,x,y,null);
			   	g.drawString(a+","+b, x+Constants.TILE_WIDTH/2-10,y+Constants.TILE_HEIGHT/2+5);
			  }


	}
}
