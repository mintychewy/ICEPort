import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JTextField;


public class LoggingInOut {
	
	//private ArrayList<ArrayList<String>> listStudent = new ArrayList<ArrayList<String>>();
	private String username;
	private String password;
	public boolean isInhabitant = false;
	
	
	public LoggingInOut(){
		//this.listStudent = listStudent;
	}
	
	//private Inhabitants moi = new Inhabitants();
	//Inhabitants.loggingIn(username, password);

	//Verify the username and name in the file of the studentlist
	public void main(String[] args){
		
	//Pick the username and the name 
		JButton buttonInhabitant = new JButton("Inhabitant");
		JButton buttonAlien = new JButton("Alien");
		//JButton buttonOK = new JButton("OK"); 
		final JTextField t1 = new JTextField("username");
		final JTextField t2 = new JTextField("password");
		
		buttonInhabitant.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				username = t1.getText();
				password = t2.getText();
			} 
		}); 
		
		buttonAlien.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				username = " ";
				password=" ";
			} 
		}); 
		
			
	
		
	//Verify the username and password in the list!
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
	
		
    
			
	}
		
	
		
}
