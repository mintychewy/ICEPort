package iceworld;

import gui.LoginPage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class YellingTaskOthers extends TimerTask {

	public static final Color SKY_BLUE = new Color(95,221,250);
	
	public BufferedImage bf;
	
	public YellingTaskOthers(BufferedImage bf) {
			this.bf = bf;
	}
	
	@Override
	public void run() {
		LoginPage.app.view.yellImageList.remove(bf);
		cancel();
	}

}
