package pixelphysics2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Main {
	static int mode = 1; //Normal:0 //Speed:1 //Angle:2
	static Particle[] particles = new Particle[Data.particleNum];
	static Random rand = new Random();
	public static void main(String[] args) {
		for (int i = 0 ;i  < Data.particleNum;i++) {
			int x = i % Data.width;
			int y = (int)Math.floor(((double)i / (double)Data.width));
			x = (int)(Math.random() * 3000);
			y = (int)(Math.random() * 1000);
//			x /= 10; y /= 10;
			particles[i] = new Particle(x + 10,y + 10,Math.random() - 0.5,Math.random() - 0.5);
			particles[i].RGB = rand.nextInt(255) << 16 | rand.nextInt(255) << 8 | rand.nextInt(255);
			particles[i].size = rand.nextInt(2)+2;
			//	int rgb = r << 16 | g << 8 | b ;
			//	int r = rgb >> 16 & 0XFF;
			//	int g = rgb >> 8 & 0XFF;
			//	int b = rgb & 0XFF;
		}
		for(int i = 0; i < 4;i++){
			new GamePanel();
		}
		while(true){
			update();
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
			}
			for(int i = 0; i < Data.particleNum;i++){
				Particle p = particles[i];
				p.lastX = p.x;
				p.lastY = p.y;
				p.x += p.vX;
				p.y += p.vY;
				p.x -= p.x / 300;
				p.y -= p.y / 300;
			}
		}
		
	}
	public static int getRGB(Particle p){
		switch(mode){
			case 0:
				return p.RGB;
			case 1:
				double v = p.getSpeed();
				int r = (int)(-0.6*((v - 35) *(v - 35))) + 240;
				if(r < 0){
					r = 0;
				}
				r += 15;
				int g = (int)-((v - 20) *(v - 20)) + 240;
				if(g < 0){
					g = 0;
				}
				g += 15;
				int b = (int)-((v - 15) *(v - 15)) + 240;
				if(b < 0){
					b = 0;
				}
				b += 15;
				return r << 16 | g << 8 | b ;
				
		}
		return 0;
	}
	public static void update(){
		if(click){
			clicks.add(MouseInfo.getPointerInfo().getLocation());
		}
		for(int i = 0; i < clicks.size();i++){
			int baseX = clicks.get(i).x;
			int baseY = clicks.get(i).y;
			for(int j = 0; j < Main.particles.length;j++){
				Particle p = Main.particles[j];
				double dist = getDistance(p.x, p.y, baseX, baseY);
				double deltaX = (p.x - baseX) / dist;
				double deltaY = (p.y - baseY) / dist;
				p.vX -= deltaX / 10;
				p.vY -= deltaY / 10;
			}
		}
		clicks.clear();
		Graphics grb = Main.bi.getGraphics();
		grb.setColor(Color.BLACK);
		for(int i = 0; i < Data.particleNum;i++){
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
			double dX = Math.cos(a) * (double)p.size / 2;
			double dY = Math.sin(a) * (double)p.size / 2;
			xA[0] = (int) (x + dX);
			yA[0] = (int) (y + dY);
			xA[1] = (int) (lastX + dX);
			yA[1] = (int) (lastY + dY);
			xA[2] = (int) (lastX - dX);
			yA[2] = (int) (lastY - dY);
			xA[3] = (int) (x - dX);
			yA[3] = (int) (y - dY);
			Polygon poly = new Polygon(xA,yA,4);
			int s = p.size;
			grb.setColor(new Color(getRGB(p)));
			grb.fillPolygon(poly);
//			grb.drawLine((int)x, (int)y, (int)lastX, (int)lastY);
			grb.fillOval((int)x - s/2, (int)y - s/2, s, s);
		}
	}
	static boolean click = false;
	static BufferedImage bi = new BufferedImage(1920,1080,BufferedImage.TYPE_3BYTE_BGR);
	static ArrayList<Point> clicks = new ArrayList<Point>();
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	
}
