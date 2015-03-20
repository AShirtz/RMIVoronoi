package utils.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import utils.Diagram;
import utils.Point;

public interface GameControllerInterface extends Remote {
	public void registerClient (ClientInterface client) throws RemoteException;
	public void acceptMove (ClientInterface client, Point newPoint) throws RemoteException;
	public Diagram getBoard () throws RemoteException;
	public Set<Point> getGenPointsFor (ClientInterface client) throws RemoteException;
	public int getScoreForPlayerOne (ClientInterface client) throws RemoteException;
	public int getScoreForPlayerTwo (ClientInterface client) throws RemoteException;
	public void deRegisterClient (ClientInterface client) throws RemoteException;
}
