package iceworld;

import java.awt.image.BufferedImage;

public class Camera {
	public static BufferedImage getSubImage(BufferedImage big, int x, int y){
		BufferedImage subImage;
		subImage = big.getSubimage(x, y, Constants.ICEWORLD_VIEWPORT_SIZE.width, Constants.ICEWORLD_VIEWPORT_SIZE.height);
		return subImage;
	}
	
	public static BufferedImage getAvatarPatchImage(BufferedImage big, int x, int y){
		BufferedImage subImage;
		subImage = big.getSubimage(x, y, Constants.AVATAR_SIZE.width, Constants.AVATAR_SIZE.height);
		return subImage;
	}
}
