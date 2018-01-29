package pixelphysics2;

import java.awt.Point;

public class Click {
	int keyCode = 0;
	Point point = null;
	
	public Click(Point p, int k){
		this.point = p;
		this.keyCode = k;
	}
	
}
