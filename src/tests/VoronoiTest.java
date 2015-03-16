package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import utils.Algorithms;
import utils.Diagram;
import utils.Line;
import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;

public class VoronoiTest {
	
	public static void main(String[] args) {
		Diagram diag = new Diagram (1000, 1000);
		Set<Point> pointSet = DataPoints.generatePoints(40, diag.getWidth(), 0, diag.getHeight(), 0);
		
		Set<Triangle> tris = Algorithms.BowyerWatson(pointSet);
		Set<VoronoiCell> cells = Algorithms.generateVoronoiFromDelaunay(tris, diag);
		Set<Line> lines = VoronoiCell.mapCellsToLines(cells);
		
		BufferedImage image = new BufferedImage(diag.getWidth(), diag.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, diag.getWidth(), diag.getHeight());
		
		for (Triangle t : tris) {
			g.setColor(Color.BLACK);
			//t.drawTriangle(g);
			g.setColor(Color.green);
			//t.drawCircumcircle(g);
		}
		g.setColor(Color.RED);
		for (Line l : lines) {
			l.drawLine(g);
		}
		g.setColor(Color.blue);
		for (Point p : VoronoiCell.mapCellsToGenPoints(cells)) {
			p.drawPoint(g);
		}
		
		g.dispose();
		
		try {
			ImageIO.write(image, "bmp", new File("VoronoiTestFile.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
