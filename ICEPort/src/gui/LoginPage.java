package gui;

import iceworld.given.ICEWorldImmigration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import objects.ICEtizen;
import core.Application;


public class LoginPage extends JFrame {
	public static int uniquePosition;
	public static ICEWorldImmigration immigration;
	public static ApplicationMainFrame app;
	public static ICEtizen me;
	public static boolean MUTE_ON=false;
	public LoginPage() {
		super("Login Page");
		
		/*
		if(!ICEWorldPeek.isReachable("http://iceworld.sls-atl.com")){
			System.out.println("unreachable");
			System.exit(0);
		}
		*/
			
		createAndDisplayGUI();
	}

	private void createAndDisplayGUI() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout(10, 10));

		ImagePanel imagePanel = new ImagePanel();

		contentPane.add(imagePanel, BorderLayout.CENTER);
		this.setContentPane(contentPane);
		this.pack();
		this.setLocationByPlatform(true);
		this.setVisible(true);
	}

}

class ImagePanel extends JPanel {
	String filePath;

	private static final long serialVersionUID = -7940935013415887043L;

	JTextField userField;
	JPasswordField passField;
	JLabel label;
	int failure = 0;
	long time1, time2, time3;
	ArrayList<String[]> blackList = new ArrayList<String[]>();
	private BufferedImage image;

	public ImagePanel() {


		
		setOpaque(true);
		try {
			image = ImageIO.read(getClass().getResource("/images/loginbg.png"));
		} catch (Exception e) {
			System.out.println("System Unreachable");
		}
		createGUI();
	}

