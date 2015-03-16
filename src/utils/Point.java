package utils;

import java.awt.Graphics;
import java.io.Serializable;

public class Point implements Serializable {

	double xCoord;
	double yCoord;
	
	public Point(double x, double y) {
		this.xCoord = x;
		this.yCoord = y;
	}
	
	public double distance (Point p) {
		double result = (this.xCoord - p.xCoord) * (this.xCoord - p.xCoord) + (this.yCoord - p.yCoord) * (this.yCoord - p.yCoord);
		result = Math.sqrt(result);
		return result;
	}
	
	public static double slopeBetween (Point A, Point B) {
		double rise = (A.yCoord - B.yCoord);
		double run = (A.xCoord - B.xCoord);
		if (run == 0.0) {
			return Math.signum(rise) * Double.MAX_VALUE;
		}
		return rise / run;
	}
	
	public static Point averagePoints (Point A, Point B) {
		return new Point((A.xCoord + B.xCoord)/2, (A.yCoord + B.yCoord)/2);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xCoord);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoord);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Double.doubleToLongBits(xCoord) != Double
				.doubleToLongBits(other.xCoord))
			return false;
		if (Double.doubleToLongBits(yCoord) != Double
				.doubleToLongBits(other.yCoord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + this.xCoord + " , " + this.yCoord + ")";
	}
	
	public void drawPoint (Graphics g) {
		g.fillOval((int)this.xCoord - 4, (int)this.yCoord - 4, 8, 8);
	}
	
	public void drawPointWithCount (Graphics g, int i) {
		g.drawString("" + i, (int) this.xCoord, (int) this.yCoord);
	}

}
