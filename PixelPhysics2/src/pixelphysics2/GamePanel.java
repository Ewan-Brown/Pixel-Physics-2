package pixelphysics2;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements Runnable{
	int sleep = 10;
	long tick = 0;
	Random rand = new Random();
	int vX = 0;
	int vY = 0;
	int aX = 0;
	int aY = 0;
	JFrame f;
	public GamePanel() {
		addMouseListener(new Inputerface());
		f = new JFrame("GamePanel");
		f.setSize(500,500);
		f.setLocation(rand.nextInt(1000), rand.nextInt(1000));
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
		gr.drawImage(Data.bi, 0 - xOff, 0 - yOff, null);
	}
	public void run() {
		while(true) {
			tick++;
			Point p = f.getLocationOnScreen();
			if(rand.nextDouble() < 0.3){
//				vX += rand.nextInt() - 1;
//				vY += rand.nextInt(3) - 1;
//				aX = rand.nextInt(3) - 1;
//				aY = rand.nextInt(3) - 1;
			}
			if(rand.nextDouble() < 0.5){
				vX += aX;
				vY += aY;
			}
			if(Math.abs(vX) > 3){
//				vX -= Math.signum(vX);
				aX = (int) -Math.signum(vX);
			}
			if(Math.abs(vY) > 3){
//				vY -= Math.signum(vY);
				aY = (int) -Math.signum(vY);
			}
			if(p.x < 0 || p.x + getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()){
				vX = -vX;
				aX = (int)Math.signum(vX);
				f.setLocation(p.x + vX, p.y);
			}
			if(p.y < 0 || p.y + getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getHeight()){
				vY = -vY;
				aY = (int)Math.signum(vY);
				f.setLocation(p.x, p.y + vY);
			}
			if(tick % 2 == 1){
				f.setLocation(p.x + vX, p.y + vY);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}

	}

}
