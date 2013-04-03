package objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ICEtizen {
	public Image avatar;
	BufferedImage avatarImage;
	
	String H,B,W,S;

	public ICEtizen(){
		avatarImage =  new BufferedImage(95,120,BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		URL url = this.getClass().getResource("/images/test-icetizen.png");
			try {
				avatar = ImageIO.read(new File(url.toString().substring(5)));
			} catch (IOException e) {
				e.printStackTrace();
			}	
	
	}
	
	
}
