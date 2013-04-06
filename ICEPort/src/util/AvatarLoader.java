package util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.JPanel;

public class AvatarLoader extends JPanel{
	
	String[] shirtList={"t_ice","t_appro","t_ice2","armor2"};
	String[] bodyList={"orangy","blue","orangy","pale"};
	String[] headList={"crown1","crown2","hat1","bighair1"};
	String[] weaponList={"hammer4","sword2","stick3","sabre2"};
	int shirtCount=0,weaponCount=0,bodyCount=0,headCount=0;
	static BufferedImage head,body,weapon,shirt,background;	
	public static void loadBufferedImage(String H, String B, String S, String W, String BG){
		head= ImageLoader.loadImageFromRemote(H);
		body= ImageLoader.loadImageFromRemote(B);
		weapon= ImageLoader.loadImageFromRemote(W);
		shirt=ImageLoader.loadImageFromRemote(S);	
		background=ImageLoader.loadImageFromRemote(BG);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		String shirtURL="http://iceworld.sls-atl.com/graphics/shirt/"+shirtList[shirtCount]+".png";
		String bodyURL="http://iceworld.sls-atl.com/graphics/body/"+bodyList[bodyCount]+".png";
		String headURL="http://iceworld.sls-atl.com/graphics/head/"+headList[headCount]+".png";
		String weaponURL="http://iceworld.sls-atl.com/graphics/weapon/"+weaponList[weaponCount]+".png";
		String bgURL="";
		loadBufferedImage(headURL,bodyURL,shirtURL,weaponURL,bgURL);
		g.drawImage(background, 80, 50, this);
		g.drawImage(body, 80, 50, this);
        g.drawImage(head, 80, 40, this);
        g.drawImage(shirt, 80, 50, this);
        g.drawImage(weapon, 70, 50, this);
    }
	
	
	
	
}
