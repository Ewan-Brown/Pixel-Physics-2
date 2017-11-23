package pixelphysics2;

import static pixelphysics2.Data.RGB;
import static pixelphysics2.Data.RGB_switch;
import static pixelphysics2.Data.bi;
import static pixelphysics2.Data.fill;
import static pixelphysics2.Data.particleNum;
import static pixelphysics2.Data.shiftAmount;
import static pixelphysics2.Data.ParticleRangeWidth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Random;

import pixelphysics2.Data.Shape;
import pixelphysics2.Data.Texture;

public class Main {
	static Particle[] particles;
	static Random rand = new Random();
	static int tick = 0;
	static Point move;
	static int mainLag = 0;
	static int panelLag = 0;
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
	public static void flick(int x, int y, long time){
		double mills = time / 1000000L;
		double xD = (double)x / (mills);
		double yD = (double)y / (mills);
		for(int i = 0; i < particleNum; i++){
			particles[i].vX += xD;
			particles[i].vY += yD;
		}
	}
	public static void main(String[] args) { 
		// Cuts <10% lag?!
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		bi = gc.createCompatibleImage(screenSize.width,screenSize.height);
		bi.setAccelerationPriority(1);
		VolatileImage retVal = gc.createCompatibleVolatileImage(bi.getWidth(), bi.getHeight());
		Random rand = new Random();
		Data.BackWidth = bi.getWidth();
		Data.BackHeight = bi.getHeight();
		Data.t = Texture.values()[rand.nextInt(Data.Texture.values().length)];
		Data.s = Shape.values()[rand.nextInt(Data.Shape.values().length)];
		Data.colorWheelMultiplier = rand.nextInt(3) + 1;
		Data.colorWheelFlip = rand.nextBoolean();
		Data.shiftAmount = rand.nextInt(4) + 1;
		Data.stretch = rand.nextBoolean();
		Data.fill = rand.nextBoolean();
		Data.particleNum = rand.nextInt(700) + 1;
		
		Data.t = Texture.MOUSE_LOCATION;
		Data.s = Shape.BOTH;
		Data.colorWheelMultiplier = 5;
		Data.colorWheelFlip = true;
		Data.stretch = false;
		Data.fill = true;
		Data.particleNum = 1000;
		particles = new Particle[particleNum];
		System.setProperty("sun.java2d.opengl","True");
		for (int i = 0 ;i  < particleNum;i++) {
			int x = (int)(Math.random() * 2000);
			int y = (int)(Math.random() * 1000);
			particles[i] = new Particle(x + 10,y + 10,Math.random() - 0.5,Math.random() - 0.5);
			particles[i].RGB = rand.nextInt(255) << 16 | rand.nextInt(255) << 8 | rand.nextInt(255);
			particles[i].size = rand.nextInt(10)+10;
			//FIXME Particles must be 4 size or bigger or else DrawOval() draws a square
			//	int rgb = r << 16 | g << 8 | b ;
			//	int r = rgb >> 16 & 0XFF;
			//	int g = rgb >> 8 & 0XFF;
			//	int b = rgb & 0XFF;
		}
		new GamePanel();
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
		int r = 0;
		int g = 0;
		int b = 0;
		switch(Data.t){

		case INDIVIDUAL:
			return p.RGB;
		case SPEED:
			double v = p.getSpeed();
			r = (v > 10) ? 240 : (int)(-5*((v - 10) *(v - 10))) + 240;
			if(r < 0){
				r = 0;
			}
			r += 15;
			g = (int)-(9*(v - 7) *(v - 7)) + 240;
			if(g < 0){
				g = 0;
			}
			g += 15;
			b = (int)-(5*(v - 3) *(v - 3)) + 240;
			if(b < 0){
				b = 0;
			}
			b += 15;
			return r << 16 | g << 8 | b ;
		case CLASSIC:
			return RGB[0] << 16 | RGB[1] << 8 | RGB[2] ;
		case ANGLE:
			double a = p.getAngle();
			double deg = a * Data.colorWheelMultiplier;
			r = (int)Math.floor((Math.cos(deg) * 255));
			g = (int)Math.floor((Math.cos(deg + 2.0944) * 255));
			b = (int)Math.floor((Math.cos(deg + 4.18879) * 255));
			if(Data.colorWheelFlip){
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
			}
			if(r < 0){
				r = 0;
			}
			if(g < 0){
				g = 0;
			}
			if(b < 0){
				b = 0;
			}
			return r << 16 | g << 8 | b ;

		case MOUSE_DISTANCE:
			double d = p.lastMouseDist / Data.colorWheelMultiplier;
			r = (int) Math.floor((-0.01*d*d+255));
			g = (int) Math.floor((-0.005*(d-200)*(d-200)+255));
			b = (int) Math.floor((-0.0025*(d-600)*(d-600)+255));
			if(Data.colorWheelFlip){
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
			}
			if(r < 0){
				r = 0;
			}
			if(g < 0){
				g = 0;
			}
			if(b < 0){
				b = 0;
			}
			return r << 16 | g << 8 | b ;
		case MOUSE_LOCATION:
			int x = MouseInfo.getPointerInfo().getLocation().x;
			int y = MouseInfo.getPointerInfo().getLocation().y;
			double w = (double)x / (double)Data.BackWidth;
			double h = (double)y / (double)Data.BackHeight;
			r = (int) Math.floor(w * 255);
			g = (int) Math.floor(h * 255);
			b = (int) Math.floor((1-h) * (1-w) * 255);
			if(r < 0){
				r = 0;
			}
			if(g < 0){
				g = 0;
			}
			if(b < 0){
				b = 0;
			}
			return r << 16 | g << 8 | b ;
		default:
			System.err.println("TEXTURE VALUE NOT RECOGNIZED: " + Data.t);
			return 0;
		}
	}

