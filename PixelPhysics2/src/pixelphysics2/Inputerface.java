package pixelphysics2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Inputerface implements MouseListener, KeyListener{

	public static boolean[] keySet = new boolean[256];
	static ArrayList<Point> rightClickList = new ArrayList<Point>();
	static boolean rightClick = false;
	static Point lastLeftClickPress = null;
	static Long lastLeftClickTime = null;
	static int[] cooldowns = new int[256];
	static int keyCooldown = 300;
	public static void updateKeys(){
		for(int i = 0; i < cooldowns.length;i++){
			cooldowns[i]--;
		}
		if(isKeyFresh(KeyEvent.VK_S)){
			Data.stretch = !Data.stretch;
		}
		if(isKeyFresh(KeyEvent.VK_F)){
			Data.fill = !Data.fill;
		}
	}
	public static boolean isKeyFresh(int key){
		return keySet[key] && cooldowns[key] < 0;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			Inputerface.rightClick = true;
		}
		else{
			lastLeftClickPress = new Point(e.getLocationOnScreen());
		}
		lastLeftClickTime = System.nanoTime();
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			Inputerface.rightClick = false;
		}
		else{
			Point local = e.getLocationOnScreen();
			Point move = new Point(local.x - lastLeftClickPress.x,local.y - lastLeftClickPress.y);
			long time = System.nanoTime() - lastLeftClickTime;
			Main.doMove(move.x, move.y, time);
		}
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
	public void keyTyped(KeyEvent e) {
		
	}	

}
