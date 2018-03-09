package pixelphysics2.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTextField;

import pixelphysics2.main.Data;
import pixelphysics2.main.Data.Shape;
import pixelphysics2.main.Data.Texture;

public class Value<K extends Object> implements FocusListener{
	String name;
	JComponent jc;
	static Object lastFocused;
	public Value(String s){
		name = s;
	}
	public K getRawValue(){
		//GETS RAW DATA
		return null;
	}
	public void setValue(K k){
		//SETS RAW DATA
	}
	public void updateValue(){
		//SETS VISUAL DATA
	}
	public static ArrayList<Value> list = new ArrayList<Value>();
	public static void init(){
		list.add(vMaxParticleNum);
		list.add(vParticleNum);
//		list.add(vTexture);
//		list.add(vShape);
//		list.add(vMouseStretch);
//		list.add(vSpeedStretch);
	}
	public static Value<Integer> vMaxParticleNum = new Value<Integer>("Max Particles"){
		public Integer getRawValue(){
			return Data.maxParticleNum;
		}
		{
			jc = new JTextField(getRawValue());
			jc.addFocusListener(this);
		}
		public void updateValue(){
			if(lastFocused != jc)
			((JTextField)jc).setText(getRawValue().toString());
		}
	};	
	public static Value<Integer> vParticleNum = new Value<Integer>("Particle Count"){
		public Integer getRawValue(){
			return Data.particleNum;
		}
		{
			jc = new JTextField(getRawValue());
			jc.addFocusListener(this);
		}
		public void updateValue(){
			if(lastFocused != jc)
			((JTextField)jc).setText(getRawValue().toString());
		}
	};	
	public static Value<Texture> vTexture = new Value<Texture>("Texture"){
		public Texture getRawValue(){
			return Data.t;
		}
	};	
	public static Value<Shape> vShape = new Value<Shape>("Shape"){
		public Shape getRawValue(){
			return Data.s;
		}
	};	
	public static Value<Boolean> vMouseStretch = new Value<Boolean>("Mouse Stretch"){
		public Boolean getRawValue(){
			return Data.mouseStretch;
		}
	};	
	public static Value<Boolean> vSpeedStretch = new Value<Boolean>("Speed Stretch"){
		public Boolean getRawValue(){
			return Data.speedStretch;
		}
	};
	@Override
	public void focusGained(FocusEvent e) {
		lastFocused = e.getSource();
	}
	@Override
	public void focusLost(FocusEvent e) {
		lastFocused = null;
	}	
}
