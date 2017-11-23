package pixelphysics2;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int sleep = 10;
	long tick = 0;
	Random rand = new Random();
	
	JFrame f;
	public GamePanel() {
		Inputerface i = new Inputerface();
		addMouseListener(i);
		addKeyListener(i);
		setFocusable(true);
		f = new JFrame("GamePanel");
		f.setSize(500,500);
		f.setLocation(0, 0);
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
		long t0 = System.nanoTime();
		int xOff = this.getLocationOnScreen().x;
		int yOff = this.getLocationOnScreen().y;
		gr.drawImage(Data.bi, 0 - xOff, 0 - yOff, null);
		long t1 = System.nanoTime();
		Main.panelLag = (int) (t1 - t0) /1000000;
	}
	public void run() {
		while(true) {
			tick++;
			repaint();
		}

	}

}
