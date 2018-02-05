package pixelphysics2.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pixelphysics2.main.Data;
import pixelphysics2.main.Inputerface;
import pixelphysics2.main.Particle;

public class UI implements Runnable{

	private static JFrame frame;
	public static JPanel p;
	public static boolean HUD = true;
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
		while(true) {
			
			if((System.nanoTime() - t0)/1000000 > 15){
				t0 = System.nanoTime();
				frame.repaint();
			}
		}

	}
	public static void addSpin(JSpinner spin){
		frame.add(spin);
		spin.setLocation(10, 20);
		spin.setSize(100, 100);
		((DefaultEditor) spin.getEditor()).getTextField().setEditable(false);
		spin.setModel(new SpinnerListModel(Data.Texture.values()));
		spin.setVisible(true);
		spin.setValue(Data.t);
		spin.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Data.t = (Data.Texture)spin.getValue();
			}
		});
	}
	public static void removeComponent(JComponent jc){
		frame.remove(jc);
	}
	public static int hudSize = 200;
	public static void updateElements(){
		if(p != null){
			if(HUD){
//				addSpin(spinTemp);
				p.setBounds(hudSize,0,frame.getWidth(), frame.getHeight());
				p.updateUI();
			}
			else{
//				frame.remove(spinTemp);
//				spinTemp.setVisible(false);
				p.setBounds(0,0,frame.getWidth(), frame.getHeight());
				p.updateUI();
			}
		}
	}
	public static ArrayList<JComponent> MenuComponents = new ArrayList<JComponent>();
//	static JSpinner spinTemp = new JSpinner();
	private void initialize() {
		frame = new JFrame();
		frame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				updateElements();
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		frame.setBounds(100, 100, 728, 542);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel(){
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
