package Chapter1.code.Ex_3;


import sun.jvm.hotspot.runtime.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//for assignment need two ports
//so we need two server sockets
//and three threads listening

public class ChatServer implements Runnable{

    //stop the client connection
    private boolean stopRequested;
    //port number for socket
    public static final int INPUT_PORT = 7777;
    public static final int OUTPUT_PORT = 8888;

    protected ServerSocket serverSocket1 = null;
    protected ServerSocket serverSocket2 = null;

    public List<ClientConnection> listClientConnection = new ArrayList<ClientConnection>();
    public Thread testThread;

    public ChatServer()
    {
        stopRequested = false;
    }

    //starts the server
    //only connects client to host
    public void startServer ()
    {
        //start the server or receive a error upon start up
        stopRequested = false;


        try
        {
            serverSocket1 = new ServerSocket(INPUT_PORT);

            serverSocket2 = new ServerSocket(OUTPUT_PORT);
            System.out.println("Server to accept clients started at "
                    + InetAddress.getLocalHost() + " on port " + serverSocket1.getLocalPort());
            System.out.println("Server to sent processed task to clients started at "
                    + InetAddress.getLocalHost() + " on port " + serverSocket2.getLocalPort());
        }
        catch (IOException e)
        {
            System.err.println("Server can't listen on port: " + e);
            System.exit(-1);
        }


    }

    //method to broadcast all chat client messages to other clients
    //called from the chatConnection object below
    public void broadcastMessage(String message){
        for (ClientConnection chat: listClientConnection) {
            chat.sendMessage(message);
        }

    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer();
        Thread t1 = new Thread(server);
        t1.start();
    }

    @Override
    public void run() {
        //while stopRequested in false continue in the loop keeping the server running
        try
        {
            while (!stopRequested) {
                Socket socket1 = serverSocket1.accept(); //->can set a time out parameter here in desired
                Socket socket2 = serverSocket2.accept();

                //confirms that connection has been made
                System.out.println("Connection made with " + socket1.getInetAddress() + " on port " +socket1.getLocalPort());
                System.out.println("Connection made with " + socket2.getInetAddress()+" on port " +socket2.getLocalPort());

                // start a clientConnect instance with this connection
                ClientConnection clientConnection = new ClientConnection(socket1, socket2);
                //add the new connection to the list
                listClientConnection.add(clientConnection);
                //start new thread for the client connection
                Thread thread = new Thread(clientConnection);
                thread.start();

            }
            //when stopRequest is true closing server
            serverSocket1.close();
            serverSocket2.close();
        } catch (IOException e) {
            System.err.println("Can't accept client connection: " + e);
        }
        System.out.println("Server finishing");
    }



    // inner class that represents a single chat client across a socket
    private class ClientConnection implements Runnable
    {
        //value to end connection which must be typed by client and recognized by by the server
        private String value = "QUIT";
        private Socket socket; // socket for client/server communication
        private Socket socket2;
        PrintWriter pw; // output stream to client
        BufferedReader br; // input stream from client


        private Boolean closeChat = false; //close chat boolean

        public ClientConnection(Socket socket1, Socket socket2)
        {
            try{
                //this.socket = socket1;
                pw = new PrintWriter(socket2.getOutputStream(), true); //-> true set autoFlush to occur
                br = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                pw.println("You are connected to the chat group! Type QUIT to exit.");
            }catch (IOException e){
                System.out.println(e);
            }

        }


        public void run()
        {

            try
            {

                do
                {
                    String clientRequest = br.readLine(); //-> if encountering block no response could be occurring here
                    System.out.println(clientRequest);

                    if (!clientRequest.equals(value)) {

                        broadcastMessage(clientRequest);

                    }
                    else {
                        closeChat = true;
                    }

                }
                while (!closeChat);
                pw.close();
                br.close();
                System.out.println("Closing connection with " + socket.getInetAddress());
                socket.close();
            }
            catch (IOException e)
            {  System.err.println("Server error: " + e);
            }
        }

        //method to sendMessages/write to all clients from the input
        //called from the Broadcast message method
        public void sendMessage(String message){
            pw.println(message);

        }
    }
}