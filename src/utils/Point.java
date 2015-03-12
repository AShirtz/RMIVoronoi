package utils;

import java.awt.Graphics;

public class Point {

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
