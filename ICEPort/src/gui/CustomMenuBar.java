package gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class CustomMenuBar extends JMenuBar{
	
	// "Settings" menu
	JMenu settingsMenu;
	JMenuItem talkDurationMenuItem, customiseAvatarMenuItem, refreshRateMenuItem;
	
	// "Help" menu
	JMenu helpMenu;
	JMenuItem quitMenuItem, userManualMenuItem, aboutMenuItem;
	
	public CustomMenuBar(){
		setMenuBarGUI();
		addListeners();
	}
	
	public void setMenuBarGUI(){
	
		settingsMenu = new JMenu("Settings");
		talkDurationMenuItem = new JMenuItem("Talk Duration");
		customiseAvatarMenuItem = new JMenuItem("Customise Avatar");
		refreshRateMenuItem = new JMenuItem("Refresh Rate");
		settingsMenu.add(customiseAvatarMenuItem);
		settingsMenu.add(talkDurationMenuItem);
		settingsMenu.add(refreshRateMenuItem);
		
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
		customiseAvatarMenuItem.addActionListener(new MenuBarListener());
		userManualMenuItem.addActionListener(new MenuBarListener());
		aboutMenuItem.addActionListener(new MenuBarListener());
		quitMenuItem.addActionListener(new MenuBarListener());
	}
}
