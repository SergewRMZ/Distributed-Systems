package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

public class Client {
  private static final int SERVER_PORT = 8000;
  private static final String PUBLIC_KEY_A = "publicKey_Server_A";

  public static void main(String[] args) {
    try (
      Socket socket = new Socket("127.0.0.1", SERVER_PORT);
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      DataInputStream in = new DataInputStream(socket.getInputStream());
      Scanner scanner = new Scanner(System.in);
    ) {
      System.out.println("Conectado al servidor en el puerto " + SERVER_PORT);

      PublicKey publicKey = loadPublicKey(PUBLIC_KEY_A);

      System.out.println("Escribe un mensaje: ");
      String message = scanner.nextLine();
      byte[] encryptedBytes = encrypt(message, publicKey);

      System.out.println("Mensaje cifrado: " + Base64.getEncoder().encodeToString(encryptedBytes));
      
      out.writeInt(encryptedBytes.length);
      out.write(encryptedBytes);
      out.flush();
      System.out.println("Paquete cifrado enviado al servidor.");

      String response = in.readUTF();
      System.out.println("Respuesta del servidor: " + response);
    } catch (Exception e) {
      System.out.println("Error en el cliente: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static PublicKey loadPublicKey(String filename) throws Exception {
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    return keyFactory.generatePublic(keySpec);
  }

  public static byte[] encrypt(String data, PublicKey publicKey) throws Exception {
    byte[] dataBytes = data.getBytes();
    Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBytes = cipher.doFinal(dataBytes);
    return encryptedBytes;
  }
}
