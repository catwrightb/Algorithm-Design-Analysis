package Week1.code.Ex_1;

import java.util.ArrayList;
import java.util.List;

public class LamportsBakeryAlgorithm {
    private static volatile int number;
    private static volatile List<LamportsBakeryAlgorithm> threads = new ArrayList();

    private volatile boolean choosing;
    private Thread activeThread;
    private int threadNumber;

    public LamportsBakeryAlgorithm() {
        activeThread = new Thread(this::run);
    }

    public void run() {
        while (true) {
            acquireLock();
           // System.out.println("Running: " + activeThread.getName());
            releaseLock();
        }
    }

    public void acquireLock() {
        choosing = true;
        threadNumber = ++number;
        System.out.println(threadNumber);
        choosing = false;
        for (LamportsBakeryAlgorithm thread : threads) {
            while (thread.choosing) {
                activeThread.yield();
            }
            while ((thread.threadNumber != 0 && thread.threadNumber < this.threadNumber)
                    || (thread.threadNumber == this.threadNumber && thread.activeThread.getId() < this.activeThread.getId())) {
                activeThread.yield();
            }
        }
        //System.out.println("Acquired Lock: " + activeThread.getName());
    }

    public void releaseLock() {
        threadNumber = 0;
       // System.out.println("Released Lock: " + activeThread.getName());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            LamportsBakeryAlgorithm thread = new LamportsBakeryAlgorithm();
            threads.add(thread);
        }
        for (LamportsBakeryAlgorithm thread : threads) {
            thread.activeThread.start();
        }
    }
}
