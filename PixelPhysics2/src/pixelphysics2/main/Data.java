package pixelphysics2.main;

import java.awt.Point;
import java.awt.image.VolatileImage;
import java.util.Random;

public class Data {
	public static int maxParticleNum = 1000;
	public static int particleNum = 50;
	public static int ParticleRangeWidth = 10;
	public static Random rand = new Random();
	public static int Seed = rand.nextInt();
	public static int BackWidth = 0;
	public static int BackHeight = 0;
	public static int alpha = 100;
	public static double flickMult = 1;
	public static boolean lowPerformance = true;
	public enum Texture{
		INDIVIDUAL, SPEED, CLASSIC, ANGLE, MOUSE_DISTANCE, MOUSE_LOCATION, MOUSE_ANGLE//PARTICLE_LOCATION;
	}
	public enum Shape{
		FULL, HEAD, TAIL, LINE, DOT, CONNECTMOUSE, CONNECTPIXEL,TRIANGLES;
	}
	public static Texture t = Texture.INDIVIDUAL;
	public static Shape s = Shape.FULL;
	public static double colorWheelMultiplier = 1;
	public static boolean colorWheelFlip = false;
	public static boolean mouseStretch = true;
	public static boolean speedStretch = true;
	public static int[] RGB = new int[3];
	public static int RGB_switch = 1;
	public static boolean paint = false;
	public static int shiftAmount = 1;
	public static boolean fill = true;
	public static VolatileImage vImage;
	public static int lastMouseRGB = 0;
	public static double forceMult = 1;
	public static double frictionMult = 1;
	public static ParticleMesser pm;
	public static Point move;
	public static Point lastMouse = null;
	public static long lastTime = System.nanoTime();
	public static Particle[] particles;
	public static long tick = 0;
	public static int panelLag = 0;
}
