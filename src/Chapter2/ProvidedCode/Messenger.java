package Chapter2.ProvidedCode;
/**
   A class that represents a Messenger which is passed an object of
   type E (by a Transmitter) and holds it until it is requested (by a
   Receiver). Note that further attempts by a thread to give the
   messenger an object while it holds one are made to wait, and
   attempts by a thread to obtain the object while the messenger does
   not hold one are made to wait.
   @author Andrew Ensor
*/

public class Messenger<E>
{
   private E item;
   private boolean empty; // whether messenger holds an item
   private boolean accepting; // whether messenger allows objects
   
   public Messenger()
   {  item = null;
      empty = true;
      accepting = true;
   }
   
   public synchronized void put(E item)
   {  if (!accepting)
         throw new IllegalStateException("Messenger not accepting");
      // make the current thread (ie Transmitter) wait until the
      // previous item has been requested
      while (!empty)
      {  try
         {  wait();
         }
         catch (InterruptedException e)
         {}
      }
      this.item = item;
      empty = false;
      // notify all other threads (ie Receiver) of the change in the
      // status of the messenger
      notifyAll();
   }
   
   public synchronized E get()
   {  // make the current thread (ie Receiver) wait until the
      // messenger holds an item
      while (empty && accepting)
      {  try
         {  wait();
         }
         catch (InterruptedException e)
         {}
      }
      if (empty)
         return null; // no item to return
      empty = true;
      // notify all other threads (ie Transmitter) of the change in the
      // status of the messenger
      notifyAll();
      return item;
   }
   
   public boolean hasItem()
   {  return !empty;
   }
   
   public boolean isAccepting()
   {  return accepting;
   }
   
   public synchronized void stopAccepting()
   {  accepting = false;
      System.out.println("Messenger requested to stop accepting");
      notifyAll();
   }
}
