package pixelphysics2;

import java.util.Random;

public class Main {

	static Particle[] particles = new Particle[Data.particleNum];
	static Random rand = new Random();
	public static void main(String[] args) {
		for (int i = 0 ;i  < Data.particleNum;i++) {
			int x = i % Data.width;
			int y = (int)Math.floor(((double)i / (double)Data.width));
			x = (int)(Math.random() * 3000);
			y = (int)(Math.random() * 1000);
//			x /= 10; y /= 10;
			particles[i] = new Particle(x + 10,y + 10,Math.random() - 0.5,Math.random() - 0.5);
			particles[i].RGB = rand.nextInt(255) << 16 | rand.nextInt(255) << 8 | rand.nextInt(255);
			//	int rgb = r << 16 | g << 8 | b ;
			//	int r = rgb >> 16 & 0XFF;
			//	int g = rgb >> 8 & 0XFF;
			//	int b = rgb & 0XFF;
		}
		for(int i = 0; i < 4;i++){
			new GamePanel();
		}
		while(true){
			GamePanel.update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			for(int i = 0; i < Data.particleNum;i++){
				Particle p = particles[i];
				p.lastX = p.x;
				p.lastY = p.y;
				p.x += p.vX;
				p.y += p.vY;
				p.x -= p.x / 100;
				p.y -= p.y / 100;
			}
		}
		
	}
	
}
