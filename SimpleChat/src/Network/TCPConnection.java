/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class TCPConnection {
    
    private final Socket socket;    
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnectionListener eventListener;
    
    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }
    
            
    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while(!rxThread.isInterrupted()) {
                        eventListener.onRecieveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        
        });
        rxThread.start();
    }

    public TCPConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void sendString (String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
    }
     
    public void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
        }
    }
    
    @Override
    public String toString () {
        return "TCPConnection: " + socket.getInetAddress() + " : " + socket.getPort();
    }
}
