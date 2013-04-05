package objects;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import util.ImageLoader;

public class ICEtizen extends Entity{
	public Image avatar;
	
	String B,S,H,W;
	BufferedImage avatarImage;

	public ICEtizen(){
		avatarImage =  new BufferedImage(95,120,BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		avatar = ImageLoader.loadImageFromLocal("images/test-icetizen.png");
	}
	
	public void walk(){
		
	}
	
}
