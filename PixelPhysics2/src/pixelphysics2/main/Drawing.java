package pixelphysics2.main;

import static pixelphysics2.main.Data.RGB;
import static pixelphysics2.main.Data.fill;
import static pixelphysics2.main.Data.particleNum;
import static pixelphysics2.main.Data.vImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.VolatileImage;

import pixelphysics2.main.Data.Shape;

public class Drawing {

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
		g2b.drawImage(retVal, 0, 0, null);
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
			r = RGB[0];
			g = RGB[1];
			b = RGB[2];
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
			//		case PARTICLE_LOCATION:
			//			
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

	public static void doDraw(Graphics2D g2v){
		for(int i = 0; i < particleNum;i++){
			Particle p = Data.particles[i];
			double x = p.x;
			double y = p.y;
			double lastX = p.lastX;
			double lastY = p.lastY;
			double m = (lastY - y)/(lastX - x);
			double m2 = -1/m;
			double a = Math.atan(m2);
			int s = Drawing.getSize(p);
			Polygon poly = Main.getPoly(p.x, p.y, p.lastX, p.lastY, a, s);
			g2v.setColor(new Color(getRGB(p)));
			Drawing.drawParticle(g2v, poly, i, s, p);
	
		}
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

}
