package iceworld;

import gui.LoginPage;

import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class TalkingTaskOthers extends TimerTask {

	public BufferedImage bf;

	public TalkingTaskOthers(BufferedImage bf) {
		this.bf = bf;
	}

	@Override
	public void run() {
		LoginPage.app.view.talkImageList.remove(bf);
		cancel();
		LoginPage.app.view.updateWorld();
	}

}
