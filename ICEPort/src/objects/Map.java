package objects;

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

public class Map {
	Image tile;
	private BufferedImage mapImage;

	public static int TILE_HEIGHT = 32;
	public static int TILE_WIDTH = 64;
	public final static Dimension WORLD_SIZE = new Dimension(6480, 4320);
	public static final int NUM_TILES = 100;

	// NUM_TILES *+ (Arbitrary offset multiplier/adders)
	public static final int OFFSET_FROM_ORIGIN_X = Map.NUM_TILES * 32 + 25;
	public static final int OFFSET_FROM_ORIGIN_Y = Map.NUM_TILES + 350;

	private int x, y;

	public BufferedImage getImage() {
		return mapImage;
	}

	public Map() {
		mapImage = new BufferedImage(WORLD_SIZE.width, WORLD_SIZE.height,
				BufferedImage.TYPE_INT_ARGB);
		loadResources();
		drawMap();
	}

	public void loadResources() {
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

		// alternate rendering method
		int offsetX = OFFSET_FROM_ORIGIN_X - TILE_HEIGHT;
		int offsetY = OFFSET_FROM_ORIGIN_Y;
		for (int a = 0; a < NUM_TILES; a++)
			for (int b = 0; b < NUM_TILES; b++) {
				x = a * TILE_WIDTH / 2 - b * TILE_WIDTH / 2 + offsetX;
				y = a * TILE_HEIGHT / 2 + b * TILE_HEIGHT / 2 + offsetY;

				g.drawImage(tile, x, y, null);
				g.drawString(a + "," + b, x + TILE_WIDTH / 2 - 10, y
						+ TILE_HEIGHT / 2 + 5);
			}

	}

	public boolean isFallingIntoTartarus(Point p) {
		if (p.x < 0 || p.x > NUM_TILES - 1 || p.y < 0 || p.y > NUM_TILES - 1)
			return true;
		return false;
	}

}
