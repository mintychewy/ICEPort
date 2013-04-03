package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	public static BufferedImage loadImageFromLocal(String path){
		BufferedImage image = null;

		URL url = ClassLoader.getSystemClassLoader().getResource("/images/green-tile.png");
		try {
			image = ImageIO.read(new File(url.toString().substring(5)));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return image;
	}
	

	public static BufferedImage loadImageFromRemote(String uri){
		BufferedImage image = null;

		
		try {
			image = ImageIO.read(new File(uri));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return image;
		
	}
	
	
	
	
	
	
}
