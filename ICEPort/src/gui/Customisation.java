package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import util.AvatarLoader;

public class Customisation extends JDialog {

	private static final long serialVersionUID = -7468630185489471323L;
	AvatarLoader avatar;
	JPanel id;
	JPanel look;

	public Customisation(){
		this.setSize(1000,600);
		this.setLayout(new GridLayout(1,2,50,10));

		

		avatar = new AvatarLoader();

		TitledBorder idHeader = new TitledBorder("ID");
		TitledBorder lookHeader = new TitledBorder("Preview");

		id = new JPanel();
		id.setBorder(idHeader);

		look = (JPanel) avatar;
		look.setBorder(lookHeader);
		this.add(id);
		this.add(look);

		id.setLayout(new GridLayout(10, 2, 20, 5));
		JButton check1 = new JButton("next");
		check1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avatar.headCount = (avatar.headCount + 1) % 4;
				avatar.repaint();
			}
		});

		JButton check2 = new JButton("next");
		check2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avatar.bodyCount = (avatar.bodyCount + 1) % 4;
				avatar.repaint();
			}
		});

		JButton check3 = new JButton("next");
		check3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avatar.shirtCount = (avatar.shirtCount + 1) % 4;
				avatar.repaint();
			}
		});

		JButton check4 = new JButton("next");
		check4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avatar.weaponCount = (avatar.weaponCount + 1) % 4;
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


	}
	
}
