package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

public class Client {
  private static final int SERVER_PORT = 8081;

  public static final String[] public_key_paths = {
    "publicKey_C", // Clave último Mix
    "publicKey_B",
    "publicKey_A"
  };

  public static void main(String[] args) {
    try (
      Socket socket = new Socket("127.0.0.1", SERVER_PORT);
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      DataInputStream in = new DataInputStream(socket.getInputStream());
      Scanner scanner = new Scanner(System.in);
    ) {
      System.out.println("Conectado al servidor en el puerto " + SERVER_PORT);

      PublicKey publicKeys[] = loadMixPublicKeys(public_key_paths);

      System.out.println("Escribe un mensaje: ");
      String message = scanner.nextLine();

      byte[] onionEncryptedMessage = BuildLayer.onionEncrypt(message.getBytes("UTF-8"), publicKeys);

      out.writeInt(onionEncryptedMessage.length);
      out.write(onionEncryptedMessage);
      out.flush();
      System.out.println("Paquete cifrado enviado al servidor.");

      String response = in.readUTF();
      System.out.println("Respuesta del servidor: " + response);
    } catch (Exception e) {
      System.out.println("Error en el cliente: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static PublicKey[] loadMixPublicKeys(String [] filenames) throws Exception {
    PublicKey[] keys = new PublicKey[filenames.length];
    for (int i = 0; i < filenames.length; i++) {
      keys[i] = CryptoUtils.loadPublicKey(filenames[i]);
    }
    return keys;
  }
}
