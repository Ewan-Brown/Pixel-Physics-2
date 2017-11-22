package pixelphysics2;

import java.awt.Color;

public class Particle {
	
	public Particle(double x, double y,double vX,double vY){
		this.x = x;
		this.y = y;
		this.lastX = x;
		this.lastY = y;
		this.vX = vX;
		this.vY = vY;
	}
	int size = 20;
	double x = 0;
	double y = 0;
	double lastX = 0;
	double lastY = 0;
	double vY = 0;
	double vX = 0;
	int RGB = Color.WHITE.getRGB();
	public double getSpeed(){
		return Math.sqrt(vX * vX + vY * vY);
	}
	public double getAngle(){
		return Math.atan2(-vY, vX);
	}

}
