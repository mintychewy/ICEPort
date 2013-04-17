package gui;

import iceworld.HelpDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


@SuppressWarnings("serial")
public class CustomMenuBar extends JMenuBar{
	
	// "Settings" menu
	JMenu settingsMenu;
	JMenuItem talkDurationMenuItem, customiseAvatarMenuItem, refreshRateMenuItem, fileDirectoryMenuItem;
	
	// "Help" menu
	JMenu helpMenu;
	JMenuItem quitMenuItem, userManualMenuItem, aboutMenuItem;
	
	// Windows
	HelpDialog helpDialog;
	JDialog x;
	public CustomMenuBar(){
		setMenuBarGUI();
		addListeners();
	}	
	

	public void setMenuBarGUI(){
	
		settingsMenu = new JMenu("Settings");
		talkDurationMenuItem = new JMenuItem("Talk Duration");
		customiseAvatarMenuItem = new JMenuItem("Customise Avatar");
		refreshRateMenuItem = new JMenuItem("Refresh Rate");
		fileDirectoryMenuItem = new JMenuItem("Set received file destination");
		settingsMenu.add(customiseAvatarMenuItem);
		settingsMenu.add(talkDurationMenuItem);
		settingsMenu.add(refreshRateMenuItem);
		settingsMenu.add(fileDirectoryMenuItem);
		
		helpMenu =  new JMenu("Help");
		
		quitMenuItem = new JMenuItem("Quit");
		userManualMenuItem = new JMenuItem("User Guide");
		aboutMenuItem = new JMenuItem("About");
		
		// set F1 shortcut for "User's Guide" menu item 
		userManualMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		
		helpMenu.add(userManualMenuItem);
		helpMenu.add(aboutMenuItem);
		helpMenu.add(quitMenuItem);
		
		this.add(settingsMenu);
		this.add(helpMenu);

	}
	
	
	public void addListeners(){
		fileDirectoryMenuItem.addActionListener(new MenuBarListener());
		talkDurationMenuItem.addActionListener(new MenuBarListener());
		customiseAvatarMenuItem.addActionListener(new MenuBarListener());
		userManualMenuItem.addActionListener(new MenuBarListener());
		aboutMenuItem.addActionListener(new MenuBarListener());
		quitMenuItem.addActionListener(new MenuBarListener());
		refreshRateMenuItem.addActionListener(new MenuBarListener());
		
		userManualMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				helpDialog = new HelpDialog();
				x = new JDialog();
				x.add(helpDialog);
				x.pack();
				x.setVisible(true);
			}
			
		});
	}
}
