package Chapter2.ProvidedCode;
/**
   An abstract class that represents an image which is
   obtained via a URL, as a demonstration of the Proxy Pattern
   @author Andrew Ensor
*/
import java.awt.Image;
import java.net.URL;

public abstract class RemoteImage
{
   private URL url;
   
   public RemoteImage(URL url)
   {  this.url = url;
   }
   
   public URL getURL()
   {  return url;
   }
   
   public abstract Image getImage();
}
