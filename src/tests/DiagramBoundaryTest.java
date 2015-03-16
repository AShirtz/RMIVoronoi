package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import utils.Diagram;
import utils.Line;
import utils.Point;

public class DiagramBoundaryTest {

	public static void main(String[] args) {
		Diagram diag = new Diagram(100, 100);
		Point p = DataPoints.generatePoints(1, 100, 0, 100, 0).iterator().next();
		double slope = 1;
		
		List<Point> boundaryIntercepts = diag.createPointsOnBoundary(p, slope);
		System.out.println(boundaryIntercepts.get(0).toString());
		System.out.println(boundaryIntercepts.get(1).toString());
		
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 100, 100);
		
		g.setColor(Color.black);
		
		new Line(p, boundaryIntercepts.get(0)).drawLine(g);
		new Line(p, boundaryIntercepts.get(1)).drawLine(g);
		g.setColor(Color.blue);
		p.drawPoint(g);
		boundaryIntercepts.get(0).drawPoint(g);
		boundaryIntercepts.get(1).drawPoint(g);

		//TODO: its backwards, positive slopes showing up as negative slopes
		boolean eh = Point.slopeBetween(p, boundaryIntercepts.get(0)) == slope;
		System.out.println("Should be true " + eh);
		System.out.println("Should be equivalent slopes: " + (Point.slopeBetween(p, boundaryIntercepts.get(0)) == Point.slopeBetween(p, boundaryIntercepts.get(1))));
		g.dispose();
		
		try {
			ImageIO.write(image, "bmp", new File("DiagramBoundary.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
