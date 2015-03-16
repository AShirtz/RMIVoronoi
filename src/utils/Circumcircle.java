package utils;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Iterator;

public class Circumcircle implements Serializable {
	
	public static final double epsilon = 1.0;

	public Point centerPoint;
	public double radius;
	
	public Circumcircle (Triangle t) {
		Iterator <Point> iter = t.endpoints.iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		
		if (A.yCoord == B.yCoord) {
			A.yCoord++;
		}
		if (B.yCoord == C.yCoord) {
			C.yCoord++;
		}
		
		Point p = new Point (0.5*(A.xCoord + B.xCoord), 0.5*(A.yCoord + B.yCoord));
		Point q = new Point (0.5*(B.xCoord + C.xCoord), 0.5*(B.yCoord + C.yCoord));
		
		double u = - (A.xCoord - B.xCoord) / (A.yCoord - B.yCoord);
		double v = - (B.xCoord - C.xCoord) / (B.yCoord - C.yCoord);
		
		if (Double.isNaN(u) || Double.isNaN(v) || Double.isInfinite(u) || Double.isInfinite(v) || u == v) {
			throw new RuntimeException("Circumcircle failure.");
		}
		
		double x = (q.yCoord + (u * p.xCoord) - (v * q.xCoord) - p.yCoord) / (u - v);
		double y = (v * (x - q.xCoord) + q.yCoord);
		
		this.centerPoint = new Point(x,y);
		this.radius = this.centerPoint.distance(A);
	}
	
	public boolean isInsideCircumcircle (Point p) {
		return p.distance(this.centerPoint) < this.radius;
	}
	
	public void drawCircumcircle (Graphics g) {
		g.drawOval((int) (this.centerPoint.xCoord - (this.radius)), (int) (this.centerPoint.yCoord - (this.radius)), (int) this.radius * 2, (int) this.radius * 2);
	}
}
