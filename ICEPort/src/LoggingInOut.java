//package iceworld.sample;

import iceworld.given.ICEWorldImmigration;
import iceworld.given.IcetizenLook;
import iceworld.given.MyIcetizen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JTextField;
//import iceworld.given.*;
//import iceworld.given.*;


public class LoggingInOut {
	
	//private ArrayList<ArrayList<String>> listStudent = new ArrayList<ArrayList<String>>();
	private String username;
	private String password;
	public boolean isInhabitant = false;
	public boolean isAlien = false;
	public int nbOfTimes=0;
	public ArrayList<Integer> time = new ArrayList<Integer>();
	//public LogFile history = new LogFile();
	
	Inhabitant tester = new Inhabitant();
	ICEWorldImmigration immigration = new ICEWorldImmigration((iceworld.given.MyIcetizen) tester);
	//tester.setIcePortID(10008);
	
	
	public LoggingInOut(){
		//this.listStudent = listStudent;
	}
	
	//private Inhabitants moi = new Inhabitants();
	//Inhabitants.loggingIn(username, password)

	
	
	public void main(String[] args){
		
		///////////////////////////////////////////////////////////////////////////////	
		  
		while(isInhabitant == false){
		 try {
			 Thread login = new Thread() {
				 public void run() {
					 //Verify the username and password in the list! LG2-3
					 String garder = "";
					 String filePath = "ListStudent.txt";
					 try{ 
						 BufferedReader buff = new BufferedReader(new FileReader(filePath)); 
						 try { 
							 String line; 
							 // Lire le fichier ligne par ligne 
							 // La boucle se termine quand la méthode affiche "null" 
							 while ((line = buff.readLine()) != null) { 
								 //System.out.println(line);
								 garder+= line + "|";
							 }
							 //System.out.println("\n\ngarder = " + "\"" + garder + "\"");
							 Pattern pattern = Pattern.compile("\\|"); // import java.util.regex.Pattern;
							 String[] tabGarder = pattern.split(garder); 
							 //System.out.println("");
							 // Results
							 for(int i = 0; i < tabGarder.length; i++)
							 	{
								 String lGardee = tabGarder[i];
								 if (username == tabGarder[i] && password == tabGarder[i+1]){
									 isInhabitant = true;
								 }
								 //System.out.println("lGardee: " + "\"" + lGardee + "\"");
							 	} 
							 buff.close(); //Reading done
						 } 
						 catch (IOException e){ 
							 System.out.println(e.getMessage()); 
							 System.exit(1); 
						 } 
					 } 
					 catch (IOException e) { 
						 System.out.println(e.getMessage()); 
						 System.exit(1); 
					 } 
					 //Keep History in a logfile LG5
					 if (isInhabitant == true){
						 try {
					        LogFile.write(username);
					        LogFile.write(password);
					        immigration.login(password);
					    		
						 }catch (IOException e1) {}
					 }
					 
	          	 } 
		 		};
		 
		    int timeSize=time.size();
			if (nbOfTimes<3 && time.get(timeSize)-time.get(0)< 180) {
				login.start(); // cool you can loggin
			}
			else{
				int timeToSleep=5*60*1000;
				Thread.sleep(timeToSleep); // wait for 5mins
				nbOfTimes=0; // clear the nb of times
				time = new ArrayList<Integer>(); // clear the time spent
				
			}
		 }
		 catch(Exception e) {}
	///////////////////////////////////////////////////////////////////////////////////////////////////
		}
	
	}
	//Mistakes in username and password LG7
		
}