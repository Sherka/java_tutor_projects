/**
 * Created by User on 24.03.2017.
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {

    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {

                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void run() {
            String msg;
            try{
                while ((msg = reader.readLine()) != null) {
                    System.out.println("read " + msg);
                    tellEveryone(msg);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main (String[] args) {
        new VerySimpleChatServer().go();
    }

    public void go () {
        clientOutputStreams = new ArrayList();

        try{

            ServerSocket serverSocket = new ServerSocket(5000);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("Есть подключение");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tellEveryone (String msg) {

        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {

                PrintWriter writer = (PrintWriter) it.next();
                writer.println(msg);
                writer.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
