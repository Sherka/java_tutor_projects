package main;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Message implements Serializable {

    private String login;
    private String message;
    private String[] users;
    private Date time;

    //Конструктор которым будет пользоваться клиент
    public Message (String login, String message) {
        this.login = login;
        this.message = message;
        this.time = java.util.Calendar.getInstance().getTime();
    }

    //Конструктор которым будет пользоваться сервер
    public Message (String login, String message, String[] users) {
        this.login = login;
        this.message = message;
        this.time = java.util.Calendar.getInstance().getTime();
        this.users = users;
    }

    public void setOnlineUsers(String [] users) {
        this.users = users;
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

    public String[] getUsers() {
        return users;
    }

    public String getTime() {
        Time tm = new Time(this.time.getTime());
        return tm.toString();
    }
}
