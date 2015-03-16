package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import utils.Point;
import utils.VoronoiCell;
import utils.rmi.RemoteInterface;

public class RMIClientTest {
	
	public static void main (String[] args) {
		try {
			RemoteInterface rDiag;
			rDiag = (RemoteInterface) java.rmi.Naming.lookup("rmi://euclid.nmu.edu:8989/voronoi");
			Set<Point> points = DataPoints.generatePoints(20, rDiag.getWidth(), 0, rDiag.getHeight(), 0);
			rDiag.addPoints(points);
			
			BufferedImage image = new BufferedImage(rDiag.getWidth(), rDiag.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, rDiag.getWidth(), rDiag.getHeight());
			g.setColor(Color.BLACK);
			for (VoronoiCell cell : rDiag.getCells()) {
				cell.drawCell(g);
			}
			g.dispose();
			try {
				ImageIO.write(image, "bmp", new File("ClientTestFile.bmp"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
