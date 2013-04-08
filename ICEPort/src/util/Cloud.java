package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Cloud extends JPanel implements Runnable {
	protected Thread thread;
	protected int delay;
	int x=0;int y=0;

	public void init(){
		thread=null;
		
	}
	public void start(){
		if(thread==null){
			thread=new Thread(this);
			thread.start();
		}
	}
	public void run(){
		while(Thread.currentThread()==thread){
		repaint();
		try {
			Thread.currentThread();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void paintComponent(Graphics g){
	
		
		start();
		
	}
	
	

}
