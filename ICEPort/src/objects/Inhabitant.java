package objects;

import iceworld.ICEWorldView;

import java.awt.Image;
import java.awt.image.BufferedImage;

import util.ImageLoader;
import util.Scaler;

public class Inhabitant extends ICEtizen {
	
	public Inhabitant(){
		// No need?
		avatarImage =  new BufferedImage((int)(ICEWorldView.zoom_factor*95),(int)(ICEWorldView.zoom_factor*120),BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		avatar = Scaler.scaleBufferedImage(ImageLoader.loadImageFromLocal("images/test-icetizen.png"),ICEWorldView.zoom_factor);
	}
	
	public void walk(){
		
	}
	
}