	public static void update(){
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		VolatileImage retVal = gc.createCompatibleVolatileImage(Data.bi.getWidth(), Data.bi.getHeight());
		shiftColor();
		for(int j = 0; j < Main.particles.length;j++){
			Particle p = Main.particles[j];
			int baseX = MouseInfo.getPointerInfo().getLocation().x;
			int baseY = MouseInfo.getPointerInfo().getLocation().y;
			double dist = getDistance(p.x, p.y, baseX, baseY);
			p.lastMouseDist = dist;
			if(Inputerface.rightClick){
				double deltaX = (p.x - baseX) / dist;
				double deltaY = (p.y - baseY) / dist;
				p.vX -= deltaX / 10;
				p.vY -= deltaY / 10;

			}

		}

		long t0 = System.nanoTime();
		Graphics2D g2v = (Graphics2D)retVal.createGraphics();
		Graphics2D g2b = (Graphics2D)bi.getGraphics();
		g2v.drawImage(Data.bi, 0, 0, null);
		g2b.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF); 
		g2b.setColor(Color.BLACK);
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
			if(Data.stretch){
				s = (int)Math.ceil(p.getSpeed() * 4);
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
			g2v.setColor(new Color(getRGB(p)));
			//TODO Fix this flag statement with something streamline, and switch this Data.s to a switch-case thing
			if(Data.s == Shape.LINE){
				g2v.drawLine((int)lastX, (int)lastY, (int)x, (int)y);
			}
			else if(fill){
				if(Data.s != Shape.CIRCLE)g2v.fillPolygon(poly);
				if(Data.s != Shape.RECTANGLE)g2v.fillOval((int)x - s/2, (int)y - s/2, s - 1, s - 1);
			}
			else{
				if(Data.s != Shape.CIRCLE)g2v.drawPolygon(poly);
				if(Data.s != Shape.RECTANGLE)g2v.drawOval((int)x - s/2, (int)y - s/2, s - 1, s - 1);
			}
		}
		g2b.drawImage(retVal, 0, 0, null);
		long t1 = System.nanoTime();
		mainLag = (int) ((t1 - t0) / 1000000);
		//		System.out.print(mainLag + " ");
		//		System.out.println(panelLag);
	}
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

}
