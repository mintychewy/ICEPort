package gui;
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
import javax.swing.SwingUtilities;

public class LoginPage {

	private void createAndDisplayGUI() {

		JFrame frame = new JFrame("ICE WORLD");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.WHITE));
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout(10, 10));

		ImagePanel imagePanel = new ImagePanel();

		contentPane.add(imagePanel, BorderLayout.CENTER);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginPage().createAndDisplayGUI();
			}
		});
	}
}

class ImagePanel extends JPanel {
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
					.read(new URL(
							"http://iceworld.sls-atl.com/sites/default/files/logo.png"));
		} catch (Exception e) {
			e.printStackTrace();
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

		add(loginPanel);

		JLabel userLabel = new JLabel("USERNAME : ");
		userLabel.setForeground(Color.BLACK);
		userField = new JTextField(10);
		JLabel passLabel = new JLabel("PASSWORD : ");
		passLabel.setForeground(Color.BLACK);
		passField = new JPasswordField(10);

		inhabitant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = new String(userField.getText());
				String password = new String(passField.getText());
				String[] user = { username, getUnixTime() + "" };

				if (isInBlackList(user)) {
				
					label.setText("blocked!");
					System.out.println("userBlock");
				} else {

					
					  if(username.equals("") || password.equals("")){
					  label.setForeground(Color.RED);
					  label.setText("empty username/password!"); }
					
					// correct username and password
					  else if (authenticate("usernameTest", "passwordTest")) {
						label.setForeground(Color.BLACK);
						label.setText("logged in");
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
							if ((time3 - time1) >= 3) {
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
				login();
			}
		});

		inhabitantLogIn.add(userLabel);
		inhabitantLogIn.add(userField);
		inhabitantLogIn.add(passLabel);
		inhabitantLogIn.add(passField);

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
		for(int i=0;i<blackList.size();i++){
			//for(String[] s : blackList){
				if(blackList.get(i)[0].equals(user[0])){
					if((getUnixTime() - Integer.parseInt(blackList.get(i)[1])) <5){
						return true;
					}else{
						blackList.remove(i);
					}
				}
			}
			
			return false;
	}

	public boolean authenticate(String user, String pass) {

		// wait for the real implementation from mederic

		/*
		 * 
		 * boolean success = ICEWorldImmigration(user,pass); return success;
		 */

		// test
		if (user.equals("a") && pass.equals("b"))
			return true;

		//
		return false;
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(1000, 700));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

}

