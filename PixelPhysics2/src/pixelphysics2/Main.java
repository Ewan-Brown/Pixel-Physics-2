package pixelphysics2;

import static pixelphysics2.Data.RGB;
import static pixelphysics2.Data.RGB_switch;
import static pixelphysics2.Data.fill;
import static pixelphysics2.Data.particleNum;
import static pixelphysics2.Data.rand;
import static pixelphysics2.Data.shiftAmount;
import static pixelphysics2.Data.vImage;

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
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import pixelphysics2.Data.Shape;
import pixelphysics2.Data.Texture;

public class Main {
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
	public static void flick(int x1, int y1, int x2, int y2, long time){
		float mills = time / 1000000L;
		float xD = (float)(x2-x1);
		float yD = (float)(y2-y1);
		float minX = Math.min(x1, x2);
		float maxX = Math.max(x1, x2);
		float minY = Math.min(y1, y2);
		float maxY = Math.max(y1, y2);
		float m = yD/xD;
		if(m < 0){
			float f = minY;
			minY = maxY;
			maxY = f;
		}
		float b = y1 - m*x1;
		float mP = -1/m;
		for(int i = 0; i < particleNum; i++){
			float xA = (float)Data.particles[i].x;
			float yA = (float)Data.particles[i].y;
			float bP = yA - mP * xA;
			float xI = -(b - bP) / (m - mP);
			float yI = (xI * mP) + bP;
			if(xI > minX && xI < maxX){
				float d = pDistance(xA,yA,x1,y1,x2,y2);
				Data.particles[i].vX += xD / d / mills * 10;
				Data.particles[i].vY += yD / d / mills * 10;
			}
			if(xI > maxX){
				float d = (float) getDistance(xA, yA, maxX, maxY);
				Data.particles[i].vX += xD / d / mills * 10;
				Data.particles[i].vY += yD / d / mills * 10;
			}
			if(xI < minX){
				float d = (float) getDistance(xA, yA, minX, minY);
				Data.particles[i].vX += xD / d / mills * 10;
				Data.particles[i].vY += yD / d / mills * 10;
			}

		}
	}
	public static float pDistance(float x, float y, float x1, float y1, float x2, float y2) {

		float A = x - x1; // position of point rel one end of line
		float B = y - y1;
		float C = x2 - x1; // vector along line
		float D = y2 - y1;
		float E = -D; // orthogonal vector
		float F = C;

		float dot = A * E + B * F;
		float len_sq = E * E + F * F;

		return (float) (Math.abs(dot) / Math.sqrt(len_sq));
	}
	static ArrayList<Integer> lowPerfModes = new ArrayList<Integer>();
	public static void randomize(){
		Data.t = Texture.values()[rand.nextInt(Data.Texture.values().length)];
		Data.s = Shape.values()[rand.nextInt(Data.Shape.values().length)];
		if(Data.lowPerformance){
			int z = lowPerfModes.get(rand.nextInt(lowPerfModes.size()));
			Data.s = Shape.values()[z];
		}
		//		Data.s = Shape.TRIANGLES;
		//		Data.t = Texture.CLASSIC;
		Data.colorWheelMultiplier = rand.nextInt(3) + 1;
		Data.colorWheelFlip = rand.nextBoolean();
		Data.shiftAmount = rand.nextInt(6) + 1;
		Data.mouseStretch = rand.nextBoolean();
		Data.fill = rand.nextBoolean();
		Data.speedStretch = rand.nextBoolean();
		//		Data.t = Texture.INDIVIDUAL;
		//		Data.s = Shape.LINE;
		//		Data.colorWheelMultiplier = 1;
		//		Data.colorWheelFlip = false;
		//		Data.shiftAmount = 1;
		//		Data.stretch = false;
		//		Data.fill = true;
	}
	public static void resetParticles(){
		Data.particleNum = rand.nextInt(Data.maxParticleNum) + 1;
		Data.particles = new Particle[particleNum];
		for (int i = 0 ;i  < particleNum;i++) {
			int x = (int)(Math.random() * 2000);
			int y = (int)(Math.random() * 1000);
			Data.particles[i] = new Particle(x + 10,y + 10,Math.random() - 0.5,Math.random() - 0.5);
			Data.particles[i].RGB = rand.nextInt(255) << 24 | rand.nextInt(255) << 16 | rand.nextInt(255) << 8 | rand.nextInt(255);
			Data.particles[i].size = rand.nextInt(10)+10;
			if(i > 0){
				Data.particles[i].prev = Data.particles[i-1];
				Data.particles[i-1].next = Data.particles[i];
			}
		}
	}
	public static void main(String[] args) { 
		// Cuts <10% lag?!
		//		lowPerfModes.add(Shape.DOT.ordinal());
		lowPerfModes.add(Shape.LINE.ordinal());
		lowPerfModes.add(Shape.CONNECTMOUSE.ordinal());
		lowPerfModes.add(Shape.CONNECTPIXEL.ordinal());
		lowPerfModes.add(Shape.TRIANGLES.ordinal());
		ParticleMesser.init();
		Data.pm = ParticleMesser.map.get("Normal");
		InitView v = new InitView();
		while(!v.finished){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Data.maxParticleNum = v.slider.getValue();
		Data.lowPerformance = v.tglPerformance.isSelected();
		System.setProperty("sun.java2d.opengl","True");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		vImage = gc.createCompatibleVolatileImage(screenSize.width,screenSize.height);
		vImage.setAccelerationPriority(1);
		randomize();
		//		Data.maxParticleNum = 2;
		resetParticles();
		Data.BackWidth = vImage.getWidth();
		Data.BackHeight = vImage.getHeight();
		v.frame.dispose();
		Data.lastTime = System.nanoTime();
		Data.lastMouse = MouseInfo.getPointerInfo().getLocation();
		new GamePanel();
		long t0 = System.nanoTime();
		while(true){
			if((System.nanoTime() - t0)/1000000 > 15){
				t0 = System.nanoTime();
				update();
				Inputerface.updateKeys();
				for(int i = 0; i < particleNum;i++){
					Particle p = Data.particles[i];
					Data.pm.doParticle(p, Data.tick);
				}
			}
		}

	}
	public static int getRGB(Particle p){
		int r = 0;
		int g = 0;
		int b = 0;
		switch(Data.t){
		case INDIVIDUAL:
			r = (p.RGB >> 16) & 0xFF;
			g = (p.RGB >> 8) & 0xFF;
			b = (p.RGB) & 0xFF;
			break;
		case SPEED:
			double v = p.getSpeed();
			r = (v > 20) ? 240 : (int)(-4*((v - 18) *(v - 18))) + 240;
			if(r < 0){
				r = 0;
			}
			r += 15;
			g = (int)-(9*(v - 10) *(v - 10)) + 240;
			if(g < 0){
				g = 0;
			}
			g += 15;
			b = (int)-(4*(v - 6) *(v - 6)) + 240;
			if(b < 0){
				b = 0;
			}
			b += 15;
			break;
		case CLASSIC:
			r = (RGB[0] >> 16) & 0xFF;
			g = (RGB[1] >> 8) & 0xFF;
			b = (RGB[2]) & 0xFF;
			break; 
		case ANGLE:
			double a = p.getAngle();
			int RGB = getAngleRGB(a);
			r = (RGB >> 16) & 0xFF;
			g = (RGB >> 8) & 0xFF;
			b = (RGB) & 0xFF;
			break;
		case MOUSE_DISTANCE:
			double d = p.lastMouseDist / Data.colorWheelMultiplier;
			r = (int) Math.floor((-0.01*d*d+255));
			g = (int) Math.floor((-0.005*(d-200)*(d-200)+255));
			b = (int) Math.floor((-0.0025*(d-600)*(d-600)+255));
			if(r < 0){
				r = 0;
			}
			if(g < 0){
				g = 0;
			}
			if(b < 0){
				b = 0;
			}
			break;
		case MOUSE_LOCATION:
			r = (Data.lastMouseRGB >> 16) & 0xFF;
			g = (Data.lastMouseRGB >> 8) & 0xFF;
			b = (Data.lastMouseRGB) & 0xFF;
			break;
		case MOUSE_ANGLE:
			int RGB0 = getAngleRGB(p.lastMouseAngle);
			r = (RGB0 >> 16) & 0xFF;
			g = (RGB0 >> 8) & 0xFF;
			b = (RGB0) & 0xFF;
			break;
		case PARTICLE_LOCATION:
			r = ((int)p.x % (Data.RGB[0] + 1)) & 0xFF;
			g = ((int)p.y % (Data.RGB[1]+1)) & 0xFF;
			b = ((int)(p.x + p.y) % (Data.RGB[2]+1)) & 0xFF;
			break;
		default:
			System.err.println("TEXTURE VALUE NOT RECOGNIZED: " + Data.t);
		}
		if(Data.colorWheelFlip){
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;
		}
		return r << 16 | g << 8 | b;
	}
	public static int getAngleRGB(double a){
		float deg = (float)Math.toDegrees((a * Data.colorWheelMultiplier));
		//Cuts from 35-29 fps with 50000p
		int r = (int)Math.floor((FastMath.cos(deg) * 255));
		int g = (int)Math.floor((FastMath.cos(deg + 120f) * 255));
		int b = (int)Math.floor((FastMath.cos(deg + 240f) * 255));
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
	public static void update(){
		Data.tick++;
		
		double w = (double)Data.lastMouse.x / (double)Data.BackWidth;
		double h = (double)Data.lastMouse.y / (double)Data.BackHeight;
		int r = (int) (w * 255);
		int g = (int) (h * 255);
		int b = (int) ((1-h) * (1-w) * 255);
		if(r < 0){
			r = 0;
		}
		if(g < 0){
			g = 0;
		}
		if(b < 0){
			b = 0;
		}
		Data.lastMouseRGB = r << 16 | g << 8 | b ;
		doPulls();
		GraphicsConfiguration gc = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		VolatileImage retVal = gc.createCompatibleVolatileImage(Data.vImage.getWidth(), Data.vImage.getHeight());
		shiftColor();
		Graphics2D g2v = (Graphics2D)retVal.createGraphics();
		Graphics2D g2b = (Graphics2D)vImage.getGraphics();
		doBackGroundDraw(g2v);
		doDraw(g2v, g2b);
		doDataDraw(g2b,retVal);
		//This flush is needed or else huge lag happens
		retVal.flush();
	}
	public static void doBackGroundDraw(Graphics2D g2v){
		if(Data.paint){
			g2v.drawImage(Data.vImage, 0, 0, null);
		}
		else{
			g2v.setColor(Color.BLACK);
			g2v.fillRect(0, 0, vImage.getWidth(), vImage.getHeight());
		}
	}
	public static void doDataDraw(Graphics2D g2b, VolatileImage retVal){
		g2b.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF); 
		g2b.setColor(Color.BLACK);

		g2b.drawImage(retVal, 0, 0, null);
	}
	public static void doPulls(){
		for(int j = 0; j < Data.particles.length;j++){
			Particle p = Data.particles[j];
			int baseX = Data.lastMouse.x;
			int baseY = Data.lastMouse.y;
			double dist = getDistance(p.x, p.y, baseX, baseY);
			double angle = 0;
			p.lastMouseDist = dist;
			p.lastMouseAngle = angle;
			p.lastMouseAngle = Math.atan2(-(p.x - baseX) , p.y - baseY);
			if(Inputerface.rightClick){
				double deltaX = (p.x - baseX) / dist;
				double deltaY = (p.y - baseY) / dist;
				p.vX -= deltaX*Data.forceMult / 5;
				p.vY -= deltaY*Data.forceMult / 5;
			}

		}
	}
	public static Polygon getPoly(double x, double y, double lastX, double lastY,double a, int s){
		double dX = Math.cos(a) * (double)s / 2;
		double dY = Math.sin(a) * (double)s / 2;
		int[] xA = new int[4];
		int[] yA = new int[4];
		xA[0] = (int) (x + dX);
		yA[0] = (int) (y + dY);
		xA[1] = (int) (lastX + dX);
		yA[1] = (int) (lastY + dY);
		xA[2] = (int) (lastX - dX);
		yA[2] = (int) (lastY - dY);
		xA[3] = (int) (x - dX);
		yA[3] = (int) (y - dY);
		return new Polygon(xA,yA,4);
	}
	public static void drawParticle(Graphics2D g2v,Polygon poly, int i, int s, Particle p){
		switch(Data.s){
		case LINE:
			g2v.drawLine((int)p.lastX, (int)p.lastY, (int)p.x, (int)p.y);
			break;
		case HEAD:
			if(fill){
				g2v.fillOval((int)p.x - s/2, (int)p.y - s/2, s, s);
			}else{
				g2v.drawOval((int)p.x - s/2, (int)p.y - s/2, s, s);
			}
			break;
		case TAIL:
			if(fill){
				g2v.fillPolygon(poly);
			}else{
				g2v.drawPolygon(poly);
			}
			break;
		case FULL:
			if(fill){
				g2v.fillPolygon(poly);
				g2v.fillOval((int)p.x - s/2, (int)p.y - s/2, s - 1, s - 1);
			}else{
				g2v.drawPolygon(poly);
				g2v.drawPolygon(poly);
			}
			break;
		case DOT:
			if(fill){
				g2v.fillRect((int)p.x - s/2, (int)p.y - s/2, s, s);
			}else{
				g2v.drawRect((int)p.x - s/2, (int)p.y - s/2, s, s);
			}
			break;
		case CONNECTMOUSE:
			g2v.drawLine((int)p.x, (int)p.y, Data.lastMouse.x, Data.lastMouse.y);
			break;
		case CONNECTPIXEL:
			if(p.prev != null){
				g2v.drawLine((int)p.x, (int)p.y, (int)p.prev.x, (int)p.prev.y);
			}
			break;
		case TRIANGLES:
			int z = (int) Math.ceil(((double)particleNum / (double)Data.colorWheelMultiplier));

			if(i % z != 0 || i > particleNum - 3){
				break;
			}
			Polygon polygon = new Polygon();
			Polygon[] magic = new Polygon[3];
			Particle c = p;
			Particle[] aA = new Particle[3];
			for(int j = 0; j < 3;j++){
				polygon.addPoint((int)c.x, (int)c.y);
				magic[j] = new Polygon();
				aA[j] = c;
				c = c.next;
			}
			for(int j = 0; j < 3;j++){
				Polygon p0 = magic[j];
				int k = (j + 1) % 3;
				p0.addPoint((int)aA[j].x, (int)aA[j].y);
				p0.addPoint((int)aA[j].lastX, (int)aA[j].lastY);
				p0.addPoint((int)aA[k].lastX, (int)aA[k].lastY);
				p0.addPoint((int)aA[k].x, (int)aA[k].y);
				if(Data.fill){
					g2v.fillPolygon(p0);
				}
			}
			if(!Data.fill){
				g2v.draw(polygon);
			}
			break;
		}
		if(Data.s == Shape.LINE){
			g2v.drawLine((int)p.lastX, (int)p.lastY, (int)p.x, (int)p.y);
		}
	}
	public static int getSize(Particle p){
		int s = p.size;
		if(Data.mouseStretch){
			double u = -(p.lastMouseDist-230);
			s = (int)Math.floor(Math.pow(1.02, u) + 1);
			if(s < 0){
				s = 1;
			}
			if(Data.speedStretch){
				s /= 2;
				s += (int)Math.ceil(p.getSpeed() * 4) / 2;
			}
		}
		else if(Data.speedStretch){
			double u = Math.ceil(p.getSpeed() * 4);
			s = (int)u;
		}
		return s;
	}
	public static void doDraw(Graphics2D g2v, Graphics2D g2b){
		for(int i = 0; i < particleNum;i++){
			Particle p = Data.particles[i];
			double x = p.x;
			double y = p.y;
			double lastX = p.lastX;
			double lastY = p.lastY;
			double m = (lastY - y)/(lastX - x);
			double m2 = -1/m;
			double a = Math.atan(m2);
			int s = getSize(p);
			Polygon poly = getPoly(p.x, p.y, p.lastX, p.lastY, a, s);
			g2v.setColor(new Color(getRGB(p)));
			drawParticle(g2v, poly, i, s, p);
			//TODO Fix this flag statement with something streamline, and switch this Data.s to a switch-case thing
			
		}
	}
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

}
