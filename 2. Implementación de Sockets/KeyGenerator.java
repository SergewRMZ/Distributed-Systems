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
      System.out.println("Llaves generadas correctamente");
    } catch (Exception e) {
      System.out.println("Error al guardar la llave en el archivo: " + e.getMessage());
    }
  }
  public static void main(String[] args) {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(2048);

      KeyPair pair = keyGen.generateKeyPair();
      PrivateKey privateKey = pair.getPrivate();
      PublicKey publicKey = pair.getPublic();

      String pubKeyB64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
      String privKeyB64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

      writeKey("publicKey_Server_A", pubKeyB64);
      writeKey("privateKey_Server_A", privKeyB64);

    } catch (Exception e) {
      System.out.println("Error durante la generación de llaves.");
      e.printStackTrace();
    }
  }
}
