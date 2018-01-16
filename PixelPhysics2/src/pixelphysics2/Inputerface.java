package pixelphysics2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Inputerface implements MouseListener, KeyListener, MouseWheelListener{
	public static boolean[] keySet = new boolean[256];
	//	static ArrayList<Point> rightClickList = new ArrayList<Point>();
	static boolean rightClick = false;
	static Point lastLeftClickPress = null;
	static Long lastLeftClickTime = null;
	static int[] cooldowns = new int[256];
	static int keyCooldown = 10;
	public static void updateKeys(){
		for(int i = 0; i < cooldowns.length;i++){
			cooldowns[i]--;
		}
		if(isKeyFresh(KeyEvent.VK_SPACE)){
			cooldowns[KeyEvent.VK_SPACE] = 2;
			Graphics g = Data.vImage.getGraphics();
			g.setColor(new Color(Main.rand.nextInt(255),Main.rand.nextInt(255),Main.rand.nextInt(255)));
			//			g.setColor(Color.WHITE);
			g.fillRect(0, 0, Data.vImage.getWidth(), Data.vImage.getHeight());
		}
		if(isKeyFresh(KeyEvent.VK_E)){
			cooldowns[KeyEvent.VK_E] = keyCooldown;
			Main.randomize();
		}
		if(isKeyFresh(KeyEvent.VK_X)){
			cooldowns[KeyEvent.VK_X] = keyCooldown;
			Data.pm = ParticleMesser.map.get(ParticleMesser.map.keySet().toArray()[Main.rand.nextInt(ParticleMesser.map.size())]);
		}
		if(isKeyFresh(KeyEvent.VK_R)){
			cooldowns[KeyEvent.VK_R] = keyCooldown;
			Main.resetParticles();
		}
		if(isKeyFresh(KeyEvent.VK_P)){
			cooldowns[KeyEvent.VK_P] = keyCooldown;
			Data.paint = !Data.paint;
			//			Main.resetBackground();
		}
		if(isKeyFresh(KeyEvent.VK_S)){
			cooldowns[KeyEvent.VK_S] = keyCooldown;
			Data.mouseStretch = !Data.mouseStretch;
		}
		if(isKeyFresh(KeyEvent.VK_D)){
			cooldowns[KeyEvent.VK_D] = keyCooldown;
			Data.speedStretch = !Data.speedStretch;
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
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(keySet[KeyEvent.VK_SHIFT]){
			Data.frictionMult -= Data.frictionMult * (double)e.getWheelRotation() * -1 / 20D;
			if(Data.frictionMult < 0.001){
				Data.frictionMult = 0.001;
			}
		}
		else{
			Data.forceMult += (double)e.getWheelRotation() * -1 / 20D;
		}
	}	
}
