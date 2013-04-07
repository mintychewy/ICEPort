package util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame{

	private static final long serialVersionUID = 7071755398042013311L;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem about, close, closeAll, exit, help;
	JDesktopPane desktop;
	JButton soundButton;
	JPanel centerPanel;
	JSlider BGSound;
	Sound sound;
	
	public Window(){
		sound = new Sound("grooving.wav");
		desktop = new JDesktopPane();
		desktop.setBackground(Color.WHITE);
		
		setContentPane(desktop);
		setJMenuBar(makeMenuBar());
	}
	
	public JMenuBar makeMenuBar(){
		menuBar= new JMenuBar();
		fileMenu= new JMenu("File");
		menuBar.add(fileMenu);
		
		
		AboutEvent a= new AboutEvent();
		about = new JMenuItem("About");
		about.addActionListener(a);
		fileMenu.add(about);
		

        HelpEvent h = new HelpEvent ();
        help= new JMenuItem("Help");
        help.addActionListener(h);
        fileMenu.add(help);
        
		
		CloseEvent c=new CloseEvent();
        close=new JMenuItem("Close");
        close.addActionListener(c);
        fileMenu.addSeparator();
        fileMenu.add(close);
        
        CloseAllEvent ca=new CloseAllEvent();
        closeAll=new JMenuItem("Close All");
        closeAll.addActionListener(ca);
        fileMenu.add(closeAll);
        
        ExitEvent ex=new ExitEvent();
        exit=new JMenuItem("Exit");
        exit.addActionListener(ex);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        
        soundButton = new JButton("Click");
        setLayout(new BorderLayout());
        centerPanel= new JPanel();
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(soundButton);
        SoundEvent s = new SoundEvent();
        soundButton.addActionListener(s);
        
        
        
		setJMenuBar(menuBar);
		return menuBar;
	}
	
	public class ExitEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}
	public class SoundEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("Blah");
			
			JDialog dialog = new JDialog();
			BGSound = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
			BGSound.setMajorTickSpacing(10);
			BGSound.setPaintTicks(true);
			dialog.add(BGSound);
			
			event slide = new event();
			BGSound.addChangeListener(slide);
			
			
			dialog.setLocationRelativeTo(null);
			dialog.pack();
			dialog.setVisible(true);
		}
	}
	public class event implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e){
			System.out.println("Boo");
			
			int value = BGSound.getValue();
			if(value <50){
				sound.increase();				
			}else sound.decrease();
			
		}
	}
	public class HelpEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String message = "Help";
			JOptionPane.showMessageDialog(new JFrame(), message, "Help",
        	        JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public class AboutEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String message= "About";
			JOptionPane.showMessageDialog(new JFrame(), message, "About",
        	        JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public class CloseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
            desktop.getSelectedFrame().setVisible(false);
           
		}
	}
	
	public class CloseAllEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
            JInternalFrame[] a= desktop.getAllFrames();
            for(int i=0;i<a.length;i++){
                    a[i].setVisible(false);
            }
            }
    }
  
	
	
	public static void main(String [] args){
		
		Window gui= new Window();
		
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setTitle("ICE");
        gui.setSize(800,760);
	}
	
	
}