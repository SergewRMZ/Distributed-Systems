package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
  private final Socket socket;
  private final BlockingQueue<byte[]> messageQueue;
  private final PrivateKey privateKey;

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
      int length = in.readInt();
      if(length > 0) {
        byte[] encryptPacket = new byte[length];
        in.readFully(encryptPacket, 0, encryptPacket.length);

        byte[] decryptedData = CryptoUtils.decrypt(encryptPacket, this.privateKey);

        messageQueue.put(decryptedData);
        System.out.println("Mensaje recibido y descifrado: " + new String(decryptedData));
        out.writeUTF("El paquete de datos se ha recibido y descifrado correctamente.");
      }
    } catch (Exception e) {
      System.err.println("Ha ocurrido un error en el ClientHandler: " + e.getMessage());
    }
  }
}
