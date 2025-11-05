package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ClientHandler implements Runnable {
  private final Socket socket;
  private final BlockingQueue<byte[]> messageQueue;
  private final PrivateKey privateKey;
  private final int RSA_KEY_SIZE = 256;

  public ClientHandler(Socket socket, BlockingQueue<byte[]> messageQueue, PrivateKey privateKey) {
    this.socket = socket;  
    this.messageQueue = messageQueue;
    this.privateKey = privateKey;
  }

  @Override
  public void run() {
    try (
      socket;
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    ) {
      while(true) {
        try {
          int length = in.readInt();
          if(length > 0) {
            byte[] encryptPacket = new byte[length];
            in.readFully(encryptPacket, 0, encryptPacket.length);

            ByteBuffer buf = ByteBuffer.wrap(encryptPacket);

            byte[] decryptedData = removeLayer(buf, this.privateKey);

            ByteBuffer bufferCheck = ByteBuffer.wrap(decryptedData);
            if (bufferCheck.getInt() == RSA_KEY_SIZE) {
              System.out.println("Mensaje recibido y descifrado: " + Base64.getEncoder().encodeToString(decryptedData));
              messageQueue.put(decryptedData);
            } else {
              System.out.println("Última capa de cifrado eliminada");
              System.out.println("Mensaje final: " + new String(decryptedData, "UTF-8"));
            }
          }
        } catch (Exception e) {
          System.out.println("Fin del lote: Se han recibido todos los mensajes");
          break;
        }
      }
      out.writeUTF("Lote de datos reibicido y procesado correctamente.");
    } catch (Exception e) {
      System.err.println("Ha ocurrido un error en el ClientHandler: " + e.getMessage());
    }
  }

  public byte[] removeLayer(ByteBuffer buffer, PrivateKey privateKey) throws Exception {
    int keyLength = buffer.getInt();
    byte[] encryptedKey = new byte[keyLength];
    buffer.get(encryptedKey);

    byte[] iv = new byte[12];
    buffer.get(iv);

    byte[] encryptedData = new byte[buffer.remaining()];
    buffer.get(encryptedData);

    byte[] aesKeyBytes = CryptoUtils.decrypt(encryptedKey, privateKey);
    SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
    Arrays.fill(aesKeyBytes, (byte) 0);
    return CryptoUtils.aesGCMDecryption(encryptedData, aesKey, iv);
  }
}
