package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static UsersList list = new UsersList();
    private static ChatHistory chatHistory = new ChatHistory();

    public static void main(String[] args) {

        try {
            //Создаем слушатель
            ServerSocket serverSocket = new ServerSocket(Config.PORT);

            System.out.println("Server started:");
            while(true) {
                Socket client = null;
                while(client == null) {
                    client = serverSocket.accept();
                }
                //Создаем новый пооток которому передаем сокет
                new ClientThread(client);
            }

        }
        catch (SocketException socketException) {
            System.err.print("Socket Exception");
            socketException.printStackTrace();
        }
        catch (IOException ioException) {
            System.err.print("I/O Exception");
            ioException.printStackTrace();
        }
    }

    public synchronized static UsersList getUserList () {
        return list;
    }

    public synchronized static ChatHistory getChatHistory () {
        return chatHistory;
    }
}
