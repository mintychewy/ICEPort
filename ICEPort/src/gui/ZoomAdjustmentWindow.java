package gui;

import iceworld.ICEWorldView;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ZoomAdjustmentWindow extends JDialog {
	
	JLabel label;
	JTextField field;
	JButton setBtn;
	
	public ZoomAdjustmentWindow() {
		createGUI();
		addListeners();
		setPreferredSize(new Dimension(300,100));
		pack();
		setVisible(true);
		
	}
	
	public void createGUI() {
		this.setLayout(new GridLayout(2,1,0,0));
		label = new JLabel("Enter Zoom-Level in between 13-100 meters");
		
		JPanel container = new JPanel();
		field = new JTextField();
		setBtn = new JButton("Set");
		this.add(label);
		container.setLayout(new GridLayout(1,2,5,5));
		container.add(field);
		container.add(setBtn);
		this.add(container);
	}
	
	public void addListeners() {
		setBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					
					String s = field.getText();
					System.out.println("s = "+s);
					double d = Double.parseDouble(s);
					
					/* d is in "meters"
					 * meters 		      zoom_factor
					 *  100  		 <==>   	  1
					 *   90  		 <==>  	    0.9
					 *   .   		 <==>    	  .
					 *   .    		 <==>      	  .
					 *   .   		 <==>     	  .
					 *  13.888889	 <==> 1.3888889
					 *  anything below this results in the same zoom-level
					 */
					
					double zoom_level = 1.0;
					
					if(d >= 100){
						zoom_level = 1.0;
					}else if( d < 100 && d >= 14){
						zoom_level = d/100.0;
					}else{
						zoom_level = 0.13888889;
					}
					
					ICEWorldView.zoom_factor = zoom_level;
					LoginPage.app.view.zoomChanged();
					dispose();
				} catch(Exception ex){
					System.out.println("Invalid Input");
				}
			}
			
		});
	}
	
	
}
