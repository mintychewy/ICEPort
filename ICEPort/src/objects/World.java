package objects;

import iceworld.ICEWorldView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import util.ImageLoader;
import util.Scaler;

public class World {
	Image tile;
	private BufferedImage mapImage;

	public static int TILE_HEIGHT;
	public static int TILE_WIDTH;
	private static int h ;
	private static int w ;
	
	public static Dimension WORLD_SIZE;
	public static final int NUM_TILES = 100;

	// NUM_TILES *+ (Arbitrary offset multiplier/adders)
	public static int OFFSET_FROM_ORIGIN_X;
	public static int OFFSET_FROM_ORIGIN_Y;

	private int x, y;

	public BufferedImage getImage() {
		return mapImage;
	}

	public World() {
		 TILE_HEIGHT = (int)(32*ICEWorldView.zoom_factor);
		 TILE_WIDTH = (int)(64*ICEWorldView.zoom_factor);
		 h = (int)(6480*ICEWorldView.zoom_factor);
		 w = (int)(4320*ICEWorldView.zoom_factor);
		 
		 OFFSET_FROM_ORIGIN_X = (int)((World.NUM_TILES * 32 + 25)*ICEWorldView.zoom_factor);
		 OFFSET_FROM_ORIGIN_Y = (int)((World.NUM_TILES + 350    )*ICEWorldView.zoom_factor);
		 
		WORLD_SIZE = new Dimension(h,w);
		System.out.println("Zoom factor in World: " +ICEWorldView.zoom_factor);
		System.out.println("Create new World image with size: "+h+","+w);
		mapImage = new BufferedImage(WORLD_SIZE.width, WORLD_SIZE.height,
				BufferedImage.TYPE_INT_ARGB);
		loadResources();
		drawMap();
	}

	public void loadResources() {
		
	tile = ImageLoader.loadImageFromLocal("images/l-tile.png");
			/*
		URL url = this.getClass().getResource("/images/l-tile.png");
		try {
			tile = ImageIO.read(new File(url.toString().substring(5)));
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
	}

	/*
	 * Draw a diamond-shaped map using tiles onto the BufferedImage
	 */
	public void drawMap() {
		Graphics2D g = mapImage.createGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, WORLD_SIZE.width, WORLD_SIZE.height);
		g.setColor(Color.RED);

		/*
		 * for (int i = 0; i < tile_map.length; i++){ for(int j =
		 * tile_map[i].length; j >= 0; j--){ g.drawImage(tile,
		 * x=(j*TILE_WIDTH/2)+(i*TILE_WIDTH/2)+offsetX, y =
		 * (i*TILE_HEIGHT/2)-(j*TILE_HEIGHT/2)+offsetY, null); // Show tile
		 * indices g.drawString(i+","+j, x+ TILE_WIDTH/2-10, y+TILE_HEIGHT/2+5);
		 * }
		 * 
		 * }
		 */

		Image newTile = tile;
		// alternate rendering method
		if(ICEWorldView.zoom_factor != 1){
			 newTile = Scaler.scaleBufferedImage((BufferedImage)tile, ICEWorldView.zoom_factor);
		}
		int offsetX = OFFSET_FROM_ORIGIN_X - TILE_HEIGHT;
		int offsetY = OFFSET_FROM_ORIGIN_Y;
		for (int a = 0; a < NUM_TILES; a++)
			for (int b = 0; b < NUM_TILES; b++) {
				x = a * TILE_WIDTH / 2 - b * TILE_WIDTH / 2 + offsetX;
				y = a * TILE_HEIGHT / 2 + b * TILE_HEIGHT / 2 + offsetY;

				g.drawImage(newTile, x, y, null);
				// Tile coord label
				/*
				g.drawString(a + "," + b, x + TILE_WIDTH / 2, y
					+ TILE_HEIGHT / 2);
				*/
			}

	}

	public boolean isFallingIntoTartarus(Point p) {
		if (p.x < 0 || p.x > NUM_TILES - 1 || p.y < 0 || p.y > NUM_TILES - 1)
			return true;
		return false;
	}

}
