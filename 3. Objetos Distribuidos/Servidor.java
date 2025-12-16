import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Servidor {
  public static void main (String[] args) {
    try {
      Calculadora obj = new CalculadoraImpl();
      LocateRegistry.createRegistry(8080);
      String url = "rmi://localhost:8080/CalculadoraRemota";
      Naming.rebind(url, obj);
      System.out.println("Servidor de Calculadora listo y enlazado en el registro: " + url);
    } catch (Exception e) {
      System.err.println("Excepción en el Servidor: " + e.toString());
      e.printStackTrace();
    }
  }
}
