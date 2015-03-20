package utils.rmi;

import java.rmi.RemoteException;
import java.util.Set;

import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;

public interface RemoteDiagramInterface extends java.rmi.Remote {
	public void addPoints(Set<Point> pointSet) throws RemoteException;
	public Set<VoronoiCell> getCells () throws RemoteException;
	public Set<Triangle> getTriangles () throws RemoteException;
	public int getWidth() throws RemoteException;
	public int getHeight() throws RemoteException;
}
