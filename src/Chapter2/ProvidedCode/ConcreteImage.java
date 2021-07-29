package Chapter2.ProvidedCode;
/**
   A class that represents a ConcreteImage which is obtained via a URL,
   and whose getImage method blocks until the image is loaded
   @see RemoteImage.java
*/

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConcreteImage extends RemoteImage
{
   private Image image;
   private Component component;
   
   // constructor for creating an image located at the specified
   // URL that will appear on specified component
   public ConcreteImage(URL url, Component component)
   {  super(url);
      image = null; // image is not yet loaded
      this.component = component;
      // set the proxy host and port number for AUT's firewall
      // note this is not needed if no firewall present
      Properties props = new Properties(System.getProperties());
      props.put("http.proxyHost", "cache.aut.ac.nz"); // AUT specific
      props.put("http.proxyPort", "3128"); // AUT specific
      props.put("https.proxyHost", "cache.aut.ac.nz"); // AUT specific
      props.put("https.proxyPort", "3128"); // AUT specific
      System.setProperties(props);
   }
   
   // returns the image, note that this implementation blocks
   // until the image is loaded
   public Image getImage()
   {  if (image == null)
      {  image = Toolkit.getDefaultToolkit().getImage(getURL());
         // wait until the image has been loaded
         MediaTracker tracker = new MediaTracker(component);
         tracker.addImage(image, 0); // id 0 assigned to the image
         try
         {  tracker.waitForID(0);
         }
         catch (InterruptedException e)
         {}
      }
      return image;
   }
   
   // driver main method to test the class
   public static void main(String[] args)
   {  URL url = null;
      try
      {  url = new URL("https://upload.wikimedia.org/wikipedia/en/0/0d/Simpsons_FamilyPicture.png");
      }
      catch (MalformedURLException e)
      {  System.err.println("Malformed URL: " + e);
      }
      final JPanel panel = new JPanel();
      final RemoteImage remoteImage = new ConcreteImage(url, panel);
      JPanel imagePanel = new JPanel()
         {  public void paintComponent(Graphics g)
            {  super.paintComponent(g);
               g.drawImage(remoteImage.getImage(), 0, 0, this);
            }
            
            public Dimension getPreferredSize()
            {  Image image = remoteImage.getImage();
               return new Dimension(image.getWidth(panel), image.getHeight(panel));
            }
         };
      panel.add(imagePanel);
      JFrame frame = new JFrame("Concrete Image Demonstration");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(panel);
      frame.pack();
      // position the frame in the middle of the screen
      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension screenDimension = tk.getScreenSize();
      Dimension frameDimension = frame.getSize();
      frame.setLocation((screenDimension.width-frameDimension.width)/2,
         (screenDimension.height-frameDimension.height)/2);
      frame.setVisible(true);
   }   
}
