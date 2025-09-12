public class App {
  public static void main(String[] args) {
    System.out.println("Multiprocesamiento con Hilos en Java");
    System.out.println("Nombre del alumno: Serge Eduardo Martínez Ramírez");
    System.out.println("----------------------------------------");

    Database db = new Database();

    Thread r1 = new Thread(new Reader(db, "Reader 1"));
    Thread r2 = new Thread(new Reader(db, "Reader 2"));
    Thread r3 = new Thread(new Reader(db, "Reader 3"));
    Thread r4 = new Thread(new Reader(db, "Reader 4"));
    Thread r5 = new Thread(new Reader(db, "Reader 5"));

    Thread w1 = new Thread(new Writer(db, "Writer 1"));
    Thread w2 = new Thread(new Writer(db, "Writer 2"));
    Thread w3 = new Thread(new Writer(db, "Writer 3"));

    r1.start();
    r2.start();    
    r3.start();
    r4.start();
    r5.start();
    w1.start();
    w2.start();
    w3.start();
  }
}
