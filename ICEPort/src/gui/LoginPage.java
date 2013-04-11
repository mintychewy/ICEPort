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
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import objects.ICEtizen;
import core.Application;

public class LoginPage extends JFrame {

	public LoginPage() {
		super("Login Page");
		createAndDisplayGUI();
	}

	private void createAndDisplayGUI() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.WHITE));
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

	ICEtizen testizen;
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
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		try {
			image = ImageIO
					.read(getClass().getResource("/images/loginbg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		createGUI();
	}

	public void createGUI() {
		
		
		setLayout(null);
		
		JButton aliens = new JButton("log in as alien");
		JButton history=new JButton("history");
		JButton inhabitant = new JButton("log in");
		
		userField = new JTextField(11);
		passField = new JPasswordField(11);
		
		userField.setBounds(70, 55, 170, 50);
		add(userField);
		passField.setBounds(360, 55, 170, 50);
		add(passField);
		aliens.setBounds(40,120,160,50);
		add(aliens);
		inhabitant.setBounds(230, 120, 160,50);
		add(inhabitant);
		history.setBounds(420, 120, 160, 50);
		add(history);
		

		

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
						
						
						//
						Application.login.setVisible(false);
					} else {
						failure++;
						label.setText("incorrect username or password" + "\t"
								+ failure + "/3");
						if (failure == 1)
							time1 = getUnixTime();
						else if (failure == 2) {
							time2 = getUnixTime();
							if ((time2 - time1) >= 3) {

								failure = 0;
								// label.setText("incorrect username or password"+
								// "\t"+ failure+"/3");

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
					Application.login.setVisible(false);
				}
			}
		});

		

	}
	
	public boolean authenticateAlien(){
		testizen = new ICEtizen();
		ICEWorldImmigration immigration = new ICEWorldImmigration(testizen);
		return immigration.loginAlien();
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
		testizen = new ICEtizen();
		testizen.setUsername(user);
		ICEWorldImmigration immigration = new ICEWorldImmigration(testizen);
		boolean status = immigration.login(pass);
		System.out.println("Success?: "+status);
		return status;
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(600, 195));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension d=this.getSize();
		
		g.drawImage(image, 0, 0, this);
	}

}
