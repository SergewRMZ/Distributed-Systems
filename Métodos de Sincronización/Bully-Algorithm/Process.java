import java.util.List;

public class Process {
  private final int id;
  private boolean isActive;
  private volatile boolean isCoordinator;
  private final List<Process> processes;

  public Process(int id, List<Process> processes) {
    this.id = id;
    this.isActive = true;
    this.processes = processes;
    this.isCoordinator = false;
  }

  public int getId() {
    return id;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  /**
   * Método para iniciar una elección.
   */
  public synchronized void holdElection() {

    // Esta parte evita que un proceso que ya es coordinador inicie de nuevo una elección.
    if(this.isCoordinator) {
      System.out.println(ConsoleColors.YELLOW + "Proceso " + id + " ya es el coordinador. No puede iniciar una elección." + ConsoleColors.RESET);
      return;
    }

    // Si el proceso no está activo, no puede iniciar una elección.
    this.isCoordinator  = false;


    System.out.println("Proceso " + id + " inicia una elección.");
    boolean receivedHigherProcess = false;

    for (Process p: processes) {
      if(p.getId() > this.id && p.isActive()) {
        System.out.println(ConsoleColors.BLUE + "Proceso " + id + " envía mensaje de elección a Proceso " + p.getId() + ConsoleColors.RESET);
        
        // Si el proceso responde, entonces hay un proceso con ID más alto activo
        if(p.respondToElection()) {
          receivedHigherProcess = true;
        }
      }
    }

    if(!receivedHigherProcess) {
      System.out.println("Process " + id + " se convierte en el coordinador. Porque no recibió respuestas de procesos con ID más alto.");
      announceVictory();
    } else {
      System.out.println(ConsoleColors.YELLOW + "Process " + id + " no se convierte en el coordinador. Se retira" + ConsoleColors.RESET);
    }
  }

  /**
   * Método para responder a un mensaje de elección que proviene de un proceso con ID más bajo.
   * @return
   */
  public boolean respondToElection() {
    if(!this.isActive) {
      return false;
    }

    if(this.isCoordinator) {
      System.out.println(ConsoleColors.YELLOW + "Process " + id + " responde que ya es el coordinador a un mensaje que llegó tarde" + ConsoleColors.RESET);
      return true;
    }

    System.out.println(ConsoleColors.MAGENTA + "Process " + id + " responde al mensaje de elección. Comenzará un proceso de elección" + ConsoleColors.RESET);
    new Thread(this::holdElection).start();
    return true;
  }

  /**
   * Método para anunciar que este proceso es el nuevo coordinador.
   */
  public void announceVictory() {
    this.isCoordinator = true;

    System.out.println(ConsoleColors.GREEN_BOLD + "PROCESO " + id + " ANUNCIA QUE ES EL NUEVO COORDINADOR." + ConsoleColors.RESET);

    for (Process p: processes) {
      if(p.getId() != this.id && p.isActive()) {
        System.out.println("Process " + id + " envía mensaje de COORDINATOR a Proceso " + p.getId());
      }
    }
  }
}