//ServerWindow!!!!!!!!

package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objects.ICEtizen;

public class FTPButtonListener implements ActionListener {

	// @Override
	// JDesktopPane desktop;
	JButton sendButton;
	JPanel centerPanel;
	JTextField path, username;
	JDialog choose;

	String filename, user;
	ICEtizen use;

	public void actionPerformed(ActionEvent e) {

		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(4, 1));

		JButton chooseF = new JButton("Choose File");
		// setLayout(new BorderLayout());

		path = new JTextField();
		pan.add(path);
		filename = path.getText();

		centerPanel = new JPanel();
		// add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(chooseF);
		chooseFileEvent cf = new chooseFileEvent();
		chooseF.addActionListener(cf);
		pan.add(chooseF);

		username = new JTextField();
		pan.add(username);
		username.setText("Please enter username.");
		user = username.getText();

		JButton ok = new JButton("OK");
		// setLayout(new BorderLayout());
		centerPanel = new JPanel();
		// add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(ok);
		OKEvent con = new OKEvent();
		ok.addActionListener(con);
		pan.add(ok);

		choose = new JDialog();

		choose.add(pan);
		choose.setLocationRelativeTo(null);
		choose.setSize(500, 100);
		// choose.pack();
		choose.setVisible(true);
	}

	public class chooseFileEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.print("yoooooooooooooooooooooooooooooooo");
			FileChooser a = new FileChooser();
			path.setText(a.FileChooser());

		}

	}

	public class OKEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// username.equals(use);
			// System.out.print(path.getText());
			System.out.print("yooooooooohhhhhhhhhhhhhhhhhoooooooooooooo");
			use = LoginPage.app.view.loggedinUsers.get(user);
			System.out.print("heyyy");
			// System.out.println("Is listening port null? "+(use.getListeningPort()
			// == null));
			try {

				System.out.println("is path null?" + (path.getText() == null));
				new FileServer(use.getListeningPort(), path.getText());

				System.out.print("heloooo");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			choose.dispose();

		}

	}

}
