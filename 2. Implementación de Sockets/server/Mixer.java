package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Mixer implements Runnable {
  private final BlockingQueue <byte[]> messageQueue;
  private final int LOTE_SIZE;

  public Mixer(BlockingQueue<byte[]> messageQueue, int loteSize) {
    this.messageQueue = messageQueue;
    this.LOTE_SIZE = loteSize;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        List<byte[]> lote = new ArrayList<>(LOTE_SIZE);

        for (int i = 0; i < LOTE_SIZE; i++) {
          byte[] message = messageQueue.take();
          lote.add(message);
          System.out.println("Mensaje " + (i + 1) + " tomado de la cola para el lote.");
        }

        Collections.shuffle(lote);
        System.out.println("Lote mezclado:");

        // Enviar lote al siguiente mixnode.
        for (byte[] msg : lote) {
          System.out.println(new String(msg)); 
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Mixer interrumpido.");
        break;
      }
    }
  } 
}
