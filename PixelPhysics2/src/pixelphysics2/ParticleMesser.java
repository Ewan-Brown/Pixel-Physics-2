package pixelphysics2;

import java.util.Hashtable;

public class ParticleMesser {

	public static Hashtable<String, ParticleMesser> map = new Hashtable<String, ParticleMesser>();
	public static void init(){
		map.put("Normal", new ParticleMesser(){
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				p.x += p.getXV();
				p.y += p.getYV();
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
			}
		});
		map.put("Color", new ParticleMesser(){
		public void doParticle(Particle p,long tick){
			p.lastX = p.x;
			p.lastY = p.y;
			int z = p.RGB;
			double alpha = z >> 24 & 0xff;
			double a = alpha / 256;
			p.x += p.getXV() * a;
			p.y += p.getYV() * a;
			p.vX -= p.vX / 200;
			p.vY -= p.vY / 200;
		}
	});
		map.put("Reverse", new ParticleMesser(){
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				p.x -= p.getXV();
				p.y -= p.getYV();
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
			}
		});
		map.put("Straight", new ParticleMesser(){
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				if(Math.abs(p.getXV()) > Math.abs(p.getYV())){
					p.x += Math.signum(p.getXV()) * 5;
				}
				else{
					p.y += Math.signum(p.getYV()) * 5;
				}
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
			}
		});
		map.put("StraightSlidey", new ParticleMesser(){
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				if(Math.abs(p.getXV()) > Math.abs(p.getYV())){
					p.x += p.getXV();
				}
				else{
					p.y += p.getYV();
				}
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
			}
		});
		map.put("Spiral", new ParticleMesser(){
			int z = Main.rand.nextInt(6) + 2;
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				double c = p.getSpeed();
				double t = p.getAngle();
				double phi = (tick/10 * c / 10D);
				double yP = Math.sin(phi);
				double a = t + phi;
				double xP = c * Math.cos(a);
				double yP2 = c * Math.sin(a);
				p.vXP = xP;
				p.vYP = yP2;
				p.x += p.getXV() / z;
				p.y += p.getYV() / z;
			}
		});
		map.put("SpiralFriction", new ParticleMesser(){
			int z = Main.rand.nextInt(6) + 2;
			public void doParticle(Particle p,long tick){
				p.lastX = p.x;
				p.lastY = p.y;
				double c = p.getSpeed();
				double t = p.getAngle();
				double phi = (tick/10 * c / 10D);
				double yP = Math.sin(phi);
				double a = t + phi;
				double xP = c * Math.cos(a);
				double yP2 = c * Math.sin(a);
				p.vXP = xP;
				p.vYP = yP2;
				p.x += p.getXV() / z;
				p.y += p.getYV() / z;
				p.vX -= p.vX / 200;
				p.vY -= p.vY / 200;
				p.vXP -= p.vXP / 200;
				p.vYP -= p.vYP / 200;
			}
		});
	}
	public void doParticle(Particle p,long tick){

	}

}
