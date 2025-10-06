package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class ClientHandler implements Runnable {
  private final Socket socket;
  private static final String PRIVATE_KEY = "privateKey_Server_A";

  public ClientHandler(Socket socket) {
    this.socket = socket;  
  }

  public PrivateKey loadPrivateKey(String filename) throws Exception{
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    return keyFactory.generatePrivate(keySpec);
  }

  public byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cipher.doFinal(encryptedData);
    return decryptedBytes;
  }

  @Override
  public void run() {
    try (
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    ) {
      int length = in.readInt();
      if(length > 0) {
        byte[] encryptPacket = new byte[length];
        in.readFully(encryptPacket, 0, encryptPacket.length);

        System.out.println("Paquete cifrado recibido. Descifrando...");
        PrivateKey privateKey = loadPrivateKey(PRIVATE_KEY);
        byte[] decryptedData = decrypt(encryptPacket, privateKey);
        String message = new String(decryptedData);
        System.out.println("Paquete descifrado: " + message);
        out.writeUTF("OK: Mensaje recibido y descifrado correctamente");
      }
    } catch (Exception e) {
      System.err.println("Ha ocurrido un error en el ClientHandler: " + e.getMessage());
    }
  }
}
