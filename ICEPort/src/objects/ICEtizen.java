package objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import util.ImageLoader;

public class ICEtizen {
	public Image avatar;
	BufferedImage avatarImage;
	
	String H,B,W,S;

	public ICEtizen(){
		avatarImage =  new BufferedImage(95,120,BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		avatar = ImageLoader.loadImageFromLocal("images/test-icetizen.png");
	}
	
	
}
