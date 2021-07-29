package Chapter2.ProvidedCode;
/**
   Decorator for any JComponent to change the foreground colour
   whenever the mouse passes over the component
   @see GUIDecorator.java
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PassOverDecorator extends GUIDecorator
{
   private Color defaultForeground, mouseOverForeground;

   public PassOverDecorator(final JComponent component)
   {
      super(component);
      defaultForeground = component.getForeground();
      mouseOverForeground = new Color(255-defaultForeground.getRed(),
         defaultForeground.getGreen(), defaultForeground.getBlue());
      component.addMouseListener(new MouseAdapter()
         {  public void mouseEntered(MouseEvent e)
            {  component.setForeground(mouseOverForeground);
            }
            
            public void mouseExited(MouseEvent e)
            {  component.setForeground(defaultForeground);
            }
         });
   }
   
   public static void main(String[] args)
   {  // prepare a panel containing some test gui components
      JComponent label
         = new PassOverDecorator(new JLabel("Test Label"));
      JComponent button
         = new PassOverDecorator(new JButton("Test Button"));
      JComponent textField
         = new PassOverDecorator(new JTextField("Test Field"));
      JComponent checkbox
              = new PassOverDecorator(new JCheckBox("Check Box"));
      JPanel container = new JPanel();

      container.add(label);
      container.add(button);
      container.add(textField);
      container.add(checkbox);
      JFrame frame = new JFrame("Decorator Demonstration");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(container);
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
