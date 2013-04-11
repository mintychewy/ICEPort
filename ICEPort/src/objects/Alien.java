package objects;

import java.awt.image.BufferedImage;

import iceworld.ICEWorldView;
import util.ImageLoader;
import util.Scaler;

public class Alien extends ICEtizen {

	public Alien() {
		avatarImage =  new BufferedImage((int)(ICEWorldView.zoom_factor*95),(int)(ICEWorldView.zoom_factor*120),BufferedImage.TYPE_INT_ARGB);
		loadResources();
	}

	public void loadResources(){
		avatar = Scaler.scaleBufferedImage(ImageLoader.loadImageFromLocal("images/alien.png"),ICEWorldView.zoom_factor);
	}
}
