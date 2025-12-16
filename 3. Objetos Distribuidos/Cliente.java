import java.rmi.Naming;

public class Cliente {
    public static void main(String[] args) {
        try {
            String url = "rmi://localhost:8080/CalculadoraRemota";
            Calculadora calc = (Calculadora) Naming.lookup(url);

            System.out.println("Conectado al objeto remoto.");
            System.out.println("Suma (80 + 20): " + calc.sumar(80, 20));
            System.out.println("Resta (150 - 30): " + calc.restar(150, 30));            
            System.out.println("Multiplicación (12 * 5): " + calc.multiplicar(12, 5));
            
            double resDiv = calc.dividir(100, 4);
            System.out.println("División (100 / 4): " + resDiv);

            try {
                System.out.print("Intentando División (10 / 0)... ");
                calc.dividir(10, 0);
            } catch (Exception e) {
                System.out.println("¡Excepción capturada! " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Excepción en el Cliente (RMI o General): " + e.toString());
        }
    }
}