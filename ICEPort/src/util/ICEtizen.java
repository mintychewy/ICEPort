package util;

import iceworld.given.IcetizenLook;
import iceworld.given.MyIcetizen;
public class ICEtizen implements MyIcetizen {

	private String username;
	private IcetizenLook fashion;
	private int icePortId;
	private int listeningPort;
	private int type;
	private String IPadress;
	private int userID;
	
	@Override
	public int getIcePortID() {
		return icePortId;
	}

	@Override
	public IcetizenLook getIcetizenLook() {
		return fashion;
	}

	@Override
	public int getListeningPort() {
		return icePortId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setIcePortID(int id) {
		icePortId = id;
	}

	@Override
	public void setIcetizenLook(IcetizenLook newLook) {
		fashion = newLook;;
	}

	@Override
	public void setListeningPort(int newPort) {
		listeningPort = newPort;
	}

	@Override
	public void setUsername(String uname) {
		username = uname;
	}

}