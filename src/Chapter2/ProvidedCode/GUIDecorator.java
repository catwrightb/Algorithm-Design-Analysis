package Chapter2.ProvidedCode;
/**
   An example of a decorator for gui components
   Adapted from The Design Patterns Java Companion by Cooper
   @author Andrew Ensor
*/
import java.awt.BorderLayout;
import javax.swing.JComponent;

public class GUIDecorator extends JComponent
{
   protected JComponent component; // component to decorate
   
   public GUIDecorator(JComponent component)
   {  super();
      this.component = component;
      System.out.println(this.component);
      setLayout(new BorderLayout());
      add(component, BorderLayout.CENTER);
   }


}
