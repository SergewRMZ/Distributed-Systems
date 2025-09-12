public class Writer implements Runnable {
  private Database db;
  private String name;

  public Writer(Database database, String name) {
    System.out.println("LOGIC STATE: CREATED " + name);
    this.db = database;
    this.name = name;
  }

  @Override
  public void run() {
    try {
      db.startWrite(name);
      Thread.sleep((long) (Math.random() * 2000));
      db.endWrite(name);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
}
