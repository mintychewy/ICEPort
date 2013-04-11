package util;

import iceworld.given.IcetizenLook;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JLabel;

import objects.ICEtizen;

import org.json.simple.parser.JSONParser;

public class WorldStatesFetcher {
	
	 HashMap<String, ICEtizen> icetizens = new HashMap<String, ICEtizen>();
	 
	 LinkedList<String> username = new LinkedList<String>();
	 LinkedList<Integer> type = new LinkedList<Integer>();
	 LinkedList<String> ip = new LinkedList<String>();
	 LinkedList<String> port = new LinkedList<String>();
	 LinkedList<Integer> pid = new LinkedList<Integer>();
	 LinkedList<Point> position = new LinkedList<Point>();
	 LinkedList<String> timestamp = new LinkedList<String>();
	 LinkedList<Integer> uids = new LinkedList<Integer>();
	 HashMap<Integer,IcetizenLook> looks = new HashMap<Integer, IcetizenLook>();
	 
	 long lastChangeWeather = 0;
     String conditionWeather = "undefined";
	
	public WorldStatesFetcher(){}
	
	/**
	 * This method fetches the raw states from the ICEWorld Server
	 * and process + store them into LinkedLists according to each 
	 * attribute (e.g., list of usernames, ip addresses, ports, etc.)
	 * 
	 * It also checks whether if the server is reachable. If not, 
	 * the method does nothing further (aka. no data is being updated)
	 * 
	 */
	public void updateWorldStates() {
	
		
		// checks whether if the ICEWorld can be reached
		// if not, update nothing and prints the error
		
		if(!ICEWorldPeek.isReachable(ICEWorldPeek.BASE_URL)){
			JDialog dialog = new JDialog();
			dialog.add(new JLabel("ICEWorld Server cannot be reached!"));
			dialog.setPreferredSize(new Dimension(200,100));
			dialog.pack();
			dialog.setModal(true);
			dialog.setVisible(true);
			return ;
		}
			
		
	//////////////////////////////////////////////////////////////
	// STATES													//
	//////////////////////////////////////////////////////////////
		// fetch and print out the raw states (for debugging)
		String out = null;
		try {
			out = ICEWorldPeek.getData("states");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(out);
		
		// create a new parser / keyfinders
		JSONParser parser = new JSONParser();
		KeyFinder finder = new KeyFinder();
		
		// "username" finder
		KeyFinder usernameFinder = new KeyFinder();
		usernameFinder.setMatchKey("username");
		
		// "type" finder
		KeyFinder typeFinder = new KeyFinder();
		typeFinder.setMatchKey("type");
		
		// 
		
		/*
		// XXX I don't think we need this
		int status = 0;
	
		finder.setMatchKey("status");
		try {
			parser.parse(out, finder, true);
			if(finder.isFound()) {
				finder.setFound(false);
				status =  Integer.parseInt(finder.getValue()+"");
				System.out.println(status+"");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		*/
	

		// Find the value of usernames

		finder.setMatchKey("username");
		try {
			while (!finder.isEnd()) { //Go until the end of the string
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					String usernm = "";
					finder.setFound(false); 
					usernm = (String) finder.getValue();
					username.add(usernm); // put all the usernames in the list
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of types

		finder.setMatchKey("type");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					int usernm = 0;
					finder.setFound(false);
					usernm = Integer.parseInt(finder.getValue().toString());
					type.add(usernm);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of ip

		finder.setMatchKey("ip");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					String usernm;
					finder.setFound(false);
					usernm = (String) finder.getValue();
					ip.add(usernm);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of port

		finder.setMatchKey("port");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					Object usernm;
					String usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = String.valueOf(usernm);
					port.add(usernm1);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of pid

		finder.setMatchKey("pid");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					Object usernm;
					Integer usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = Integer.parseInt(usernm.toString());
					pid.add(usernm1);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of the position of the icetizen
		finder.setMatchKey("position");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					String usernm;
					finder.setFound(false);
					usernm = (String) finder.getValue();
					// raw data comes in (85,85) format
					// or null
					Point pt = null;
					if(usernm!=null){
						int x = Integer.parseInt(usernm.substring(1, usernm.indexOf(',')));
						int y = Integer.parseInt(usernm.substring(usernm.indexOf(',')+1,usernm.lastIndexOf(')')));
						pt = new Point(x,y);
					}
					

					position.add(pt);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		 //Find the value of the time of the position
		
		finder.setMatchKey("timestamp");
		try {
			while (!finder.isEnd()) {
				parser.parse(out, finder, true);
				if (finder.isFound()) {
					Object usernm;
					String usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = String.valueOf(usernm);
					timestamp.add(usernm1);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}
			  
		 

		// Find the value of the weather: condition
		finder.setMatchKey("condition");
		try {
			parser.parse(out, finder, true);
			if (finder.isFound()) {
				finder.setFound(false);
				conditionWeather = (String) finder.getValue();
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of the time the weather changed
		finder.setMatchKey("last_change");
		try {
			parser.parse(out, finder, true);
			if (finder.isFound()) {
				finder.setFound(false);
				lastChangeWeather = Integer.parseInt(finder.getValue().toString());
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		

		
		int startIndex = 0;
		int endIndex = 0;
		String tmpString = "";
		int uid = -1;
		while(out.indexOf("user") != -1){
			
			tmpString = out.substring(out.indexOf("user")-10,out.indexOf("user"));
			String intValue = tmpString.replaceAll("[a-zA-Z\"}{,:]", "");
			out = out.substring(out.indexOf("user")+10);
			uids.add(Integer.parseInt(intValue));
		}
		
		//////////////////////////////////////////////////////////////
		// Looks													//
		//////////////////////////////////////////////////////////////
		
		// key is uid
		looks = new HashMap<Integer, IcetizenLook>();
		
		Iterator uidItr = uids.iterator();
		String rawLook = null;
		// for each key in the list, fetch the looks
		
		
		/*
		ExecutorService pool = Executors.newFixedThreadPool(10);
		
		for (int k = 0; k < uids.size(); k++) {
		    pool.submit(new GetLooksTask(uids.get(k)));
		}
		
		pool.shutdown();
		pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		*/
		IcetizenLook look;
		while(uidItr.hasNext()){
			int key = (Integer) uidItr.next();
				try {
					rawLook = ICEWorldPeek.getLooks(key+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// {"status":1,"data":[{"B":null,"H":null,"S":null,"W":null}]}
				// null will become "ul"
				look = new IcetizenLook();
				look.gidB = rawLook.substring(rawLook.indexOf("B")+4,rawLook.indexOf("H")-3);
				look.gidH = rawLook.substring(rawLook.indexOf("H")+4,rawLook.indexOf("S")-3);
				look.gidS = rawLook.substring(rawLook.indexOf("S")+4,rawLook.indexOf("W")-3);
				look.gidW = rawLook.substring(rawLook.indexOf("W")+4,rawLook.lastIndexOf("]")-2);
				looks.put(key, look);
		}
		
		
		appendData();
	
	}
	
	/*
	class GetLooksTask implements Runnable {
		int key;
		public GetLooksTask(int key){
			this.key = key;
		}
		@Override
		public void run() {
			try {
				String rawLook = ICEWorldPeek.getLooks(key+"");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	*/
	
	/**
	 * This method creates a new ICEtizen, assigns values 
	 * to their attributes, and adds the ICEtizen object
	 * to the HashMap containing currently-logged-in ICEtizens 
	 * 
	 * Data is taken from lists obtained earlier in the 
	 * updateWorldStates() method
	 */
	private void appendData(){
		
		while(!username.isEmpty()){
			
			// creating a new ICEtizen
			ICEtizen icetizen = new ICEtizen();
	
			icetizen.setUsername(username.getFirst());
	
			icetizen.setType(type.poll());
			
			icetizen.setIcePortID(pid.poll());
			
			icetizen.setuid(uids.poll());
			
			icetizen.setIPAddress(ip.poll());
			
			icetizen.setListeningPort(Integer.parseInt(port.poll()));
			
			icetizen.setCurrentPosition(position.poll());
			
			
		
			// if icetizen is not an alien 
			// fetch his/her looks 
			if(icetizen.getType() == 1) {
				System.out.println(icetizen.getUsername()+" is not an alien. Giving him clothes..");
				System.out.println("His uid is :"+ icetizen.getuid());
				icetizen.setIcetizenLook(looks.get(icetizen.uid));
			} else {
				icetizen.setIcetizenLook(null);
			}
			

			// add to the currently logged-in user list
			icetizens.put(username.poll(), icetizen);
			
		}
		
		
		// get controller user
		//icetizens.get();
	}
	
	public HashMap<String,ICEtizen> getLoggedinUserMap() {
		return this.icetizens;
	}
	
}