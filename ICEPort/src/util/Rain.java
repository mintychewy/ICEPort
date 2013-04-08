package util;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Rain extends JPanel{
	public void paintComponent(Graphics g){
	for(int i=0;i<150;i++){
		g.setColor(Color.BLACK);
		int x=(int)(Math.random()*900)+1;
		int y=(int)(Math.random()*720)+1;
		int size=(int)(Math.random()*6+10);
		g.drawLine(x, y, x+size, y+size);
		}
	}	

}
