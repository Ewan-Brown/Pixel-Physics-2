package pixelphysics2.main;

import static pixelphysics2.main.Data.RGB;
import static pixelphysics2.main.Data.RGB_switch;
import static pixelphysics2.main.Data.particleNum;
import static pixelphysics2.main.Data.rand;
import static pixelphysics2.main.Data.shiftAmount;
import static pixelphysics2.main.Data.vImage;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import pixelphysics2.main.Data.Shape;
import pixelphysics2.main.Data.Texture;
import pixelphysics2.ui.UI;
import pixelphysics2.ui.Value;

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
			float d = 0;
			boolean bo = false;
			if(xI > minX && xI < maxX){
				bo = true;
				d = pDistance(xA,yA,x1,y1,x2,y2);
			}
			if(xI > maxX){
				bo = true;
				d = (float) getDistance(xA, yA, maxX, maxY);
			}
			if(xI < minX){
				bo = true;
				d = (float) getDistance(xA, yA, minX, minY);	
			}
			if(bo){
				d *= Data.flickMult;
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
//		Data.shiftAmount = rand.nextInt(6) + 1;
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
		Data.particles =  new Particle[particleNum];
		for (int i = 0 ;i < particleNum;i++) {
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
		Value.init();
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
		Data.lastMouse = Inputerface.getMouseLocation();
//		new GamePanel();
		new UI();
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
	public static void setLastMouseRGB(){
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
	}
	static GraphicsConfiguration gc = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();
	public static void update(){
		Data.tick++;
		setLastMouseRGB();
		doPulls();
		shiftColor();
		 if(vImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE){
	            vImage = gc.createCompatibleVolatileImage(vImage.getWidth(), vImage.getHeight());
		 }
		Graphics2D g2b = (Graphics2D)vImage.getGraphics();
		Drawing.doBackGroundDraw(g2b);
		Drawing.doDraw(g2b);
		vImage.flush();
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
				p.vX -= deltaX*Data.forceMult / 5D;
				p.vY -= deltaY*Data.forceMult / 5D;
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
	public static double getDistance( double x1,  double y1,  double x2,  double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

}
