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
  private static final int SERVER_PORT = 8080;

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

      PublicKey publicKeys[] = loadMixPublicKyes(public_key_paths);

      System.out.println("Escribe un mensaje: ");
      String message = scanner.nextLine();

      byte[] onionEncryptedMessage = onionEncrypt(message.getBytes(), publicKeys);

      System.out.println("Paquete cifrado. Tamaño: " + onionEncryptedMessage.length + " bytes");
      System.out.println(new String(onionEncryptedMessage));
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

  // Método para cargar múltiples llaves públicas
  public static PublicKey[] loadMixPublicKyes(String [] filenames) throws Exception {
    PublicKey[] keys = new PublicKey[filenames.length];
    for (int i = 0; i < filenames.length; i++) {
      keys[i] = loadPublicKey(filenames[i]);
    }
    return keys;
  }

  public static byte[] onionEncrypt(byte[] data, PublicKey[] publicKeys) throws Exception {
    byte[] currentPacket = data;
    for (PublicKey key : publicKeys) {
      currentPacket = encrypt(currentPacket, key);
    }
    return currentPacket;
  }

  public static PublicKey loadPublicKey(String filename) throws Exception {
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    return keyFactory.generatePublic(keySpec);
  }

  public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
    Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBytes = cipher.doFinal(data);
    return encryptedBytes;
  }
}
