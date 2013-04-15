package iceworld;

import java.awt.image.BufferedImage;

public class TalkObject {
	String username;
	BufferedImage talkImage;
	
	public TalkObject(BufferedImage talkImage, String username){
		this.talkImage = talkImage;
		this.username = username;
	}
}
