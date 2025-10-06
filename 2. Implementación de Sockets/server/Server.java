package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static final int SERVER_PORT = 8000;
  private static Server instance;
  private ServerSocket serverSocket;

  private Server () {
    try {
      serverSocket = new ServerSocket(SERVER_PORT);
      System.out.println("Servidor iniciado en el puerto " + SERVER_PORT + ". Esperando conexiones..."); 
    } catch (Exception e) {
      System.out.println("Error al iniciar el servidor: " + e.getMessage());
    } 
  }

  public static Server getInstance () {
    if(instance == null) {
      instance = new Server();
    }

    return instance;
  }


  public void init() {
    while(true) {
      try {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

        Thread clientHandler = new Thread(new ClientHandler(clientSocket));
        clientHandler.start();
      } catch (Exception e) {
        System.out.println("Error al aceptar la conexión del cliente: " + e.getMessage());
      }
    }
  }
}
