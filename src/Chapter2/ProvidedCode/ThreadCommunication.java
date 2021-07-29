package Chapter2.ProvidedCode;

/**
   A class which demonstrate communication between two threads
   one created from a Transmitter and the other from a Receiver
   which communicate via a Messenger object by observing its
   thread notifications
   @author Andrew Ensor
*/
import java.util.ArrayList;
import java.util.Collection;

public class ThreadCommunication
{
   public static void main(String[] args)
   {  Messenger<String> messenger = new Messenger<String>();
      String[] items = {"Proceed", "Solinus", "to", "procure",
         "my", "fall", "And", "by", "the", "doom", "of", "death",
         "end", "woes", "and", "all"};
      Collection<String> information = new ArrayList<String>();
      for (String item : items)
         information.add(item);
      Transmitter<String> transmitter = new Transmitter<String>
         (messenger, information);
      Receiver<String> receiver = new Receiver<String>(messenger);
      Thread transmitterThread = new Thread(transmitter);
      Thread receiverThread = new Thread(receiver);
      transmitterThread.start();
      receiverThread.start();
   }
}
