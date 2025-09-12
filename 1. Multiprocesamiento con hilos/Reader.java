public class Reader implements Runnable {
  private Database db;
  private String name;

  public Reader(Database database, String name) {
     System.out.println("LOGIC STATE: CREATED " + name);
    this.db = database;
    this.name = name;
  }

  @Override
  public void run() {
    try {
      db.startRead(name);
      Thread.sleep((long) (Math.random() * 2000));
      db.endRead(name);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
