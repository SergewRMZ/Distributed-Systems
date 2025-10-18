package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MixNode {
  public int port;
  private static final int LOTE_SIZE = 5;
  private ServerSocket serverSocket;
  private BlockingQueue<byte[]> messageQueue;
  private PrivateKey privateKey;
  private final int nextMixPort;
  private final String nextMixHost;
  public MixNode (int port, String privateKeyPath, String nextMixHost, int nextMixPort) {
    this.port = port;
    this.messageQueue = new LinkedBlockingQueue<>(LOTE_SIZE);
    this.nextMixHost = nextMixHost;
    this.nextMixPort = nextMixPort;

    try {
      this.privateKey = CryptoUtils.loadPrivateKey(privateKeyPath);
    } catch (Exception e) {
      System.out.println("Error al iniciar el servidor: " + e.getMessage());
    } 
  }

  public void init() {
    try {
      this.serverSocket = new ServerSocket(port);
      System.out.println("Servidor iniciado en el puerto " + port + ". Esperando conexiones..."); 
    } catch (Exception e) {
      System.err.println("Error al iniciar el servidor: " + e.getMessage());
      return;
    }

    Thread mixerThread = new Thread(new Mixer(messageQueue, LOTE_SIZE, nextMixHost, nextMixPort));
    mixerThread.start();

    while(true) {
      try {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

        Thread clientHandler = new Thread(new ClientHandler(clientSocket, messageQueue, privateKey));
        clientHandler.start();
      } catch (Exception e) {
        System.out.println("Error al aceptar la conexión del cliente: " + e.getMessage());
      }
    }
  }
}
