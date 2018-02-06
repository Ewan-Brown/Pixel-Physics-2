package pixelphysics2.ui;

import java.util.ArrayList;

import pixelphysics2.main.Data;
import pixelphysics2.main.Data.Shape;
import pixelphysics2.main.Data.Texture;

public class Value<K> {
	String name;
	public Value(String s){
		name = s;
	}

	public K getValue(){
		return null;
	}
	public void setValue(){
		//TODO DO SOMETHING HERE SOMETIMES???
	}
	public static ArrayList<Value> k = new ArrayList<Value>();
	public static void init(){
		k.add(vMaxParticleNum);
		k.add(vParticleNum);
		k.add(vTexture);
		k.add(vShape);
		k.add(vMouseStretch);
		k.add(vSpeedStretch);
	}
	public static Value<Integer> vMaxParticleNum = new Value<Integer>("Max Particles"){
		public Integer getValue(){
			return Data.maxParticleNum;
		}
	};	
	public static Value<Integer> vParticleNum = new Value<Integer>("Particle Count"){
		public Integer getValue(){
			return Data.particleNum;
		}
	};	
	public static Value<Texture> vTexture = new Value<Texture>("Texture"){
		public Texture getValue(){
			return Data.t;
		}
	};	
	public static Value<Shape> vShape = new Value<Shape>("Shape"){
		public Shape getValue(){
			return Data.s;
		}
	};	
	public static Value<Boolean> vMouseStretch = new Value<Boolean>("Mouse Stretch"){
		public Boolean getValue(){
			return Data.mouseStretch;
		}
	};	
	public static Value<Boolean> vSpeedStretch = new Value<Boolean>("Speed Stretch"){
		public Boolean getValue(){
			return Data.speedStretch;
		}
	};	
}
