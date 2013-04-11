package gui;

import iceworld.given.ICEWorldImmigration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import objects.ICEtizen;
import util.ICEWorldPeek;
import core.Application;


public class LoginPage extends JFrame {

	public static ICEWorldImmigration immigration;
	public static ApplicationMainFrame app;
	public static ICEtizen me;
	public LoginPage() {
		super("Login Page");
		
		if(!ICEWorldPeek.isReachable("http://iceworld.sls-atl.com")){
			System.out.println("unreachable");
			System.exit(0);
		}
			
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


		filePath = (ClassLoader.getSystemClassLoader().getResource("logs/log.txt").toString()).substring(5);
		setOpaque(true);
		try {
			image = ImageIO
					.read(new URL(
						"http://iceworld.sls-atl.com/sites/default/files/logo.png"));
		} catch (Exception e) {
			System.out.println("System Unreachable");
		}
		createGUI();
	}

	public void createGUI() {
		setLayout(new GridBagLayout());
		JPanel inhabitantLogIn = new JPanel();
		inhabitantLogIn.setOpaque(true);
		inhabitantLogIn.setBackground(new Color(0f, 0f, 0f, 0f));
		label = new JLabel();
		label.setForeground(Color.BLACK);
		inhabitantLogIn.setLayout(new GridLayout(2, 2, 2, 2));
		JButton aliens = new JButton("aliens");
		aliens.setSize(2, 1);
		JButton inhabitant = new JButton("inhabitants");
		final JPanel loginPanel = new JPanel();
		loginPanel.setOpaque(false);
		loginPanel.setLayout(new GridLayout(6, 1, 2, 2));
		loginPanel.add(inhabitantLogIn);
		loginPanel.add(inhabitant);
		loginPanel.add(label);
		loginPanel.add(aliens);

		JButton history = new JButton("History");
		loginPanel.add(history);
		add(loginPanel);

		JLabel userLabel = new JLabel("USERNAME : ");
		userLabel.setForeground(Color.BLACK);
		userField = new JTextField(10);
		JLabel passLabel = new JLabel("PASSWORD : ");
		passLabel.setForeground(Color.BLACK);
		passField = new JPasswordField(10);


		history.addActionListener(new ActionListener(){

			
			@Override
			public void actionPerformed(ActionEvent e) {

				TreeSet<String> usernameList = new TreeSet<String>();
				BufferedReader reader;
				try {
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
				
					final JComboBox comboBox = new JComboBox(
							usernameList.toArray());
					
				//comboBox.setSelectedIndex(0);
				
				comboBox.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// fill the username field when one is selected
						userField.setText((String) comboBox.getSelectedItem());
					}
					
				});
			
				
				JDialog dia = new JDialog();
				dia.add(comboBox);
				dia.setVisible(true);
				// show JTable 
				while(!usernameList.isEmpty()){
					System.out.println(usernameList.pollFirst());
				}
				// doubleclick on the JTable fills 
				// the username field

			}

		});

		inhabitant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = new String(userField.getText());
				String password = new String(passField.getPassword());
				String[] user = { username, getUnixTime() + "" };

				if (isInBlackList(user)) {

					label.setText("blocked!");
					System.out.println("userBlock");
				} else {

					if (username.equals("") || password.equals("")) {
						label.setForeground(Color.RED);
						label.setText("Username / Password cannot be empty!");
					}

					// correct username and password
					else if (authenticate(username, password)) {

						label.setForeground(Color.BLACK);
						label.setText("logged in");

						// add username to the history
						try {
							//LogFile.write(username);

							FileWriter writer = new FileWriter(filePath, true);
							writer.write(username+"\n");
							writer.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//
						
						javax.swing.SwingUtilities.invokeLater(new Runnable(){
							public void run(){
								LoginPage.app = new ApplicationMainFrame();
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

						/*
						 * failure++; Calendar c=Calendar.getInstance();
						 * 
						 * if(failure==1){sec1=c.get(Calendar.SECOND);} else
						 * if(failure==2){sec2=c.get(Calendar.SECOND);
						 * if((sec2-sec1)>=3){ failure=0;
						 * label.setText("incorrect username or password"+ "\t"+
						 * failure+"/3");
						 * 
						 * } } else if(failure==3){ sec3=c.get(Calendar.SECOND);
						 * if((sec3-sec1)>3){ failure=0;
						 * label.setText("incorrect username or password"+ "\t"+
						 * failure+"/3");
						 * 
						 * }
						 * 
						 * else{
						 * 
						 * try {
						 * 
						 * Thread.sleep(5000); failure=0; } catch
						 * (InterruptedException e1) { // TODO Auto-generated
						 * catch block e1.printStackTrace();
						 * 
						 * } } } label.setText("incorrect username or password"+
						 * "\t"+ failure+"/3");
						 */
					}
				}
			}

		});

		aliens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(authenticateAlien()){
					System.out.println("alien logged in ");
					
					javax.swing.SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							LoginPage.app = new ApplicationMainFrame();
						}
					});
					Application.login.setVisible(false);
				}
			}
		});

		inhabitantLogIn.add(userLabel);
		inhabitantLogIn.add(userField);
		inhabitantLogIn.add(passLabel);
		inhabitantLogIn.add(passField);

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
		LoginPage.immigration = new ICEWorldImmigration(LoginPage.me);
		boolean status = LoginPage.immigration.login(pass);
		System.out.println("Success?: "+status);
		return status;
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(600, 300));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.drawImage(image, 0, 0, this);
	}

}
