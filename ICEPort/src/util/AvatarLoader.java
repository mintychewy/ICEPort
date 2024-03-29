package util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import iceworld.ICEWorldView;
import iceworld.given.*;;

public class AvatarLoader extends JPanel {
	static String list;
	static AvatarLoader loader;
	LinkedList<String> s = new LinkedList<String>();
	LinkedList<String> b = new LinkedList<String>();
	LinkedList<String> h = new LinkedList<String>();
	LinkedList<String> w = new LinkedList<String>();
	public String bodyURL;
	public String shirtURL;
	public String weaponURL;
	public String headURL;
	String bgURL; 
	public int numShirt;
	public int numBody;
	public int numHead;
	public int numWeapon;
	String bodyPath,weaponPath,headPath,shirtPath;
	
	public int shirtCount = 0;
	public int weaponCount = 0;
	public int bodyCount = 0;
	public int headCount = 0;
	static BufferedImage head, body, weapon, shirt, background;
	//constructor
	public AvatarLoader(String x){
		list=x;
	}
	 
	

	public void addToLinkedList(String list) {
		String sList = list;
		String hList = list;
		String bList = list;
		String wList = list;
		// add number of body
		numBody = 0;
		while (bList.indexOf("B") != -1) {
			bodyPath = bList.substring(bList.indexOf("B"),
					bList.indexOf("B") + 4);
			if (bList.length() >= 4) {
				bList = bList.substring(bList.indexOf("B") + 4);
				b.add(bodyPath);
				numBody++;
			} else {
				break;
			}
		}
		// add number of head
		numHead = 0;
		while (hList.indexOf("H") != -1) {
			headPath = hList.substring(hList.indexOf("H"),
					hList.indexOf("H") + 4);
			if (hList.length() >= 4) {
				hList = hList.substring(hList.indexOf("H") + 4);
				h.add(headPath);
				numHead++;
			} else {
				break;
			}
		}

		// add number of shirt
		numShirt = 0;
		while (sList.indexOf("S") != -1) {
			 shirtPath = sList.substring(sList.indexOf("S"),
					sList.indexOf("S") + 4);
			if (sList.length() >= 4) {
				sList = sList.substring(sList.indexOf("S") + 4);
				s.add(shirtPath);
				numShirt++;
			} else {
				break;
			}
		}
		// System.out.print(numShirt+"");

		// add number of weapon
		numWeapon = 0;
		while (wList.indexOf("W") != -1) {
			weaponPath = wList.substring(wList.indexOf("W"),
					wList.indexOf("W") + 4);
			if (wList.length() >= 4) {
				wList = wList.substring(wList.indexOf("W") + 4);
				w.add(weaponPath);
				numWeapon++;
			} else {
				break;
			}
		}

	}
	
	public static BufferedImage loadAvatar(IcetizenLook l){
		BufferedImage full = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
		Graphics g = full.createGraphics();

		BufferedImage bi = loadImageFromLocal("images/body/" +l.gidB+ ".png", l.gidB);
		g.drawImage(bi,0,0,null);
		bi = loadImageFromLocal("images/head/" + l.gidH +".png", l.gidH);
		g.drawImage(bi,0,0,null);
		bi = loadImageFromLocal("images/shirt/"+l.gidS+".png", l.gidS);
		g.drawImage(bi,0,0,null);
		bi = loadImageFromLocal("images/weapon/"+l.gidW+".png", l.gidW);
		g.drawImage(bi,0,0,null);

		full = Scaler.scaleBufferedImage(full, 0.25*ICEWorldView.zoom_factor);
		return full;
		
	}
	
	

