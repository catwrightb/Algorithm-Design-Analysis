package Chapter1.code.Ex_2;

public class Swappable_AvoidingDeadlock<E> {
    private E element;

    public Swappable_AvoidingDeadlock(E element)
    {  this.element = element;
    }

    public synchronized E getElement()
    {  return element;
    }

    public synchronized void setElement(E element)
    {  this.element = element;
    }

    //the issue is two threads acquiring monitors in different orders
    //we need threads to acquire monitors in the same order each time
    public void swap(Swappable_AvoidingDeadlock<E> other)
    {
        //we need synchrohized on the code instead of the method
        //check which hashCode is smaller between threads
        // if A is smaller then B
        if (this.hashCode() < other.hashCode()){
            synchronized(this){
                E temp = element;
                element = other.getElement();
                other.setElement(temp);
                // put Thread.sleep(1) here to make deadlock obvious
            }
        }
        else {
            //if the larger thread checks then the threads will swap
            other.swap(this);
        }

    }

    public static void main(String[] args)
    {  final Swappable_AvoidingDeadlock<String> a = new Swappable_AvoidingDeadlock<String>("A");
        final Swappable_AvoidingDeadlock<String> b = new Swappable_AvoidingDeadlock<String>("B");
        Thread t1 = new Thread(new Runnable()
        {  public void run()
            {  while(true)
                {  System.out.println("Swapping a with b");
                    a.swap(b);
                }
            }
        });
        Thread t2 = new Thread(new Runnable()
        {  public void run()
            {  while(true)
                {  System.out.println("Swapping b with a");
                    b.swap(a);
                }
            }
        });
        t1.start();
        t2.start();
    }
}
