package pixelphysics2;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Random;

public class Data {
	
	static int particleNum = 50;
	static int ParticleRangeWidth = 10;
	static Random rand = new Random();
	static int Seed = rand.nextInt();
	static int BackWidth = 0;
	static int BackHeight = 0;
	enum Texture{
		INDIVIDUAL, SPEED, CLASSIC, ANGLE, MOUSE_DISTANCE, MOUSE_LOCATION
	}
	enum Shape{
		BOTH, CIRCLE, RECTANGLE, LINE
	}
	static Texture t = Texture.INDIVIDUAL;
	static Shape s = Shape.BOTH;
	static double colorWheelMultiplier = 1;
	static boolean colorWheelFlip = false;
	static boolean stretch = true;
	public static int[] RGB = new int[3];
	public static int RGB_switch = 1;
	public static boolean paint = false;
	public static int shiftAmount = 1;
	static boolean fill = true;
//	static BufferedImage bi;
	static VolatileImage vImage;
	static VolatileImage backgroundImage;
}
