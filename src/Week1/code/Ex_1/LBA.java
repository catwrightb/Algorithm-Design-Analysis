package Week1.code.Ex_1;

import java.util.ArrayList;
import java.util.List;

public class LBA extends Thread{

    //arrayList of the threads
    private static volatile List<LBA> threadsArray = new ArrayList();

    //each thread has
    private volatile boolean choosing;
    private volatile int ticket;

    public int numberCounter;

    public static int countTotal = 0;
    public static int valueToIncrease = 0;
    public boolean running = true;


    public LBA(int ticket) {
        this.ticket = ticket;
        this.choosing = false;
    }


    @Override
    public void run() {
       while (running){
           acquireLock();
           critical(this);
           releaseLock();

       }
    }

    public void critical(LBA t){
        valueToIncrease++;
        countTotal++;
        System.out.println("Thread " + t.getName()+ ": increase count to "+valueToIncrease);
    }

    public void acquireLock(){
        this.choosing = true;
        this.ticket = numberCounter++;
        choosing = false;
        for (LBA s : threadsArray) {
            while (s.choosing) {
                s.yield();
            }
            while ((s.ticket != 0 && s.ticket < this.ticket)
                    || (s.ticket == this.ticket && s.getId() < this.getId())) {
                s.yield();
            }
        }

        System.out.println("Thread acquireLock: " + this.getName());
    }

    public void releaseLock(){
        this.ticket = 0;
        System.out.println("Thread releaseLock: " + this.getName());
    }


    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            LBA thread = new LBA(i);
            threadsArray.add(thread);
        }

        for (LBA thread:threadsArray) {
            thread.start();
        }

    }
}
