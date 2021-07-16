package Week1.code.Ex_1;

import java.util.ArrayList;
import java.util.List;

public class LamportsBakery extends Thread{

    private static volatile int number;
    private static volatile List<LamportsBakery> threads = new ArrayList();

    private boolean choosing; //boolean choosing flag
    private Thread activeThread; //the active thread
    private int thread_num; //the thread id

    public int num_threads = 10;



    public void acquireLock(){
        choosing = true;
        thread_num = number++;
        choosing = false;

        for (LamportsBakery thread:threads) {
            while (thread.choosing){

            }

        }
    }

    public void releaseLock(){

    }

}
