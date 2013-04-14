package util;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JLabel;

import objects.ICEtizen;

import org.json.simple.parser.JSONParser;

import core.Application;

public class ActionFetcher {

	public static long from = 1365931994;

	public LinkedList<Actions> talkList;
	public LinkedList<Actions> yellList;

	LinkedList<Integer> uidList = new LinkedList<Integer>();
	LinkedList<Integer> actionTypeList = new LinkedList<Integer>();
	LinkedList<String> detailList = new LinkedList<String>();

	public ActionFetcher() {
		talkList = new LinkedList<Actions>();
		yellList = new LinkedList<Actions>();
	}

	public void fetchActions() {

		yellList.clear();
		talkList.clear();
		//////////////////////////////////////////////////////////////
		// Actions													//
		//////////////////////////////////////////////////////////////

		System.out.println("Now fetching actions.. from "+from);

		// fetch raw actions
		String out = null;
		try {
			out = ICEWorldPeek.getData("actions&from="+from);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// create a new parser / keyfinders
		JSONParser parser = new JSONParser();
		KeyFinder finder = new KeyFinder();

		// "action_type" finder
		finder.setMatchKey("action_type");

		try {
			while(!finder.isEnd()) {
				parser.parse(out, finder, true);
				if(finder.isFound()) {
					String action_type = "";
					finder.setFound(false);
					action_type = (String) finder.getValue();
					actionTypeList.add(Integer.parseInt(action_type));
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// "uid" finder
		finder.setMatchKey("uid");

		try {

			while(!finder.isEnd()) {
				parser.parse(out, finder, true);
				if(finder.isFound()) {
					String uid = "";
					finder.setFound(false);
					uid = (String) finder.getValue();
					uidList.add(Integer.parseInt(uid));
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();

		} catch (Exception e) {
			e.printStackTrace();
		}

	

		// "detail" finder
		finder.setMatchKey("detail");

		try {
			while(!finder.isEnd()) {
				parser.parse(out, finder, true);
				if(finder.isFound()) {

					String detail = "";
					finder.setFound(false);
					detail = (String) finder.getValue();
					// adds even if if details is null
					detailList.add(detail);
				}
			}
			parser.parse(out, finder, false);
			finder = new KeyFinder();
		} catch (Exception e) {
			e.printStackTrace();
		}

		appendData();
	}

	private void appendData() {

		HashMap<Integer,String> uidAndUsernameList = new HashMap<Integer, String>();
		
		HashMap<String,ICEtizen> tempList = new HashMap<String,ICEtizen>();
		
		ICEtizen test = new ICEtizen();
		test.setuid(28);
		test.setUsername("Putti.O");
		tempList.put("Putti.O",test);
		
		for(ICEtizen value : tempList.values()){
			uidAndUsernameList.put(value.getuid(),value.getUsername());
		}
		/*
		for(ICEtizen value : Application.app.view.loggedinUsers.values()){
			uidAndUsernameList.put(value.getuid(),value.getUsername());
		}
		*/
		
		LinkedList<Actions> actionList = new LinkedList<Actions>();

		Actions action;
		for(int i = 0; i < uidList.size(); i++) {
			action = new Actions();
			action.uid = uidList.poll();
			action.action_type = actionTypeList.poll();
			action.details = detailList.poll();
			actionList.add(action);
		}

		for(Actions a : actionList) {
			if(a.action_type == 1){
				continue;
			} else if(a.action_type == 2){
				
				a.username = uidAndUsernameList.get(a.uid);
				talkList.add(a);
			} else if(a.action_type == 3) {
				a.username = uidAndUsernameList.get(a.uid);
				yellList.add(a);
			}


		}

	}
}
