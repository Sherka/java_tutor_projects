package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersList {

    private Map<String, Client> onlineUsers = new HashMap<>();

    public void addUser (String login, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        System.out.println(login + " connected");

        if (!this.onlineUsers.containsKey(login)) {
            this.onlineUsers.put(login, new Client(socket, oos, ois));
        } else {
            int i = 1;
            while(this.onlineUsers.containsKey(login)) {
                login = login + i;
                i++;
            }
            this.onlineUsers.put(login, new Client(socket, oos, ois));
        }
    }

    public void deleteUser (String login) { this.onlineUsers.remove(login); }

    public String[] getUsers () { return this.onlineUsers.keySet().toArray(new String[0]); }

    public ArrayList<Client> getClientList () {
        ArrayList<Client> clientList = new ArrayList<Client>(this.onlineUsers.entrySet().size());

        String s = "";
        for(Map.Entry<String, Client> entry : this.onlineUsers.entrySet()) {
            clientList.add(entry.getValue());
            System.out.println(entry.getKey());
            s = s + entry.getKey();
        }

        return clientList;
    }
}
