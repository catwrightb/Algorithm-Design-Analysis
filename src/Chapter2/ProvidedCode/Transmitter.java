package Chapter2.ProvidedCode;
/**
   A class that represents a Transmitter of objects of type E that 
   are sent to the Receiver (operating in a separate thread) one
   item at a time via a Messenger
   @author Andrew Ensor
*/
import java.util.Collection;
import java.util.Random;

public class Transmitter<E> implements Runnable
{
   private Messenger<E> messenger;
   private Collection<E> information;
   private Random generator;
   
   public Transmitter(Messenger<E> messenger,
      Collection<E> information)
   {  this.messenger = messenger;
      this.information = information;
      generator = new Random();
   }
   
   public void run()
   {  // transmit information to the messenger with a random delay
      // between each transmission
      for (E item : information)
      {  System.out.println("Transmitter about to pass " + item
            + " to the messenger");
         messenger.put(item); // waits until item has been put
         try // sleep for up to 500ms
         {  Thread.sleep(generator.nextInt(500));
         }
         catch (InterruptedException e)
         {}
      }
      messenger.stopAccepting();
      System.out.println("Transmitter stopping");
   }
}
