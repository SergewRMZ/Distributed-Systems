import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculadora extends Remote {
  public long sumar (long a, long b) throws RemoteException;
  public long restar (long a, long b) throws RemoteException;
  public long multiplicar (long a, long b) throws RemoteException;
  public double dividir (long a, long b) throws RemoteException;
}