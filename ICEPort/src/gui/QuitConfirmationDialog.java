package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuitConfirmationDialog extends JDialog {
	
	JLabel message;
	JButton yesBtn, noBtn;
	public QuitConfirmationDialog(){
		JLabel message = new JLabel("Are you sure you want to quit?");
		yesBtn = new JButton("Yes");
		noBtn = new JButton("No");
		this.setLayout(new GridLayout(2,1,5,5));
		this.add(message);
		
		JPanel buttonsPanel = new JPanel();
		
		buttonsPanel.add(yesBtn);
		buttonsPanel.add(noBtn);
		
		this.add(buttonsPanel);
		this.setPreferredSize(new Dimension(300,150));
		this.pack();
		
		addListeners();
	}

	private void addListeners(){
		noBtn.addActionListener(new QuitListener());
	
		yesBtn.addActionListener(new QuitListener());

}
	
	public class QuitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractButton btn = (AbstractButton) e.getSource();
			if(btn.getText().equals("No")){
				MenuBarListener.confirmDialog.dispose();
			}else{
				if(LoginPage.immigration.logout())
					System.exit(0);
			}
		}
		
	}
}
