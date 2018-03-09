package pixelphysics2.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pixelphysics2.main.Data;
import pixelphysics2.main.Inputerface;
import pixelphysics2.main.Particle;

public class UI implements Runnable{

	private static JFrame frame;
	public static JPanel gamePanel;
	public static boolean HUD = false;
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					UI window = new UI();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}
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
        JFrame.setDefaultLookAndFeelDecorated(true);
		HUDPanel.setLayout(new BoxLayout(HUDPanel, BoxLayout.Y_AXIS));
		for(int i = 0; i < Value.list.size();i++){
			Value v = Value.list.get(i);
			JComponent jc = v.jc;
			jc.setVisible(true);
			HUDPanel.add(jc);
		}
		while(true) {
			if((System.nanoTime() - t0)/1000000 > 15){
				t0 = System.nanoTime();
				frame.repaint();
				updateUIData();
			}
		}

	}
	public static JPanel HUDPanel = new JPanel(){};
	public static int hudSize = 200;
	public static void updateUIData(){
		for(int i = 0; i < Value.list.size();i++){
			Value v = Value.list.get(i);
			v.updateValue();
		}
	}
	public static void updateUIShape(){
		if(gamePanel != null){
			if(HUD){
				gamePanel.setBounds(hudSize,0,frame.getWidth(), frame.getHeight());
				frame.add(HUDPanel);
				HUDPanel.setBounds(0, 0, hudSize, frame.getHeight());
				gamePanel.updateUI();
			}
			else{
				gamePanel.setBounds(0,0,frame.getWidth(), frame.getHeight());
				frame.remove(HUDPanel);
				gamePanel.updateUI();
			}
		}
	}
	public static ArrayList<JComponent> MenuComponents = new ArrayList<JComponent>();
	private void initialize() {
		HUDPanel.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) {
				if(e.getSource() instanceof Component){
					((Component)e.getSource()).requestFocus();
				}
			}
			public void mouseClicked(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		HUDPanel.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					gamePanel.requestFocus();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
		frame = new JFrame();
		frame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				updateUIShape();
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		frame.setBounds(100, 100, 728, 542);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics gr) {
				long t0 = System.nanoTime();
				int xOff = (HUD) ? hudSize : 0;
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
		gamePanel.setBounds(0, 0, 484, 461);
		gamePanel.setFocusable(true);
		Inputerface i = new Inputerface();
		gamePanel.addMouseListener(i);
		frame.addKeyListener(i);
		gamePanel.addMouseWheelListener(i);
		frame.setFocusable(true);
		frame.setSize(500,500);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		gamePanel.setDoubleBuffered(true);
		gamePanel.setLayout(null);
		frame.getContentPane().add(gamePanel);
		Thread t1 = new Thread(this);
		t1.start();
	}

}