	public void createGUI() {
		
		
		setLayout(null);
		
		JButton aliens = new JButton("Login as Alien");
		JButton history=new JButton("History");
		JButton inhabitant = new JButton("Login");
		
		userField = new JTextField(11);
		passField = new JPasswordField(11);
		
		userField.setText("Avika.L");
		passField.setText("2516432");
		label = new JLabel("");
		
		userField.setBounds(70, 55, 170, 50);
		add(userField);
		passField.setBounds(350, 55, 170, 50);
		add(passField);
		aliens.setBounds(30,120,160,50);
		add(aliens);
		inhabitant.setBounds(220, 120, 160,50);
		add(inhabitant);
		history.setBounds(410, 120, 160, 50);
		add(history);
		label.setBounds(200,155,500,50);
		add(label);

		history.addActionListener(new ActionListener(){

			
			@Override
			public void actionPerformed(ActionEvent e) {

				TreeSet<String> usernameList = new TreeSet<String>();
				BufferedReader reader;
				try {
					
					System.out.println("PATH: "+getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
					filePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +"log.txt";
				
					reader = new BufferedReader(new FileReader(filePath));

					String line;

					while((line=reader.readLine())!=null){
						usernameList.add(line);
					}
			
				} catch (FileNotFoundException e1) {
					System.out.println("No logs found");
				} catch (IOException e1) {
				}

			System.out.println("TreeSet"+usernameList.toString());
				
			
				
				if(usernameList.isEmpty()){
					usernameList.add("No logged-in history yet");
				}else if(usernameList.size() == 1){
					String s = usernameList.pollFirst();
					if(s.equals("No logged-in history yet"))
						usernameList.clear();
					else
						usernameList.add(s);
				}else{}
				
				
				final JDialog dia = new JDialog();
					final JComboBox comboBox = new JComboBox(
							usernameList.toArray());
					
				//comboBox.setSelectedIndex(0);
				
				comboBox.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// fill the username field when one is selected
						userField.setText((String) comboBox.getSelectedItem());
						dia.dispose();
					}
					
				});
				
				dia.add(comboBox);
				dia.pack();
				dia.setLocation(Application.screenDimension.width/2,Application.screenDimension.height/2);
				dia.setVisible(true);
	
				while(!usernameList.isEmpty()){
					System.out.println(usernameList.pollFirst());
				}
			

			}

		});

		inhabitant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = new String(userField.getText());
				String password = new String(passField.getPassword());
				String[] user = { username, getUnixTime() + "" };

				if (isInBlackList(user)) {

					label.setText("User blocked!");
				} else {

					if (username.equals("") || password.equals("")) {
						label.setForeground(Color.WHITE);
						label.setText("Username / Password cannot be empty!");
					}

					// correct username and password
					else if (authenticate(username, password)) {

						// add username to the history
						try {
							//LogFile.write(username);
							filePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +"log.txt";

							FileWriter writer = new FileWriter(filePath, true);
							
							
							writer.write(username+"\n");
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						//
						
						javax.swing.SwingUtilities.invokeLater(new Runnable(){
							public void run(){
								LoginPage.app = new ApplicationMainFrame();
								LoginPage.app.setLocation(Application.screenDimension.width/2-450,Application.screenDimension.height/2-450);
							}
						});
						
						Application.login.setVisible(false);
	
						
						
					} else {
						failure++;
						label.setText("incorrect username or password" + "\t"
								+ failure + "/3");
						if (failure == 1)
							time1 = getUnixTime();
						else if (failure == 2) {
							time2 = getUnixTime();
							if ((time2 - time1) >= 180) {

								failure = 0;

							}

						}
						if (failure == 3) {
							time3 = getUnixTime();
							if ((time3 - time1) >= 180) {
								failure = 0;
								// label.setText("incorrect username or password"+
								// "\t"+ failure+"/3");
							} else {
								addToBlackList(user);
								failure = 0;
							}
						}

	
					}
				}
			}

		});

		aliens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(authenticateAlien()){
					
					System.out.println("alien logged in ");
					
					// Assign a unique position so we know this is the controller
					LoginPage.uniquePosition = 10000 + (int)(Math.random() * ((20000 - 10000) + 1));
					LoginPage.immigration.walk(LoginPage.uniquePosition,-245);
					javax.swing.SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							LoginPage.app = new ApplicationMainFrame();
							LoginPage.app.setLocation(Application.screenDimension.width/2-450,Application.screenDimension.height/2-450);

						}
					});
					label.setText("");

					Application.login.setVisible(false);
				}
			}
		});

		

	}

	public boolean authenticateAlien(){
		LoginPage.me = new ICEtizen();
		LoginPage.me.setType(0);
		LoginPage.me.setListeningPort(10000 + (int)(Math.random() * ((20000 - 10000) + 1)));
		LoginPage.immigration = new ICEWorldImmigration(LoginPage.me);
		return LoginPage.immigration.loginAlien();
	}

	public long getUnixTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public void login() {

	}

	public void addToBlackList(String[] s) {
		blackList.add(s);
	}

	public boolean isInBlackList(String[] user) {
		for (int i = 0; i < blackList.size(); i++) {
			// for(String[] s : blackList){
			if (blackList.get(i)[0].equals(user[0])) {
				if ((getUnixTime() - Integer.parseInt(blackList.get(i)[1])) < 300) {
					return true;
				} else {
					blackList.remove(i);
				}
			}
		}

		return false;
	}

	public boolean authenticate(String user, String pass) {
		System.out.println("authenticating user: "+user+" pass: "+pass);
		LoginPage.me = new ICEtizen();
		LoginPage.me.setType(1);
		LoginPage.me.setUsername(user);
		LoginPage.me.setListeningPort(10000 + (int)(Math.random() * ((20000 - 10000) + 1)));
		System.out.println("set listeningport to "+ LoginPage.me.getListeningPort());
		LoginPage.immigration = new ICEWorldImmigration(LoginPage.me);
		boolean status = LoginPage.immigration.login(pass);
		System.out.println("Success?: "+status);
		label.setText("");
		return status;
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(600, 195));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(image, 0, 0, this);
	}

}
