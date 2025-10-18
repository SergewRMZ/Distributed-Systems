import java.io.File;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyGenerator {

    public static void writeKey(String filename, String key_base_64) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.print(key_base_64);
            System.out.println("Llave guardada en: " + filename); // Mensaje más específico
        } catch (Exception e) {
            System.out.println("Error al guardar la llave en el archivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Nombres de los servidores Mix para el loop
        String[] serverNames = {"A", "B", "C"}; 

        try {
            // Inicialización del generador de llaves (fuera del loop si es costoso)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            
            System.out.println("Iniciando generación de pares de llaves RSA-2048...");
            
            for (String name : serverNames) {
                // 1. Generar el par de llaves
                KeyPair pair = keyGen.generateKeyPair();
                PrivateKey privateKey = pair.getPrivate();
                PublicKey publicKey = pair.getPublic();

                // 2. Codificar a Base64
                String pubKeyB64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                String privKeyB64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

                // 3. Escribir al disco con nombres específicos
                String pubFilename = "publicKey_" + name;
                String privFilename = "privateKey_" + name;
                
                writeKey(pubFilename, pubKeyB64);
                writeKey(privFilename, privKeyB64);
                
                System.out.println("Pares de llaves para Mix " + name + " generados correctamente.");
                System.out.println("-------------------------------------------");
            }
            
        } catch (Exception e) {
            System.out.println("Error durante la generación de llaves.");
            e.printStackTrace();
        }
    }
}