package pixelphysics2;

import java.awt.image.VolatileImage;
import java.util.Random;

public class Data {
	static int maxParticleNum = 1000;
	static int particleNum = 50;
	static int ParticleRangeWidth = 10;
	static Random rand = new Random();
	static int Seed = rand.nextInt();
	static int BackWidth = 0;
	static int BackHeight = 0;
	static int alpha = 100;
	static boolean lowPerformance = true;
	enum Texture{
		INDIVIDUAL, SPEED, CLASSIC, ANGLE, MOUSE_DISTANCE, MOUSE_LOCATION
	}
	enum Shape{
		FULL, HEAD, TAIL, LINE, DOT, CONNECTMOUSE, CONNECTPIXEL,TRIANGLES;
	}
	static Texture t = Texture.INDIVIDUAL;
	static Shape s = Shape.FULL;
	static double colorWheelMultiplier = 1;
	static boolean colorWheelFlip = false;
	static boolean mouseStretch = true;
	static boolean speedStretch = true;
	public static int[] RGB = new int[3];
	public static int RGB_switch = 1;
	public static boolean paint = false;
	public static int shiftAmount = 1;
	static boolean fill = true;
	static VolatileImage vImage;
	static int lastMouseRGB = 0;
	public static double forceMult = 1;
	public static double frictionMult = 1;
	public static ParticleMesser pm;
}
