package pixelphysics2;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Random;

public class Data {
	
	static int particleNum = 50;
	static int width = 10;
	static int height = 20;
	static Random rand = new Random();
	enum Texture{
		INDIVIDUAL, SPEED, CLASSIC, ANGLE
	}
	enum Shape{
		BOTH, CIRCLE, RECTANGLE, LINE
	}
	static Texture c = Texture.INDIVIDUAL;
	static Shape s = Shape.BOTH;
	static double colorWheelMultiplier = 1;
	static boolean colorWheelFlip = false;
	static boolean fade = true;
	static boolean stretch = true;
	public static int[] RGB = new int[3];
	public static int RGB_switch = 1;
	public static int shiftAmount = 1;
	static boolean fill = true;
	static BufferedImage bi;
}
