package util;

import iceworld.ICEWorldView;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import objects.Map;
import objects.Minimap;

public class Scaler {
	
	/**
	 * Scales a BufferedImage
	 * @param original original BufferedImage
	 * @param scaleFactor scaling factor
	 * @return new resized BufferedImage
	 */
	public static BufferedImage scaleBufferedImage(BufferedImage original, double scaleFactor){
		int originalWidth = original.getWidth();
		int originalHeight = original.getHeight();
		int newWidth = (int) (scaleFactor*originalWidth);
		int newHeight = (int) (scaleFactor*originalHeight);
		
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g2 = resized.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(original,0, 0, newWidth, newHeight, 0, 0, originalWidth, originalHeight, null);
		g2.dispose();
		return resized;
	}
	
	/**
	 * Scales a Point 
	 * @param original original Point
	 * @param scaleFactor scaling factor
	 * @return new scaled Point
	 */
	public static Point scalePoint(Point original, double scaleFactor){
		return new Point((int)(original.x*scaleFactor), (int)(original.y*scaleFactor));
	}
	
	/**
	 * Converts a viewport point into a screen-space point
	 * @param screenspacePoint viewport point
	 * @param map destination map
	 * @return map Point
	 */
	public static Point toMapPoint(Point screenspacePoint, int deltaX, int deltaY){
		return new Point(deltaX+screenspacePoint.x,deltaY+screenspacePoint.y);
	}
   
	
	/**
	 * Converts tile indices Point into Screen-space coordinates
	 * (aka.pixel coordinates)
	 * @param tilePoint tile indices
	 * @return pixel coordinate Point
	 */
	public static Point toScreenSpace(Point tilePoint){
		// see logic below
		
		int w = Map.TILE_WIDTH/2;
		int h = Map.TILE_HEIGHT/2;
		int fx = Map.OFFSET_FROM_ORIGIN_X;
		int fy = Map.OFFSET_FROM_ORIGIN_Y;
		int x = (tilePoint.x * w + tilePoint.y * -w + fx); 
		int y = (tilePoint.x * h + tilePoint.y * h + fy);
		
		// centre of the tile
		y += Map.TILE_HEIGHT/2;

		return new Point(x,y);
	}
	
	/**
	 * A conveniece method to converts a viewport (mouseclick) Point into
	 * tile-space coordinates  
	 * @param viewportPoint viewport mouseclick Point
	 * @return tile indices Point
	 */
	public static Point toTileSpaceFromViewport(Point viewportPoint){
		return toTileSpace(toMapPoint(viewportPoint, ICEWorldView.deltaX, ICEWorldView.deltaY));
	}
	
	/**
	 * Converts a screenSpace (aka.map point) Point into 
	 * tile-space coordinates 
	 * @param screenspacePoint screen-space mouseclick Point
	 * @return tile indices Point
	 */
	public static Point toTileSpace(Point screenspacePoint){
		
		/* General dimetric projection matrices
		 * 
		 * If you have an isometric perspective with each tile
		 * being 2w in width (64 in this case, so w = 32) and 2h
		 * in height (32 in this case, so h = 16), and the offset
		 * of the origin point in screen space being fx and fy
		 * for the horizontal and vertical axis respectively,
		 * the matrices look like follows.
		 * 
		 * TILE SPACE ==> SCREEN SPACE
		 * | w -w fx |
		 * | h  h fy |
		 * | 0  0 1  |
		 * 
		 * SCREEN SPACE ==> TILE SPACE (inverse matrix)
		 * 
		 *  		 |  h w (-fxh - fyw) |
		 * (1/2wh) * | -h w (fxh - fyw ) |
		 *           |  0 0 (    2wh   ) |
		 *           
		 *           
		 * (1/2024) * | 16 32 -(offset+32)*16 |          
		 *            |-16 32  (offset+32)*16 |
		 *            |  0  0       1024      |
		 *           
		 * Example calculation:
		 * 
		 * (1/1024) * | 16 32 -3072 |
		 * 		      |-16 32  3072 |
		 *            | 0  0   1024 | 
		 * 
		 * Screen-coordinate = (105,100,1) 
		 * third coordinate extended (1) in order to be able to 
		 * incorporate the translation into the matrix
		 * 
		 * a = (1.0/1024) * (105*16 + 100*32 - 3072) = 1.76
		 * b = (1.0/1024) * (105*-16 + 100*32 + 3072) = 4.48
		 * 
		 * floor(a) = 1, floor(b) = 4, 
		 * 
		 * tile indices = (1,4)
		 */
		
		// 1/det|A|
		double iDet = (1.0/(2*(Map.TILE_HEIGHT/2)*(Map.TILE_WIDTH/2)));
		
		int a = (int) ((iDet)*(screenspacePoint.x*Map.TILE_HEIGHT/2 + screenspacePoint.y*Map.TILE_WIDTH/2 - (Map.OFFSET_FROM_ORIGIN_X+Map.OFFSET_FROM_ORIGIN_Y*2)*(Map.TILE_HEIGHT/2)));
		int b = (int) ((iDet)*(screenspacePoint.x*-Map.TILE_HEIGHT/2 + screenspacePoint.y*Map.TILE_WIDTH/2 + (Map.OFFSET_FROM_ORIGIN_X-Map.OFFSET_FROM_ORIGIN_Y*2)*(Map.TILE_HEIGHT/2)));
		
		System.out.println("Tile Indices: ["+a+","+b+"]");
		return new Point((int)a,b);
	}
		
	
	/**
	 * Returns the determinant of a 3x3 integer matrix
	 * @param a 3x3 integer matrix
	 * @return determinant of the 3x3 integer matrix
	 */
	public static int det(int[][] a){
		    return (a[0][0] * (a[1][1] * a[2][2] - a[2][1] * a[1][2])
		           -a[1][0] * (a[0][1] * a[2][2] - a[2][1] * a[0][2])
		           +a[2][0] * (a[0][1] * a[1][2] - a[1][1] * a[0][2]));
	}
	
	/**
	 * Converts a map-coordinate Point to a minimap Point (in pixels)
	 * @param mapPoint map-coordinate Point
	 * @return MiniMap (pixel) coordinate Point
	 */
	public static Point toMiniMapPoint(Point mapPoint){
		return scalePoint(mapPoint,Minimap.MINIMAP_SCALE_FACTOR);
	}
	
}
