package iceworld;

import gui.LoginPage;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class TalkingTaskOthers extends TimerTask {

	public static final Font TALKING_FONT = new Font("monospaced", Font.PLAIN, 20);
	public TalkObject to;

	public TalkingTaskOthers(TalkObject to) {
		this.to = to;
	}

	@Override
	public void run() {
		LoginPage.app.view.talkImageList.remove(to);
		cancel();
		LoginPage.app.view.updateWorld();
	}

}
