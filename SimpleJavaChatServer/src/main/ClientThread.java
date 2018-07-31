package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ClientThread extends Thread{

    private final static long DELAY = 30000L;

    private Socket socket;
    private Message c;
    private String login;
    private int inPacks;
    private int outPacks;
    private boolean flag;
    private Timer timer;

    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }

    public void run () {
        try {
            //Создаем потоки ввода/вывода для работы с сокетом
            final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            //Читаем main.Message из чата
            this.c = (Message) objectInputStream.readObject();

            //Читаем логин отправителя
            this.login = this.c.getLogin();

            //Смотрим входящее сообщение
            if(!this.c.getMessage().equals(Config.HELLO_MESSAGE)) { //Если это не приветственное сообщение
                System.out.println("[" + this.c.getLogin() + "]: " + this.c.getMessage());
                Server.getChatHistory().addMessage(this.c); //Добавляем его в историю чата
            } else {
                objectOutputStream.writeObject(Server.getChatHistory()); //Отправляем новичку историю чата
                this.broadcast(Server.getUserList().getClientList(), new Message("main.Server-bot", "The user " + login + " has been connected."));
            }
            //Добавляем пользователя в общий список
            Server.getUserList().addUser(login, socket, objectInputStream, objectOutputStream);

            //Для ответа, указываем список доступных пользователей
            this.c.setOnlineUsers(Server.getUserList().getUsers());

            //Передаем сообщение всем пользователям
            this.broadcast(Server.getUserList().getClientList(), this.c);

            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    try {
                        //Если кол-во входящих пакетов от клиента равно исходящему, значит клиент еще жив
                        if(inPacks == outPacks) {
                            objectOutputStream.writeObject(new Ping());
                            outPacks++;
                            System.out.println(outPacks + " out");
                        } else {
                            //Иначе в ауте
                            throw new SocketException();
                        }
                    } catch (SocketException ex1) {
                        System.out.println("Packages not clash");
                        System.out.println(login + " disconnected!");
                        //Удалим клиента из списка доступных клиентов и отправляем сообщение об этом
                        Server.getUserList().deleteUser(login);
                        broadcast(Server.getUserList().getClientList(), new Message("main.Server-bot", "User " + login + " was disconnected!"));
                        flag = true;
                        cancel();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                    }
                }
            };

            //Запускаем таймер
            this.timer = new Timer();
            this.timer.schedule(tt, DELAY);

            //Начинаем пинговать клиента
            objectOutputStream.writeObject(new Ping());
            this.outPacks++;
            System.out.println(outPacks + " out");

            //Ожидаем новых сообщений
            while(true) {
                if(this.flag) {
                    this.flag = false;
                    break;
                }
                //Принимаем сообщение
                this.c = (Message) objectInputStream.readObject();

                //Если это пинг
                if(this.c instanceof Ping) {
                    this.inPacks++;
                    System.out.println(this.inPacks + " in");
                } else if (!c.getMessage().equals(Config.HELLO_MESSAGE)) { //Если это не сообщение приветствия
                    System.out.println("[" + login + "]: " + c.getMessage());
                    Server.getChatHistory().addMessage(this.c);
                } else {
                    objectOutputStream.writeObject(Server.getChatHistory());
                    this.broadcast(Server.getUserList().getClientList(), new Message("main.Server-bot", "User " + login + "was connected!"));
                }

                this.c.setOnlineUsers(Server.getUserList().getUsers());

                if(!(c instanceof Ping) && !c.getMessage().equals(Config.HELLO_MESSAGE)) {
                    System.out.println("Send Broadcast main.Message: \"" + c.getMessage() + "\"");
                    this.broadcast(Server.getUserList().getClientList(), this.c);
                }

            }

        } catch (SocketException socketE) {
            System.out.println(login + " disconnected");
            this.broadcast(Server.getUserList().getClientList(), new Message("main.Server-Bot", "User " + login + " was disconnected"));
            this.timer.cancel();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void broadcast (ArrayList<Client> clientArrayList, Message message) {
        try {
            for (Client client : clientArrayList) {
                client.getThisObjectOutputStream().writeObject(message);
            }
        } catch (SocketException socketEx) {
            System.out.println("in broadcast: " + login + " was disconnected");
            Server.getUserList().deleteUser(login);
            this.broadcast(Server.getUserList().getClientList(), new Message("System", "User " + login + " was disconnected"));
            timer.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
