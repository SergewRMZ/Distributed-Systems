import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class CalculadoraImpl extends UnicastRemoteObject implements Calculadora {
  public CalculadoraImpl() throws RemoteException {
    super();
  }

  public long sumar (long a, long b) throws RemoteException {
    return a + b;
  }

  public long restar (long a, long b) throws RemoteException {
    return a - b;
  }

  public long multiplicar (long a, long b) throws RemoteException {
    return a * b;
  }

  public double dividir (long a, long b) throws RemoteException {
    if (b == 0) {
      throw new ArithmeticException("División por cero");
    }
    return (double) a / b;
  }
}