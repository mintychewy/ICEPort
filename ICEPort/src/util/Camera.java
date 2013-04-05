package util;

import iceworld.ICEWorldView;
import java.awt.image.BufferedImage;

public class Camera {

	public BufferedImage getSubImage(BufferedImage big, int x, int y){
		BufferedImage subImage;
		subImage = big.getSubimage(x, y, ICEWorldView.ICEWORLD_VIEWPORT_SIZE.width, ICEWorldView.ICEWORLD_VIEWPORT_SIZE.height);
		return subImage;
	}
	
	
}
