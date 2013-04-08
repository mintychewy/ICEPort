package objects;

import java.awt.Image;
import java.awt.image.BufferedImage;

import util.ImageLoader;

public class Inhabitant extends ICEtizen {
	
	public Inhabitant(){
		avatarImage =  new BufferedImage(95,120,BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		avatar = ImageLoader.loadImageFromLocal("images/test-icetizen.png");
	}
	
	public void walk(){
		
	}
	
}
