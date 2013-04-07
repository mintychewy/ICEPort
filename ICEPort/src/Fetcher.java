import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class Fetcher {

	String weather;

	HashMap<String, ICEtizen> loggedinUsers = new HashMap<String, ICEtizen>();
	static long status = 0;
	// All the datas we need are in an ArrayList!
	static ArrayList<String> icetizen = new ArrayList<String>();
	static ArrayList<String> username = new ArrayList<String>();
	static ArrayList<Long> type = new ArrayList<Long>();
	static ArrayList<String> ip = new ArrayList<String>();
	static ArrayList<String> port = new ArrayList<String>();
	static ArrayList<String> pid = new ArrayList<String>();
	static ArrayList<String> position = new ArrayList<String>();
	static ArrayList<String> timestamp = new ArrayList<String>();
	// Here is the weather
	static String conditionWeather = "cool";
	static long lastChangeWeather = 0;

	static KeyFinder finder = new KeyFinder();

	public static void main(String[] args) throws Exception {

		doSomething();

	}

	public static void doSomething() throws Exception {

		// get the output
		String output = ICEWorldPeek.getData("states");
		System.out.println(output);

		// String output2 = "[" + output + "]";
		// System.out.println(output2);
		JSONParser parser = new JSONParser();
		// Object obj = (parser.parse(output2));

		// JSONArray array = (JSONArray) obj;

		// for (int i = 0; i < array.size(); i++)
		// System.out.println(array.get(i));

		// Find the value of status

		finder.setMatchKey("status");
		try {
			parser.parse(output, finder, true);
			if (finder.isFound()) {
				finder.setFound(false);
				status = (long) finder.getValue(); //get the value
				System.out.println("status is " + status);
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of icetizen
		/*
		 * finder = new KeyFinder(); finder.setMatchKey("icetizen"); try{
		 * while(!finder.isEnd()){ parser.parse(output, finder, true);
		 * if(finder.isFound()){ String usernm= ""; finder.setFound(false);
		 * //System.out.println("found icetizen:");
		 * //System.out.println(finder.getValue()); usernm = (String)
		 * finder.getValue(); icetizen.add(usernm);
		 * //System.out.println("username is " + usernm); } }
		 * parser.parse(output, finder, false); finder = new KeyFinder();
		 * System.out.println("icetizen is " + icetizen); } catch(Exception pe){
		 * pe.printStackTrace(); }
		 */

		// Find the value of usernames

		finder.setMatchKey("username");
		try {
			while (!finder.isEnd()) { //Go until the end of the string
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					String usernm = "";
					finder.setFound(false);
					usernm = (String) finder.getValue();
					username.add(usernm); // put all the usernames in the list
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("username is " + username);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of types

		finder.setMatchKey("type");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					long usernm = 0;
					finder.setFound(false);
					usernm = (long) finder.getValue();
					type.add(usernm);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("type is " + type);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of ip

		finder.setMatchKey("ip");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					String usernm;
					finder.setFound(false);
					usernm = (String) finder.getValue();
					ip.add(usernm);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("ip is " + ip);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of port

		finder.setMatchKey("port");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					Object usernm;
					String usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = String.valueOf(usernm);
					port.add(usernm1);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("port is " + port);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of pid

		finder.setMatchKey("pid");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					Object usernm;
					String usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = String.valueOf(usernm);
					pid.add(usernm1);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("pid is " + pid);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of the position of the icetizen
		finder.setMatchKey("position");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					String usernm;
					finder.setFound(false);
					usernm = (String) finder.getValue();
					position.add(usernm);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("position is " + position);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		 //Find the value of the time of the position
		
		finder.setMatchKey("timestamp");
		try {
			while (!finder.isEnd()) {
				parser.parse(output, finder, true);
				if (finder.isFound()) {
					Object usernm;
					String usernm1;
					finder.setFound(false);
					usernm = finder.getValue();
					usernm1 = String.valueOf(usernm);
					timestamp.add(usernm1);
				}
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("timestamp is " + timestamp);
		} catch (Exception pe) {
			pe.printStackTrace();
		}
			  
		 

		// Find the value of the weather: condition
		finder.setMatchKey("condition");
		try {
			parser.parse(output, finder, true);
			if (finder.isFound()) {
				System.out.println("vvvvv");
				finder.setFound(false);
				conditionWeather = (String) finder.getValue();
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("the weather is " + conditionWeather);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		// Find the value of the time the weather changed
		finder.setMatchKey("last_change");
		try {
			parser.parse(output, finder, true);
			if (finder.isFound()) {
				finder.setFound(false);
				lastChangeWeather = (long) finder.getValue();
			}
			parser.parse(output, finder, false);
			finder = new KeyFinder();
			System.out.println("the time the weather is " + lastChangeWeather);
		} catch (Exception pe) {
			pe.printStackTrace();
		}

	}

	public long getStatus() {
		return status;
	}

	public ArrayList<String> getUsername() {
		return username;
	}

	public ArrayList<Long> getType() {
		return type;
	}

	public ArrayList<String> getIp() {
		return ip;
	}

	public ArrayList<String> getPid() {
		return pid;
	}

	public ArrayList<String> getPort() {
		return port;
	}

	public ArrayList<String> getPosition() {
		return position;
	}

	public ArrayList<String> getTimestamp() {
		return timestamp;
	}

	public String getConditionWeather() {
		return conditionWeather;
	}

	public long getTimeWeather() {
		return lastChangeWeather;
	}

}
