/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 *
 * @author User
 */
public interface TCPConnectionListener {
    
    void onConnectionReady(TCPConnection tcpConnection);
    
    void onRecieveString(TCPConnection tcpConnection, String value);
    
    void onDisconnect(TCPConnection tcpConnection);
    
    void onException(TCPConnection tcpConnection, Exception e);
     
}
