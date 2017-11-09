package pixelphysics2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.BitSet;

public class Inputerface implements MouseListener, KeyListener{

	public static boolean[] keySet = new boolean[256];
	static ArrayList<Point> clicks = new ArrayList<Point>();
	static boolean click = false;
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		Inputerface.click = true;
	}
	public void mouseReleased(MouseEvent e) {
		Inputerface.click = false;
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keySet[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keySet[e.getKeyCode()] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}	
	
}
