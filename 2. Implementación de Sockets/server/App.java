package server;

public class App {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Uso correcto:");
            System.out.println("java server.MixNodeApp <puerto_local> <ruta_clave_privada> <puerto_siguiente_mix> <host_siguiente_mix>");
            System.out.println("\nEjemplo para el Mix 1: java server.MixNodeApp 8081 key_A 8082 localhost");
            return;
        }

        try {
            int currentPort = Integer.parseInt(args[0]); 
            String privateKeyPath = args[1];
            int nextMixPort = Integer.parseInt(args[2]);
            String nextMixHost = args[3];

            MixNode server = new MixNode(
                currentPort, 
                privateKeyPath, 
                nextMixHost,
                nextMixPort 
            );
            
            System.out.println("Iniciando MixNode en puerto " + currentPort);
            server.init();
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El puerto debe ser un número entero válido.");
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar la Mixnet: " + e.getMessage());
        }
    }
}