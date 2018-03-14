package Server;

import Network.TCPConnection;
import Network.TCPConnectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }
    
    private final ArrayList<TCPConnection> connections = new ArrayList<TCPConnection>();
    
    private Server () {
        System.out.println("Server Running");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onRecieveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client Disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }
    
    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0 ; i < cnt ; i ++) {
            connections.get(i).sendString(value);
        }
    }
}