	public static void loadBufferedImage(String H, String B, String S,
			String W, String BG) {
		loader = new AvatarLoader(list);
		//load head
		try{
		head = loader.loadImageFromLocal("images/head/" + H+ ".png",H);
		}catch(IllegalArgumentException e){
			String result = null;
			try {
				result = ICEWorldPeek.getData("gurl&gid="+H).substring(27+17);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String link = "http://iceworld.sls-atl.com/"+result.substring(1,result.length()-3);
			link = link.replace("\\", "");
		head=loader.loadImageFromRemote(link);
			
		}
		//load body
		try{
			body = loader.loadImageFromLocal("images/body/" + B+ ".png",B);
			}catch(IllegalArgumentException e){
				String result = null;
				try {
					result = ICEWorldPeek.getData("gurl&gid="+B).substring(27+17);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String link = "http://iceworld.sls-atl.com/"+result.substring(1,result.length()-3);
				link = link.replace("\\", "");
			body=loader.loadImageFromRemote(link);
				
			}
		
	//load weapon
		try{
			weapon = loader.loadImageFromLocal("images/weapon/" + W + ".png",W);
			}catch(IllegalArgumentException e){
				System.out.println("WEAPON NOT FOUND IN LOCAL DATABSE");
				String result = null;
				try {
					result = ICEWorldPeek.getData("gurl&gid="+W).substring(27+17);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String link = "http://iceworld.sls-atl.com/"+result.substring(1,result.length()-3);
				link = link.replace("\\", "");
			weapon=loader.loadImageFromRemote(link);
				
			}
		//load shirt

		try{
			shirt = loader.loadImageFromLocal("images/shirt/" +S + ".png",S);
			}catch(IllegalArgumentException e){
				String result = null;
				try {
					result = ICEWorldPeek.getData("gurl&gid="+S).substring(27+17);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String link = "http://iceworld.sls-atl.com/"+result.substring(1,result.length()-3);
				link = link.replace("\\", "");
			shirt=loader.loadImageFromRemote(link);
				
			}	
		background = loader.loadImageFromLocal(BG,BG);

	}
	
	public static BufferedImage loadImageFromLocal(String path, String originalFilename){
		BufferedImage img=null;
		try {
			img=ImageIO.read(ClassLoader.getSystemClassLoader().getResource(path));
		} catch (Exception e) {
			System.out.println("Error loading image from LOCAL DATABASE");

			
			System.out.println("Try loading from the directory outside jar");
			
			try {
				String filePath = ClassLoader.getSystemClassLoader().getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + originalFilename +".png";
				
				img = ImageIO.read(new File(filePath));
				
			} catch (Exception e3) {
				System.out.println("Try loading from server");

				try {
					String result = ICEWorldPeek.getData("gurl&gid="+originalFilename).substring(27+17);
					result = result.replaceAll("\\\\","");
					//http://iceworld.sls-atl.com/graphics\/weapon\/waterpistol.png
					String link = "http://iceworld.sls-atl.com/"+result.substring(1,result.length()-3);
					img = loadImageFromRemote(link);

					
				
					    File outputfile = new File(originalFilename+".png");
					    ImageIO.write(img, "png", outputfile);
	
					
				} catch (Exception e1) {
					// OUT OF LUCK
					e1.printStackTrace();
				}
			}
			
		
						
		}
		return img;
	}
	
	public static BufferedImage loadImageFromRemote(String uri){
		BufferedImage image = null;

		try {
			@SuppressWarnings("unused")
			URL url = new URL(uri);
		} catch (MalformedURLException e1) {
			System.out.println(e1.getMessage());
		}
		
		try {
			//System.out.println(""+uri);
			image = ImageIO.read(new URL(uri));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return image;
		
	}
	


	
	

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		
		addToLinkedList(list);
		shirtURL = s.get(shirtCount);
		//System.out.print(s.get(shirtCount));
		bodyURL =  b.get(bodyCount);
		headURL =  h.get(headCount) ;
		weaponURL = w.get(weaponCount) ;
		bgURL = "";
		loadBufferedImage(headURL, bodyURL, shirtURL, weaponURL, bgURL);
		g.drawImage(background, 80, 50, this);
		g.drawImage(body, 80, 50, this);
		g.drawImage(head, 80, 40, this);
		g.drawImage(shirt, 80, 50, this);
		g.drawImage(weapon, 80, 50, this);
	}

}