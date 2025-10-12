package server;

public class App {
  public static void main(String[] args) {
    try {
      MixNode server = new MixNode(8000, "privateKey_Server_A");
      server.init();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
