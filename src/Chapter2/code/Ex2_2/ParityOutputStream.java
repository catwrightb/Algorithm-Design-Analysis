package Chapter2.code.Ex2_2;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class ParityOutputStream extends FilterOutputStream {

   public Queue<Boolean> queue = new LinkedBlockingQueue<Boolean>();
   public Boolean paritySet = false;
   protected OutputStream outputStream;

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public ParityOutputStream(OutputStream out) {
        super(out);
        this.outputStream = out;
    }

    /*
    * How to enqueue bits of byte value starting at most significant bit
    * */
    @Override
    public void write(int byteNum){
        int value = byteNum; // some byte value
        for (int i=0; i<8; i++)
        {
            boolean nextBit = (value & 0x80)!=0;
            queue.add(nextBit);
            value = value<<1; // shift bits one left
            System.out.println("Next bit is " + nextBit);
        }

        do {
            int assembledByte = 0;
            for (int i=0; i<7; i++)
            {
                boolean nextBit = queue.poll();
                if (nextBit){
                    paritySet = !paritySet;
                }

                System.out.println("assembled bit "+nextBit);
                assembledByte = (assembledByte<<1)
                        + (nextBit?1:0);
            }
            if (paritySet) {
                assembledByte = assembledByte | 0x80;
            }
            System.out.println("Assembled byte is " + assembledByte);
        }while (queue.size() >= 7);

    }

    public static void main(String[] args) throws IOException {

        FilterOutputStream test =  new ParityOutputStream(System.out);
        test.write(107);
    }

}
