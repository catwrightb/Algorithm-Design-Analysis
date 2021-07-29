package Chapter2.ProvidedCode;
/**
   A class that represents a proxy image which is obtained via a URL,
   with a temporary image displayed until the actual image is loaded
   @see RemoteImage.java
*/
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ProxyImage extends RemoteImage
{
   private Component component;
   private ConcreteImage ci;
   private boolean imageAvailable;
   private ImageIcon tempIcon;
   
   // constructor for creating an image located at the specified
   // URL that will appear on specified component
   public ProxyImage(URL url, Component component)
   {  super(url);
      this.component = component;
      ci = null; // no concrete image yet loaded
      imageAvailable = false;
      // load the temporary image
      tempIcon = new ImageIcon(getClass().getResource("downloading.gif")); // local image
      // create a separate thread to get the desired image from URL
      Thread thread = new Thread(new ConcreteImageLoader());
      thread.start();
   }
   
   // returns the image, note that this implementation returns
   // immediately
   public Image getImage()
   {  if (imageAvailable)
         return ci.getImage();
      else
         return tempIcon.getImage();
   }
   
   // inner class that loads an image using ConcreteImage
   private class ConcreteImageLoader implements Runnable
   {
      public void run()
      {  ci = new ConcreteImage(getURL(), component);
         ci.getImage();
         imageAvailable = true;
         component.repaint(); // repaint component with loaded image
      }
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
      final RemoteImage remoteImage = new ProxyImage(url, panel);
      JPanel imagePanel = new JPanel()
         {  public void paintComponent(Graphics g)
            {  super.paintComponent(g);
               g.drawImage(remoteImage.getImage(), 0, 0, this);
            }
            
            public Dimension getPreferredSize()
            {  Image image = remoteImage.getImage();
               return new Dimension(image.getWidth(panel),
                  image.getHeight(panel));
            }
         };
      panel.add(imagePanel);
      JFrame frame = new JFrame("Proxy Image Demonstration");
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
