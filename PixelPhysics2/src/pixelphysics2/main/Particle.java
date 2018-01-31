package pixelphysics2.main;

import java.awt.Color;

public class Particle {
	
	public Particle(double x, double y,double vX,double vY){
		this.x = x;
		this.y = y;
		this.lastX = x;
		this.lastY = y;
	}
	int size = 20;
	double x = 0;
	double y = 0;
	double lastX = 0;
	double lastY = 0;
	double vY = 0;
	double vX = 0;
	double vXP = 0;
	double vYP = 0;
	double lastMouseDist = 0;
	double lastMouseAngle = 0;
	Particle prev;
	Particle next;
	int RGB = Color.WHITE.getRGB();
	public double getSpeed(){
		return Math.sqrt(vX * vX + vY * vY);
	}

	public double getXV(){
		return vX + vXP;
	}
	public double getYV(){
		return vY + vYP;
	}
	public double getAngle(){
//		return FastMath.atan2(-vY, vX);
//		return Math.atan2(-(vY+vYP) , vX+vXP);
		return Math.atan2(-(vY) , vX);
	}

}
