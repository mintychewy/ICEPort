package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class Snow extends JPanel {

	public void paintComponent(Graphics g){
		Dimension d = this.getSize();
		//g.setColor(Color.white);
		g.clearRect(0,0,d.width,d.height);
		for(int i=0;i<100;i++){
		int x=(int)(Math.random()*900)+1;
		int y=(int)(Math.random()*750)+1;
		int size=(int)(Math.random()*5+8);
		g.setColor(Color.GRAY);
		g.drawOval(x, y, size,size );
		g.setColor(Color.WHITE);
		g.fillOval(x, y, size,size );
		//System.out.println(x+"\t"+y);
		
		}
	    }  
}
