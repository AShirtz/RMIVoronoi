package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import utils.Algorithms;
import utils.Line;
import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;

public class VoronoiTest {
	
	static int height = 1000;
	static int width = 1000;
	
	static int genZoneHeight = 400;
	static int genZoneWidth = 400;
	
	public static void main(String[] args) {
		Set<Point> pointSet = DataPoints.generateSetPoints(20, width, 0, height, 0);
		
		Set<Triangle> tris = Algorithms.BowyerWatson(pointSet);
		Set<VoronoiCell> cells = Algorithms.generateVoronoiFromDelaunay(tris);
		Set<Line> lines = VoronoiCell.mapCellsToLines(cells);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		for (Triangle t : tris) {
			g.setColor(Color.BLACK);
			t.drawTriangle(g);
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
