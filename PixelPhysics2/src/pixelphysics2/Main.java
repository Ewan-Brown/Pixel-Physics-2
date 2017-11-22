package pixelphysics2;

import static pixelphysics2.Data.RGB;
import static pixelphysics2.Data.RGB_switch;
import static pixelphysics2.Data.bi;
import static pixelphysics2.Data.fill;
import static pixelphysics2.Data.particleNum;
import static pixelphysics2.Data.shiftAmount;
import static pixelphysics2.Data.width;

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
import java.util.Random;

import pixelphysics2.Data.Shape;
import pixelphysics2.Data.Texture;

public class Main {
	static Particle[] particles;
	static Random rand = new Random();
	static int tick = 0;
	static Point move;
	static int mainLag;
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
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
		        getLocalGraphicsEnvironment().getDefaultScreenDevice().
		        getDefaultConfiguration();
		bi = gfx_config.createCompatibleImage(screenSize.width,screenSize.height);
		bi.setAccelerationPriority(1);
		Random rand = new Random();
//		Data.c = Texture.values()[rand.nextInt(Data.Texture.values().length)];
//		Data.s = Shape.values()[rand.nextInt(Data.Shape.values().length)];
//		Data.colorWheelMultiplier = rand.nextInt(3) + 1;
//		Data.shiftAmount = rand.nextInt(4) + 1;
//		Data.stretch = rand.nextBoolean();
//		Data.fill = rand.nextBoolean();
		//Data.particleNum = rand.nextInt(100) + 1;
		Data.particleNum = 1000;
		System.out.println(Data.s);
		Data.c = Texture.ANGLE;
		Data.fill = true;
		Data.s = Shape.BOTH;
		Data.stretch = false;
		Data.colorWheelMultiplier = 5;
		particles = new Particle[particleNum];
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
		switch(Data.c){

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
		long t0 = System.nanoTime();
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
			int v = (int)Math.ceil(p.getSpeed() * 4);
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
			//TODO Fix this flag statement with something streamline, and switch this Data.s to a switch-case thing
			if(Data.s == Shape.LINE){
				grb.drawLine((int)lastX, (int)lastY, (int)x, (int)y);
			}
			else if(fill){
				if(Data.s != Shape.CIRCLE)grb.fillPolygon(poly);
				if(Data.s != Shape.RECTANGLE)grb.fillOval((int)x - s/2, (int)y - s/2, s - 1, s - 1);
			}
			else{
				if(Data.s != Shape.CIRCLE)grb.drawPolygon(poly);
				if(Data.s != Shape.RECTANGLE)grb.drawOval((int)x - s/2, (int)y - s/2, s - 1, s - 1);
			}
		}
		long t1 = System.nanoTime();
		System.out.print((t1 - t0) / 1000000 + " ");
		System.out.println(panelLag);
	}
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

}
