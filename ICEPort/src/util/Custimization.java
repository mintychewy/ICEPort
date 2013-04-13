package util;



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
	public static JPanel getAvatar(){
		
		avatar=new AvatarLoader();
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
				avatar.headCount=(avatar.headCount+1)%4;
				avatar.repaint();
			}
		});
		
		
		JButton check2=new JButton("next");
		check2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.bodyCount=(avatar.bodyCount+1)%4;
				avatar.repaint();
			}
		});
		
		JButton check3=new JButton("next");
		check3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.shirtCount=(avatar.shirtCount+1)%4;
				avatar.repaint();
			}
		});
		
		JButton check4=new JButton("next");
		check4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				avatar.weaponCount=(avatar.weaponCount+1)%4;
				avatar.repaint();
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
		customise.setVisible(true);
		return customise;
	}
	
	
	
	public static void main(String[] args){
		
		customizeWindow();
	}
}
