package iceworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AnimationTestPanel extends JPanel {
	public AnimationTestPanel() {
		setPreferredSize(new Dimension(900,600));

		//repaint();
	}
	
	public void paintComponent(Graphics g){

		g.setColor(Color.black);
		g.drawString("HELLOnajsdfajisldfjas",200,200);
	}
}
