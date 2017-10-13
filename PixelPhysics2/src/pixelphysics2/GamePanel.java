package pixelphysics2;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	int sleep = 10;
	public GamePanel() {
		Thread t1 = new Thread(this);
		t1.start();
	}
	
	public void paint (Graphics gr) {
		
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

	
	
}
