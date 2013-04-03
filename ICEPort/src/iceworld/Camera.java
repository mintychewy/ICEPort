package iceworld;

import java.awt.image.BufferedImage;

public class Camera {
	public static BufferedImage getSubImage(BufferedImage big, int x, int y){
		BufferedImage subImage;
		subImage = big.getSubimage(x, y, 900, 600);

		return subImage;
	}
}
