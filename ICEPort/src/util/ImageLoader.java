package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	public static BufferedImage loadImageFromLocal(String path){
		BufferedImage image = null;

		URL url = ClassLoader.getSystemClassLoader().getResource(path);
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
			URL url = new URL(uri);
		} catch (MalformedURLException e1) {
			System.out.println(e1.getMessage());
		}
		
		try {
			System.out.println(""+uri);
			image = ImageIO.read(new URL(uri));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return image;
		
	}
	
	
	
	
	
	
}
