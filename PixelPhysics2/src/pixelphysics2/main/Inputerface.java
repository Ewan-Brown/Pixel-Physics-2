package pixelphysics2.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pixelphysics2.main.Data.Shape;
import pixelphysics2.main.Data.Texture;
import pixelphysics2.ui.OpenGL;
import pixelphysics2.ui.UI;

public class Inputerface implements MouseListener, KeyListener, MouseWheelListener{
	public static boolean[] keySet = new boolean[65536];
	//	static ArrayList<Point> rightClickList = new ArrayList<Point>();
	public static boolean RMB = false;
	public static Point lastLeftClickPress = null;
	public static Long lastLeftClickTime = null;
	public static int[] cooldowns = new int[256];
	public static int keyCooldown = 10;
	public static void updateKeys(){
		for(int i = 0; i < cooldowns.length;i++){
			cooldowns[i]--;
		}
		if (Inputerface.keySet[KeyEvent.VK_Q]){
			Point p = Inputerface.getMouseLocation();
			Main.flick(Data.lastMouse.x, Data.lastMouse.y, p.x, p.y, System.nanoTime() - Data.lastTime);
		}
		Data.lastTime = System.nanoTime();
		Data.lastMouse = Inputerface.getMouseLocation();
		if(isKeyFresh(KeyEvent.VK_SPACE)){
			cooldowns[KeyEvent.VK_SPACE] = 2;
			Graphics g = Data.vImage.getGraphics();
			g.setColor(new Color(Data.rand.nextInt(255),Data.rand.nextInt(255),Data.rand.nextInt(255)));
			//			g.setColor(Color.WHITE);
			g.fillRect(0, 0, Data.vImage.getWidth(), Data.vImage.getHeight());
		}
		if(isKeyFresh(KeyEvent.VK_E)){
			cooldowns[KeyEvent.VK_E] = keyCooldown;
			Main.randomize();
		}
		if(isKeyFresh(KeyEvent.VK_X)){
			cooldowns[KeyEvent.VK_X] = keyCooldown;
			Data.pm = ParticleMesser.map.get(ParticleMesser.map.keySet().toArray()[Data.rand.nextInt(ParticleMesser.map.size())]);
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
		if(isKeyFresh(KeyEvent.VK_T)){
			cooldowns[KeyEvent.VK_T] = keyCooldown;
			Data.t = Texture.values()[(Data.t.ordinal() + 1) % Texture.values().length];
		}
		if(isKeyFresh(KeyEvent.VK_G)){
			cooldowns[KeyEvent.VK_G] = keyCooldown;
			Data.s = Shape.values()[(Data.s.ordinal() + 1) % Shape.values().length];
		}
		if(isKeyFresh(KeyEvent.VK_F)){
			cooldowns[KeyEvent.VK_F] = keyCooldown;
			Data.frictionMult = 1;
			Data.forceMult = 1;
		}
		if(isKeyFresh(KeyEvent.VK_O)){
			cooldowns[KeyEvent.VK_O] = keyCooldown;
			Data.fill = !Data.fill;
		}
		if(isKeyFresh(KeyEvent.VK_1)){
			cooldowns[KeyEvent.VK_1] = keyCooldown;
			UI.HUD = !UI.HUD;
			//			UI.updateUIShape(); //XXX Removed HUD Temporary
		}
	}
	public static boolean isKeyFresh(int key){
		return keySet[key] && cooldowns[key] < 0;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			Inputerface.RMB = true;
		}
		else{
			lastLeftClickPress = getMouseLocation();
			lastLeftClickTime = System.nanoTime();
		}
	}
	public static Point getMouseLocation(){
		Point p = null;
		if(Main.LWJGL){
			double[] components = OpenGL.getCursorPos();
			return new Point((int)components[0] , (int)components[1]);
		}else{
			p = MouseInfo.getPointerInfo().getLocation();
			if(UI.gamePanel != null){
				p.move(p.x - UI.gamePanel.getParent().getLocationOnScreen().x, p.y - UI.gamePanel.getParent().getLocationOnScreen().y);
			}
		}
		return p;
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			Inputerface.RMB = false;
		}
		else{
			Point local = getMouseLocation();
			long time = System.nanoTime() - lastLeftClickTime;
			Main.flick(lastLeftClickPress.x,lastLeftClickPress.y,local.x, local.y, time);
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
