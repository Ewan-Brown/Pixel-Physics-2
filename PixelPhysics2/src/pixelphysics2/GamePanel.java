package pixelphysics2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements Runnable,MouseListener{
	int sleep = 10;
	static BufferedImage bi = new BufferedImage(1920,1080,BufferedImage.TYPE_3BYTE_BGR);
	static ArrayList<Point> clicks = new ArrayList<Point>();
	static boolean click = false;
	public GamePanel() {
		addMouseListener(this);
		JFrame f = new JFrame("GamePanel");
		f.setSize(500, 500);
		f.add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		Thread t1 = new Thread(this);
		t1.start();
	}
	public boolean isInside(Particle p){
		return false;
	}
	public void paint (Graphics gr) {
		int xOff = this.getLocationOnScreen().x;
		int yOff = this.getLocationOnScreen().y;
		gr.drawImage(bi, 0 - xOff, 0 - yOff, null);
	}
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	public static void update(){
		if(click){
			clicks.add(MouseInfo.getPointerInfo().getLocation());
		}
		for(int i = 0; i < clicks.size();i++){
			int baseX = clicks.get(i).x;
			int baseY = clicks.get(i).y;
			for(int j = 0; j < Main.particles.length;j++){
				Particle p = Main.particles[j];
				double dist = getDistance(p.x, p.y, baseX, baseY);
				double deltaX = (p.x - baseX) / dist;
				double deltaY = (p.y - baseY) / dist;
				p.vX -= deltaX / 10;
				p.vY -= deltaY / 10;
			}
		}
		clicks.clear();
		Graphics grb = bi.getGraphics();
		grb.setColor(Color.BLACK);
		for(int i = 0; i < Data.particleNum;i++){
			Particle p = Main.particles[i];
			double x = p.x;
			double y = p.y;
			double lastX = p.lastX;
			double lastY = p.lastY;
//			if(lastX < x){
//				double tempX = x;
//				double tempY = y;
//				x = lastX;
//				y = lastY;
//				lastX = tempX;
//				lastY = tempY;
//			}
			double m = (lastY - y)/(lastX - x);
			double m2 = -1/m;
			int[] xA = new int[4];
			int[] yA = new int[4];
			double a = Math.atan(m2);
			double dX = Math.cos(a) * (double)p.size / 2;
			double dY = Math.sin(a) * (double)p.size / 2;
			xA[0] = (int) (x + dX);
			yA[0] = (int) (y + dY);
			xA[1] = (int) (lastX + dX);
			yA[1] = (int) (lastY + dY);
			xA[2] = (int) (lastX - dX);
			yA[2] = (int) (lastY - dY);
			xA[3] = (int) (x - dX);
			yA[3] = (int) (y - dY);
			Polygon poly = new Polygon(xA,yA,4);
			int s = p.size;
			grb.setColor(new Color(p.RGB));
			grb.fillPolygon(poly);
//			grb.drawLine((int)x, (int)y, (int)lastX, (int)lastY);
			grb.fillOval((int)x - s/2, (int)y - s/2, s, s);
		}
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		click = true;
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		click = false;
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
