package iceworld;

import gui.LoginPage;

import java.util.TimerTask;

public class SelfChat extends TimerTask {

	@Override
	public void run() {
		LoginPage.app.view.instantTalkMessage = null;
		cancel();
	}
	
}
