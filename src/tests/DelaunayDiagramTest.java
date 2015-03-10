package tests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import utils.Algorithms;
import utils.Line;
import utils.Point;
import utils.Triangle;

public class DelaunayDiagramTest {
	
	static int height = 1000;
	static int width = 1000;
	
	static int genZoneHeight = 400;
	static int genZoneWidth = 400;
	
	public static void main(String[] args) {
		Random rand = new Random();
		
		Set<Point> pointSet = DataPoints.generatePoints(20, width, 0, height, 0);
		
		Set<Triangle> tris = Algorithms.BowyerWatson(pointSet);
		
		Set<Line> lines = Triangle.mapTrianglesToLines(tris);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		for (Triangle t : tris) {
			g.setColor(Color.BLACK);
			t.drawTriangle(g);
			//g.setColor(Color.BLUE);
			//t.drawCircumcircle(g);
		}
		int i = 0;
		g.setColor(Color.RED);
		for (Point p : pointSet) {
			//p.drawPointWithCount(g, i++);
		}
		g.dispose();
		
		try {
			ImageIO.write(image, "bmp", new File("TestFile.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
