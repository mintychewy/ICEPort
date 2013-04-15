package gui;

import iceworld.given.IcetizenLook;
import util.AvatarLoader;
import util.ICEWorldPeek;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import util.AvatarLoader;

public class Customisation extends JDialog {

	private static final long serialVersionUID = -7468630185489471323L;
	static AvatarLoader avatar;
	JPanel id;
	JPanel look;
	
	static String list;
	
	
	public static JPanel getAvatar(){
		try {
			list = ICEWorldPeek.getData("gresources&uid=0").substring(20,ICEWorldPeek.getData("gresources&uid=0").length()-2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		avatar=new AvatarLoader(list);
		avatar.setPreferredSize(new Dimension(500,500));
		avatar.setVisible(true);
	     return avatar; 
	}

	
	public Customisation() {
		customizeWindow();
		pack();
	}
	

	public void customizeWindow(){
		
		this.setSize(1000, 600);
		this.setLayout(new GridLayout(1,2,50,10));
		TitledBorder idHeader = new TitledBorder("id");
		TitledBorder lookHeader = new TitledBorder("your look");
		JPanel id=new JPanel();
		id.setBorder(idHeader);
		JPanel look=(JPanel) getAvatar();
		look.setBorder(lookHeader);
		this.add(id);
		this.add(look);
		
		id.setLayout(new GridLayout(14,2,20,5));
		JButton check1=new JButton("next");
		JButton back1=new JButton("back");
		check1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.headCount=(avatar.headCount+1)%avatar.numHead;
				avatar.repaint();
			}
		});
		
		back1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if((avatar.headCount-1)<0){
					(avatar.headCount)=(avatar.headCount)+avatar.numHead;
					}
				avatar.headCount=Math.abs((avatar.headCount-1))%avatar.numHead;
				avatar.repaint();
			}
		});
		
		
		
		JButton check2=new JButton("next");
		JButton back2=new JButton("back");
		check2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.bodyCount=(avatar.bodyCount+1)%avatar.numBody;
				avatar.repaint();
			}
		});
		back2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if((avatar.bodyCount-1)<0){
					(avatar.bodyCount)=(avatar.bodyCount)+avatar.numBody;
					}
				avatar.bodyCount=Math.abs((avatar.bodyCount-1))%avatar.numBody;
				avatar.repaint();
			}
		});
		
		
		JButton check3=new JButton("next");
		JButton back3=new JButton("back");
		check3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.shirtCount=(avatar.shirtCount+1)%avatar.numShirt;
				avatar.repaint();
			}
		});
		back3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if((avatar.shirtCount-1)<0){
					(avatar.shirtCount)=(avatar.shirtCount)+avatar.numShirt;
					}
				avatar.shirtCount=Math.abs((avatar.shirtCount-1))%avatar.numShirt;
				avatar.repaint();
			}
		});
		
		
		JButton check4=new JButton("next");
		JButton back4=new JButton("back");
		check4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.weaponCount=(avatar.weaponCount+1)%avatar.numWeapon;
				avatar.repaint();
			}
		});
		back4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if((avatar.weaponCount-1)<0){
					(avatar.weaponCount)=(avatar.weaponCount)+avatar.numWeapon;
					}
				avatar.weaponCount=Math.abs((avatar.weaponCount-1))%avatar.numWeapon;
				avatar.repaint();
			}
		});
		JButton confirm=new JButton("confirm");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			
				
				IcetizenLook look=new IcetizenLook();
				look.gidB = avatar.bodyURL;
				look.gidH = avatar.headURL;
				look.gidW = avatar.weaponURL;
				look.gidS = avatar.shirtURL;
				//report to ICEWorld immigration
				LoginPage.immigration.customization(look);
				//get the look  refreshed

				LoginPage.me.setIcetizenLook(look);
				LoginPage.app.view.updateWorld();
				
			}
		});
		
		id.add(new JLabel("head"));
		id.add(back1);
		id.add(check1);
		id.add(new JLabel("body"));
		id.add(back2);
		id.add(check2);
		id.add(new JLabel("shirt"));
		id.add(back3);
		id.add(check3);
		id.add(new JLabel("weapon"));
		id.add(back4);
		id.add(check4);
		id.add(confirm);
		this.setVisible(true);
	}
	
}