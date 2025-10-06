package server;

public class App {
  public static void main(String[] args) {
    try {
      Server server = Server.getInstance();
      server.init();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
