/**
 * Created by User on 24.03.2017.
 */

import java.io.*;
import java.net.*;

public class DailyAdviceServer {
    String[] adviceList = {"Ешьте меньшими порциями", "Купите облегающие джинсы. Нет они не делают вас полнее.",
                            "Два слова: не годится", "Будьте чесны, хотя бы сегодня", "Скажите своему начальнику все, что вы *на самом деле* о нем думаете",
                            "Возможно, вам стоит подобрать другую прическу"};

    public void go () {
        try {

            ServerSocket serverSocket = new ServerSocket(4242);

            while (true) {
                Socket sock = serverSocket.accept();

                PrintWriter writer = new PrintWriter(sock.getOutputStream());
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println(advice);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAdvice () {
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }

    public static void main (String[] args) {
        DailyAdviceServer server = new DailyAdviceServer();
        server.go();
    }
}
