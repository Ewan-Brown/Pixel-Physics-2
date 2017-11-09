package pixelphysics2;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Data {
	
	static int particleNum = 1000;
	static int width = 10;
	static int height = 20;
	static Random rand = new Random();
	static int mode = 2; //Normal:0 //Speed:1 //Angle:2
	public static int[] RGB = new int[3];
	public static int RGB_switch = 1;
	public static int shiftAmount = 1;
	static boolean fill = true;
	static BufferedImage bi = new BufferedImage(1920,1080,BufferedImage.TYPE_3BYTE_BGR);
}
