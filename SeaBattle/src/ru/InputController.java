package ru;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class InputController {
    
    String alphabet = "abcdefghij";
    
    public String userInputString () {
        boolean rightCoord = false;
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        
        String userString = "";
        
        try {
            while (!rightCoord) {
                userString = stdin.readLine();
                rightCoord = checkUserInput(userString);
            }
                
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        return userString;
    }
    
    private boolean checkUserInput (String userString) {
        
        boolean correctChar = false;
        userString = userString.toLowerCase();
        
        if (userString.length() > 3 || userString.length() < 2) {
            System.out.println("Не верные данные");
            return false;
        }
        
        char cell = userString.charAt(0);
        
        if (userString.length() == 2) {
            for (int i = 0; i < alphabet.length(); i++)
                if (cell == alphabet.charAt(i))
                    correctChar = true;

            if (!correctChar) {
                System.out.println("Не верно введена клетка");
                return false;
            }

            int row;

            try {
                row = Integer.parseInt("" + userString.charAt(1));
            } catch (Exception ex){
                System.out.println("Неверно введен номер строки");
                return false;
            }

            if(row < 1 || row > 10) {
                System.out.println("Строка вне поля игры");
                return false;
            }
        }
        
        if (userString.length() == 3) {
            for (int i = 0; i < alphabet.length(); i++)
                if (cell == alphabet.charAt(i))
                    correctChar = true;

            if (!correctChar) {
                System.out.println("Не верно введена клетка");
                return false;
            }

            int row;
            String secondCoord = "" + userString.charAt(1) + userString.charAt(2);
            
            try {
                row = Integer.parseInt("" + userString.charAt(1));
            } catch (Exception ex){
                System.out.println("Неверно введен номер строки");
                return false;
            }

            if(row < 1 || row > 10) {
                System.out.println("Строка вне поля игры");
                return false;
            }
        }
        return true;
        
    }
    
    public void idleInput() {
        
    }
    
}
