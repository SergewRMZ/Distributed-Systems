class Database {
  public static final String RESET = "\u001B[0m";
  public static final String BLUE = "\u001B[34m";  
  public static final String RED = "\u001B[31m";

  private int readers = 0;
  private boolean writing = false;
  private int waitingWriters = 0;

  public synchronized void startRead(String name) throws InterruptedException {
    if(writing || waitingWriters > 0) System.out.println(BLUE + name + " is waiting to read.\tLOGIC STATE: WAITING" + RESET);

    while (writing || waitingWriters > 0) {
      wait();
    }
    readers++;
    System.out.println(BLUE + name + " started reading. Readers: " + readers + "\tLOGIC STATE: RUNNING" + RESET);
  }

  public synchronized void endRead(String name) {
    readers--;
    System.out.println(BLUE + name + " finished reading. Readers: " + readers + "\tLOGIC STATE: TERMINATED" + RESET);
    if (readers == 0) {
      notifyAll();
    }
  }

  public synchronized void startWrite(String name) throws InterruptedException {
    waitingWriters++;
    if(readers > 0 || writing) System.out.println(RED + name + " is waiting to write. \tLOGIC STATE: WAITING" + RESET);
    while (readers > 0 || writing) {
      wait();
    }

    waitingWriters--;
    writing = true;
    System.out.println(RED + name + " started writing.\tLOGIC STATE: RUNNING" + RESET);
  }

  public synchronized void endWrite(String name) {
    writing = false;
    System.out.println(RED + name + " finished writing.\tLOGIC STATE: TERMINATED" + RESET);
    notifyAll();
  }
}