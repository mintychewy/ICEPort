package util;



import gui.LoginPage;
import iceworld.given.IcetizenLook;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class Custimization {
	static AvatarLoader avatar;
	static String list;
	public static JPanel getAvatar(){
		try {
			list = ICEWorldPeek.getData("gresources&uid=0").substring(20,ICEWorldPeek.getData("gresources&uid=0").length()-2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		avatar=new AvatarLoader(list);
		avatar.setPreferredSize(new Dimension(500,500));
		avatar.setVisible(true);
	     return avatar; 
	}

	
	public static JFrame customizeWindow(){
		JFrame customise=new JFrame("customize");
		customise.setSize(1000, 600);
		customise.setLayout(new GridLayout(1,2,50,10));
		TitledBorder idHeader = new TitledBorder("id");
		TitledBorder lookHeader = new TitledBorder("your look");
		JPanel id=new JPanel();
		id.setBorder(idHeader);
		JPanel look=(JPanel) getAvatar();
		look.setBorder(lookHeader);
		customise.add(id);
		customise.add(look);
		
		id.setLayout(new GridLayout(10,2,20,5));
		JButton check1=new JButton("next");
		check1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.headCount=(avatar.headCount+1)%avatar.numHead;
				avatar.repaint();
			}
		});
		
		
		JButton check2=new JButton("next");
		check2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.bodyCount=(avatar.bodyCount+1)%avatar.numBody;
				avatar.repaint();
			}
		});
		
		JButton check3=new JButton("next");
		check3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.shirtCount=(avatar.shirtCount+1)%avatar.numShirt;
				avatar.repaint();
			}
		});
		
		JButton check4=new JButton("next");
		check4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.weaponCount=(avatar.weaponCount+1)%avatar.numWeapon;
				avatar.repaint();
			}
		});
		
		JButton confirm=new JButton("confirm");
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			//	System.out.println(avatar.bodyURL);
				//System.out.println(avatar.headURL);
				//System.out.println(avatar.weaponURL);
				//System.out.println(avatar.shirtURL);
				IcetizenLook look=new IcetizenLook();
				look.gidB=avatar.bodyURL ;
				look.gidH=avatar.headURL;
				look.gidW=avatar.weaponURL;
				look.gidS=avatar.shirtURL;
			//	System.out.println(look.gidB);
				LoginPage.immigration.customization(look);

				
				
				
			}
		});
		
		id.add(new JLabel("head"));
		id.add(check1);
		id.add(new JLabel("body"));
		id.add(check2);
		id.add(new JLabel("shirt"));
		id.add(check3);
		id.add(new JLabel("weapon"));
		id.add(check4);
		id.add(confirm);
		customise.setVisible(true);
		return customise;
	}
	
	
	
	public static void main(String[] args){
		
		customizeWindow();
	}
}
