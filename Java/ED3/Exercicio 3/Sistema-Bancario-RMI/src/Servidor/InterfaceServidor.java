package Servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServidor extends Remote {

    int somar(int a, int b) throws RemoteException;

    int somar(int[] vetor) throws RemoteException;

    String upperCase(String texto) throws RemoteException;

    String getStatusServidor() throws RemoteException;
}