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



// CAN I DELETE THE PROJECT?yep

public class FTPButtonListener implements ActionListener {

	// @Override
	// JDesktopPane desktop;
	JButton sendButton;
	JPanel centerPanel;
	JTextField path;
	JDialog choose;

	String filename;

	public void actionPerformed(ActionEvent e) {

		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(3, 1));

		JButton chooseF = new JButton("Choose File");
		// setLayout(new BorderLayout());

		centerPanel = new JPanel();
		// add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(chooseF);
		chooseFileEvent cf = new chooseFileEvent();
		chooseF.addActionListener(cf);
		pan.add(chooseF);

		path = new JTextField();
		pan.add(path);
		filename = path.getText();

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
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			FileChooser a = new FileChooser();
			path.setText(a.FileChooser());

		}

	}

	public class OKEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			try {
				new FileServer(8799, path.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			choose.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		}

	}
}
