package util;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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
   
}
