import java.util.ArrayList;
import java.util.List;

public class App {
  public static void main(String[] args) throws InterruptedException {
    List<Process> processes = new ArrayList<>();
    int numberOfProcesses = 7;

    for (int i = 1; i <= numberOfProcesses; i++) {
      processes.add(new Process(i, processes));
    }

    System.out.println(numberOfProcesses + " procesos creados. El líder inicial es el proceso " + numberOfProcesses);

    Process leader = processes.get(numberOfProcesses - 1);
    System.out.println(ConsoleColors.RED + "¡¡¡Proceso " + leader.getId() + " ha fallado!!!." + ConsoleColors.RESET);
    leader.setActive(false);

    System.out.println(ConsoleColors.YELLOW_BOLD + "Proceso 3 detecta la falla y comienza la elección." + ConsoleColors.RESET);
    processes.get(2).holdElection();

    Thread.sleep(3000);
    System.out.println("Simulación finalizada.");
  }
}
