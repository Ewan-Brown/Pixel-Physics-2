package pixelphysics2;

import java.awt.Color;
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
		addMouseWheelListener(i);
		setFocusable(true);
		f = new JFrame("GamePanel");
		f.setSize(500,500);
		f.setLocation(0, 0);
		f.add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		this.setDoubleBuffered(true);
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
		gr.drawImage(Data.vImage, 0 - xOff, 0 - yOff, null);
		long t1 = System.nanoTime();
		Data.panelLag = (int) (t1 - t0) /1000000;
		gr.setColor(Color.RED);
		drawString(gr,"Left Click-Drag-Release to throw particles\n"
				+ "Right Click-Hold to pull particles\n"
				+ "E to completely randomize\n"
				+ "T to change color algorithm\n"
				+ "G to change shape algorithm\n"
				+ "X to randomize physics algorithm - sketchy\n"
				+ "Q hold to really fuck shit up\n"
				+ "R to reset particles\n"
				+ "P to toggle persistant paint\n"
				+ "S to toggle mouse stretching\n"
				+ "D to toggle speed stretching\n"
				+ "Space to erase paint\n"			
				+ "Scroll up/down to change PULL force\n"			
				+ "Scroll up/down AND hold shift to change friction force\n"			
				+ "ugggly menu - by Ewan Brown\n"		
				+ "\n\n\n\n\n\n\n\n\n\n\n"			
				+ "OOoh also move the window around\n"			
				, 10, 20);
	}
	void drawString(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}
	public void run() {
		long t0 = System.nanoTime();
		while(true) {
			if((System.nanoTime() - t0)/1000000 > 15){
				t0 = System.nanoTime();
				tick++;
				repaint();
			}
		}

	}

}
