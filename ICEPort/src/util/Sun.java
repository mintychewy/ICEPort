package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Sun extends JPanel implements Runnable {
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
		x+=3;// y+=2;
		Dimension d = this.getSize();
		g.setColor(Color.white);
		g.fillRect(0,0,d.width,d.height);
		
	
		int y=(int)(Math.random()*30)+100;
		for(int i=0 ; i<10;i++){
			int r=(int)(Math.random()*50)+200;
			int x2=(int)((300+x)+r*Math.sin(Math.toRadians(36*i)));
			int y2=(int)((100+y)+r*Math.cos(Math.toRadians(36*i)));		
		//	g.setColor(Color.BLACK);
		//	g.drawLine(300+x,100+y,x2,y2);		
			drawThickLine(g,300+x,100+y,x2,y2,4,Color.RED); //red line animated
			int x3=(int)((300+x)+200*Math.sin(Math.toRadians(18+36*i)));
			int y3=(int)((100+y)+200*Math.cos(Math.toRadians(18+36*i)));	
			drawThickLine(g,300+x,100+y,x3,y3,6,Color.ORANGE); // orange line still
		}
		
		g.setColor(new Color(255,215,0));
		g.fillOval(200+x, y, 200, 200);
		g.setColor(new Color(255,165,0));
		g.fillOval(210+x, y+10, 180, 180);
		g.setColor(new Color(255,127,0));
		g.fillOval(220+x, y+20, 160, 160);
		
		
		start();
		
	}
	public void drawThickLine(
			  Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c) {
			  // The thick line is in fact a filled polygon
			  g.setColor(c);
			  int dX = x2 - x1;
			  int dY = y2 - y1;
			  // line length
			  double lineLength = Math.sqrt(dX * dX + dY * dY);

			  double scale = (double)(thickness) / (2 * lineLength);

			  // The x,y increments from an endpoint needed to create a rectangle...
			  double ddx = -scale * (double)dY;
			  double ddy = scale * (double)dX;
			  ddx += (ddx > 0) ? 0.5 : -0.5;
			  ddy += (ddy > 0) ? 0.5 : -0.5;
			  int dx = (int)ddx;
			  int dy = (int)ddy;

			  // Now we can compute the corner points...
			  int xPoints[] = new int[4];
			  int yPoints[] = new int[4];

			  xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
			  xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
			  xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
			  xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

			  g.fillPolygon(xPoints, yPoints, 4);
			  }
	

}
