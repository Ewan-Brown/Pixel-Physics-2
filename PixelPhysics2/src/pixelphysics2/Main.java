package pixelphysics2;

import static pixelphysics2.Data.RGB;
import static pixelphysics2.Data.RGB_switch;
import static pixelphysics2.Data.bi;
import static pixelphysics2.Data.fill;
import static pixelphysics2.Data.mode;
import static pixelphysics2.Data.particleNum;
import static pixelphysics2.Data.shiftAmount;
import static pixelphysics2.Data.width;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Random;
public class Main {
	static Particle[] particles = new Particle[particleNum];
	static Random rand = new Random();
	static int tick = 0;
	static Point move;
	public static void shiftColor(){
		RGB[RGB_switch] += shiftAmount;
		if(RGB[RGB_switch] > 255){
			RGB[RGB_switch] = 255;
			switchShift();
		}
		else if(RGB[RGB_switch] < 1){
			RGB[RGB_switch] = 1;
			switchShift();
		}
		else if(rand.nextInt(100) < 1)
			switchShift();
	}
	public static void switchShift(){
		RGB_switch = rand.nextInt(3);
		shiftAmount = -1;
		if(getPositive(RGB_switch))
			shiftAmount = 1;
	}
	public static boolean getPositive(int index){
		if(RGB[index] < 127)
			return true;
		return false;
	}
	public static void doMove(int x, int y, long time){
		double mills = time / 1000000L;
		double xD = (double)x / (mills);
		double yD = (double)y / (mills);
		for(int i = 0; i < particleNum; i++){
			particles[i].vX += xD;
			particles[i].vY += yD;
		}
	}
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl","True");
		for (int i = 0 ;i  < particleNum;i++) {
			int x = i % width;
			int y = (int)Math.floor(((double)i / (double)width));
			x = (int)(Math.random() * 2000);
			y = (int)(Math.random() * 1000);
			particles[i] = new Particle(x + 10,y + 10,Math.random() - 0.5,Math.random() - 0.5);
			particles[i].RGB = rand.nextInt(255) << 16 | rand.nextInt(255) << 8 | rand.nextInt(255);
			particles[i].size = rand.nextInt(10)+10;
			//FIXME Particles must be 4 size or bigger or else DrawOval() draws a square
			//	int rgb = r << 16 | g << 8 | b ;
			//	int r = rgb >> 16 & 0XFF;
			//	int g = rgb >> 8 & 0XFF;
			//	int b = rgb & 0XFF;
		}
		for(int i = 0; i < 1;i++){
			new GamePanel();
		}
		while(true){
			tick++;
			update();
			Inputerface.updateKeys();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			for(int i = 0; i < particleNum;i++){
				Particle p = particles[i];
				p.lastX = p.x;
				p.lastY = p.y;
				p.x += p.vX * 4;
				p.y += p.vY * 4;
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
			}
		}

	}
	public static int getRGB(Particle p){
		switch(mode){
		case 0:
			return p.RGB;
		case 1:
			double v = p.getSpeed();
			int r = (v > 10) ? 240 : (int)(-5*((v - 10) *(v - 10))) + 240;
			if(r < 0){
				r = 0;
			}
			r += 15;
			int g = (int)-(9*(v - 7) *(v - 7)) + 240;
			if(g < 0){
				g = 0;
			}
			g += 15;
			int b = (int)-(5*(v - 3) *(v - 3)) + 240;
			if(b < 0){
				b = 0;
			}
			b += 15;
			return r << 16 | g << 8 | b ;
		case 2:
			return RGB[0] << 16 | RGB[1] << 8 | RGB[2] ;

		}
		return 0;
	}
	public static void update(){
		shiftColor();
		if(Inputerface.rightClick){
			Inputerface.rightClickList.add(MouseInfo.getPointerInfo().getLocation());
		}
		for(int i = 0; i < Inputerface.rightClickList.size();i++){
			int baseX = Inputerface.rightClickList.get(i).x;
			int baseY = Inputerface.rightClickList.get(i).y;
			for(int j = 0; j < Main.particles.length;j++){
				Particle p = Main.particles[j];
				double dist = getDistance(p.x, p.y, baseX, baseY);
				double deltaX = (p.x - baseX) / dist;
				double deltaY = (p.y - baseY) / dist;
				p.vX -= deltaX / 10;
				p.vY -= deltaY / 10;
			}
		}
		Inputerface.rightClickList.clear();
		Graphics2D grb = (Graphics2D)bi.getGraphics();
		grb.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON); 
		grb.setColor(Color.BLACK);
		for(int i = 0; i < particleNum;i++){
			Particle p = Main.particles[i];
			double x = p.x;
			double y = p.y;
			double lastX = p.lastX;
			double lastY = p.lastY;
			double m = (lastY - y)/(lastX - x);
			double m2 = -1/m;
			int[] xA = new int[4];
			int[] yA = new int[4];
			double a = Math.atan(m2);
			int s = p.size;
			int v = (int)Math.ceil(p.getSpeed() * 3);
			if(Data.stretch){
				s = v;
			}
			double dX = Math.cos(a) * (double)s / 2;
			double dY = Math.sin(a) * (double)s / 2;
			xA[0] = (int) (x + dX);
			yA[0] = (int) (y + dY);
			xA[1] = (int) (lastX + dX);
			yA[1] = (int) (lastY + dY);
			xA[2] = (int) (lastX - dX);
			yA[2] = (int) (lastY - dY);
			xA[3] = (int) (x - dX);
			yA[3] = (int) (y - dY);
			Polygon poly = new Polygon(xA,yA,4);
			grb.setColor(new Color(getRGB(p)));
			if(fill){
				grb.fillPolygon(poly);
				grb.fillOval((int)x - s/2, (int)y - s/2, s - 1, s - 1);
			}
			else{
				grb.drawPolygon(poly);
				grb.drawOval((int)x - s/2, (int)y - s/2, s, s);
			}
		}
	}
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

}
