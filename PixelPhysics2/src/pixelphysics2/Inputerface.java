package pixelphysics2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Inputerface implements MouseListener, KeyListener{

	public static boolean[] keySet = new boolean[256];
//	static ArrayList<Point> rightClickList = new ArrayList<Point>();
	static boolean rightClick = false;
	static Point lastLeftClickPress = null;
	static Long lastLeftClickTime = null;
	static int[] cooldowns = new int[256];
	static int keyCooldown = 30;
	public static void updateKeys(){
		for(int i = 0; i < cooldowns.length;i++){
			cooldowns[i]--;
		}
		if(isKeyFresh(KeyEvent.VK_SPACE)){
			cooldowns[KeyEvent.VK_SPACE] = 2;
			Data.bi.getGraphics().clearRect(0, 0, 10000, 10000);
		}
		if(isKeyFresh(KeyEvent.VK_E)){
			cooldowns[KeyEvent.VK_E] = keyCooldown;
			Main.randomize();
		}
		if(isKeyFresh(KeyEvent.VK_R)){
			cooldowns[KeyEvent.VK_R] = keyCooldown;
			Main.resetParticles();
		}
		if(isKeyFresh(KeyEvent.VK_P)){
			cooldowns[KeyEvent.VK_P] = keyCooldown;
			Data.paint = !Data.paint;
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
			Main.flick(move.x, move.y, time);
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
