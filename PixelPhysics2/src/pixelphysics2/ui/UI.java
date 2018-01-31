package pixelphysics2.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pixelphysics2.main.Data;
import pixelphysics2.main.Inputerface;
import pixelphysics2.main.Particle;

public class UI implements Runnable{

	private static JFrame frame;
	public static JPanel p;
	public boolean HUD = true;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public UI() {
		initialize();
	}
	public boolean isInside(Particle p){
		return false;
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
				frame.repaint();
			}
		}

	}
	public void updateElements(){
		if(p != null){
			p.setBounds(0,0,frame.getWidth(), frame.getHeight());
			p.updateUI();
		}
	}
	private void initialize() {
		frame = new JFrame();
		frame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				updateElements();
			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});
		frame.setBounds(100, 100, 728, 542);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics gr) {
				long t0 = System.nanoTime();
				//				int xOff = this.getLocationOnScreen().x;
				//				int yOff = this.getLocationOnScreen().y;
				int xOff = 0;
				int yOff = 0;
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
						//						+ "Q hold to really fuck shit up\n"
						+ "Q hold to throw particles\n"
						+ "R to reset particles\n"
						+ "P to toggle persistant paint\n"
						+ "S to toggle mouse stretching\n"
						+ "D to toggle speed stretching\n"
						+ "Space to erase paint\n"			
						+ "Scroll up/down to change PULL force\n"			
						+ "Scroll up/down AND hold shift to change friction force\n"			
						//						+ "ugggly menu - by Ewan Brown\n"		
						//						+ "\n\n\n\n\n\n\n\n\n\n\n"			
						//						+ "OOoh also move the window around\n"			
						, 10, 20);
			}
		};
		p.setBounds(0, 0, 484, 461);
		Inputerface i = new Inputerface();
		frame.addMouseListener(i);
		frame.addKeyListener(i);
		frame.addMouseWheelListener(i);
		frame.setFocusable(true);
		frame.setSize(500,500);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		p.setDoubleBuffered(true);
		p.setLayout(null);
		frame.getContentPane().add(p);
		Thread t1 = new Thread(this);
		t1.start();
	}

}
