package objects;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import util.AvatarLoader;

import iceworld.ICEWorldView;
import iceworld.given.*;

public class ICEtizen implements MyIcetizen {

	// group ICEPort ID
	public int ICEPORT_ID = 245;
	//

	public static final Dimension AVATAR_SIZE = new Dimension(95,120);
	
	// TODO make non-static
	// Zoom in

	public static int AVATAR_OFFSET_X;
	public static int AVATAR_OFFSET_Y;

	public Point lastKnownIntendedDestination;
	public String destinationSpecifyTimestamp;
	public Point currentPosition;
	// AtomicReference<Object> cache = new AtomicReference<Object>();
	public BufferedImage avatar;
	public IcetizenLook look;
	// used for loading avatar
	BufferedImage avatarImage;

	public int uid;

	public String username;
	public String IPAddress;
	public int listeningPort;

	// Actions
	public int actionID;

	// Type
	public Integer type;

	public ICEtizen(){
		
		AVATAR_OFFSET_X = (int)(48*ICEWorldView.zoom_factor);
		AVATAR_OFFSET_Y = (int)(115*ICEWorldView.zoom_factor);
		
		
		// set the default looks
		look = new IcetizenLook();
		
		/*
		look.gidB = "B001";
		look.gidS = "S019";
		look.gidH = "H015";
		look.gidW = "W045";
		
		avatar = AvatarLoader.loadAvatar(look);
		*/

	}
	
	public void updateAvatarImage() {
		avatar = AvatarLoader.loadAvatar(look);
	}

	@Override
	public int getIcePortID() {
		return ICEPORT_ID;
	}

	@Override
	public IcetizenLook getIcetizenLook() {
		return look;
	}

	@Override
	public int getListeningPort() {
		return listeningPort;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setIcePortID(int newID) {
		this.ICEPORT_ID = newID;
	}


	@Override
	public void setIcetizenLook(IcetizenLook newLook) {
		System.out.println("Set New Looks");

		this.look = newLook;

		if(look != null){
			System.out.println("Reloading new looks:" );
			System.out.println(look.gidB+" "+look.gidW);
			avatar = AvatarLoader.loadAvatar(look);

		}

	}


	@Override
	public void setListeningPort(int newListeningPort) {
		this.listeningPort = newListeningPort;
	}

	@Override
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getType(){
		return this.type;
	}
	
	public void setIPAddress(String IPAddress){
		this.IPAddress = IPAddress;
	}
	
	public String getIPAddress() {
		return this.IPAddress;
	}

	public void setCurrentPosition(Point currentPosition){
		this.currentPosition = currentPosition;
	}
	
	public Point getCurrentPosition() {
		return this.currentPosition;
	}
	
	public void setuid(int uid){
		this.uid = uid;
	}
	
	public int getuid() {
		return this.uid;
	}
}
