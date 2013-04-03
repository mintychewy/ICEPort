package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AvatarLoader extends ImageLoader{
	String uri; 
	
	static BufferedImage head,body,weapon,headgear,overall;
	
	public static void avatarPart(String H, String B, String S, String W){
		head= loadImageFromLocal(H);
		body= loadImageFromRemote(B);
		weapon= loadImageFromRemote(W);
		headgear=loadImageFromRemote(H);	
		
	}
	
	public static JFrame makeAvatar(){
		avatarPart( null,null,null,null);
		JFrame frame=new JFrame();
		frame.setSize(400,400);
		frame.getContentPane().add(new JLabel(new ImageIcon(head)));
		frame.getContentPane().add(new JLabel(new ImageIcon(body)));
		frame.getContentPane().add(new JLabel(new ImageIcon(weapon)));
		frame.getContentPane().add(new JLabel(new ImageIcon(headgear)));
		frame.pack();
		frame.setVisible(true);
		return frame;

	}
	
	public static void main(String[] args){
		makeAvatar();
		/*
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL(" http://iceworld.sls-atl.com/api/&cmd=gurl&gid=H008"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame x=new JFrame();
		x.setSize(400,400);
		x.setVisible(true);
		*/
	
	}
	
	
	
}
