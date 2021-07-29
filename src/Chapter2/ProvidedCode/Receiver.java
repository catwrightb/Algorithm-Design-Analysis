package Chapter2.ProvidedCode;
/**
   A class that represents a Receiver of objects of type E that 
   are sent from the Transmitter (operating in a separate thread) one
   item at a time via a Messenger
   @author Andrew Ensor
*/
import java.util.Collection;
import java.util.Random;

public class Receiver<E> implements Runnable
{
   private Messenger<E> messenger;
   
   public Receiver(Messenger<E> messenger)
   {  this.messenger = messenger;
   }
   
   public void run()
   {  // receive information from the messenger
      while (messenger.isAccepting() || messenger.hasItem())
      {  E item = messenger.get(); // waits until item has been gotten
         if (messenger.isAccepting())
            System.out.println("Receiver has gotten " + item
               + " from the messenger");
      }
      System.out.println("Receiver stopping");
   }
}
