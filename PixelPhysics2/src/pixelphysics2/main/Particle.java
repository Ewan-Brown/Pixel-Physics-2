package pixelphysics2.main;

import java.awt.Color;

public class Particle {
	
	public Particle(double x, double y,double vX,double vY){
		this.x = x;
		this.y = y;
		this.lastX = x;
		this.lastY = y;
		alpha = (float)Math.random();
	}
	public int size = 20;
	public double x = 0;
	public double y = 0;
	public double lastX = 0;
	public double lastY = 0;
	public double vY = 0;
	public double vX = 0;
	public float alpha = 1;
	public double vXP = 0;
	public double vYP = 0;
	public double lastMouseDist = 0;
	public double lastMouseAngle = 0;
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
