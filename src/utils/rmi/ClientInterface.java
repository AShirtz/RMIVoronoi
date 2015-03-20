package utils.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import utils.Diagram;

public interface ClientInterface extends Remote {
	public void updateBoardView () throws RemoteException;
	public void acceptGameStart () throws RemoteException;
	public void acceptTurn () throws RemoteException;
	public void acceptGameEnd () throws RemoteException;
}
