package utils;

import java.awt.Graphics;

public class Circumcircle {
	
	public static final double epsilon = 1.0;

	public Point centerPoint;
	public double radius;
	
	public Circumcircle (Triangle t) {
		
		Point p = new Point (0.5*(t.endpoints.get(0).xCoord + t.endpoints.get(1).xCoord), 0.5*(t.endpoints.get(0).yCoord + t.endpoints.get(1).yCoord));
		Point q = new Point (0.5*(t.endpoints.get(1).xCoord + t.endpoints.get(2).xCoord), 0.5*(t.endpoints.get(1).yCoord + t.endpoints.get(2).yCoord));

		double u = - (t.endpoints.get(0).xCoord - t.endpoints.get(1).xCoord) / (t.endpoints.get(0).yCoord - t.endpoints.get(1).yCoord);
		double v = - (t.endpoints.get(1).xCoord - t.endpoints.get(2).xCoord) / (t.endpoints.get(1).yCoord - t.endpoints.get(2).yCoord);
		
		double x = (q.yCoord + (u * p.xCoord) - (v * q.xCoord) - p.yCoord) / (u - v);
		double y = (v * (x - q.xCoord) + q.yCoord);
		
		this.centerPoint = new Point(x,y);
		double distanceA = this.centerPoint.distance(t.endpoints.get(0));
		double distanceB = this.centerPoint.distance(t.endpoints.get(1));
		double distanceC = this.centerPoint.distance(t.endpoints.get(2));
		
		this.radius = distanceA;
	}
	
	public boolean isInsideCircumcircle (Point p) {
		return p.distance(this.centerPoint) < this.radius;
	}
	
	public void drawCircumcircle (Graphics g) {
		g.drawOval((int) (this.centerPoint.xCoord - (this.radius)), (int) (this.centerPoint.yCoord - (this.radius)), (int) this.radius * 2, (int) this.radius * 2);
	}
}
