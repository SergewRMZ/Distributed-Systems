package server;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Mixer implements Runnable {
  private final BlockingQueue <byte[]> messageQueue;
  private final int LOTE_SIZE;
  private final String nextMixHost;
  private final int nextMixPort;

  public Mixer(BlockingQueue<byte[]> messageQueue, int loteSize, String nextMixHost, int nextMixPort) {
    this.messageQueue = messageQueue;
    this.LOTE_SIZE = loteSize;
    this.nextMixHost = nextMixHost;
    this.nextMixPort = nextMixPort;
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
        System.out.println("Lote mezclado correctamente, enviar al siguiente mixnode.");

        try (
          Socket outputSocket = new Socket(nextMixHost, nextMixPort);
          DataOutputStream out = new DataOutputStream(outputSocket.getOutputStream());
        ) {
          for(byte[] msg : lote) {
            out.writeInt(msg.length);
            out.write(msg);
          }
          out.flush();
          System.out.println("Lote enviado al siguiente MixNode en " + nextMixHost + ":" + nextMixPort);
        } catch (Exception e) {
          System.err.println("Error al conectar con el siguiente MixNode (" + nextMixHost + ":" + nextMixPort + "): " + e.getMessage());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Mixer interrumpido.");
        break;
      }
    }
  } 
}
