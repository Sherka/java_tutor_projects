package ru;

import java.util.*;

public class GameClass {
    
    private int numOfGuesses = 0; //Количество ходов
    private InputController helper = new InputController(); //Объект ввода данных
    public ArrayList<ShipManager> dotComsList = new ArrayList<ShipManager>(); //Общий список кораблей
    

    
    public static void main(String[] args) {
        
        DescManager dm = new DescManager();
        dm.setupGameBoard();
        dm.addNewShips();
        dm.refreshGameBoard();
        
        dm.printGameBoard();
        
        System.out.println("exit");
    }
}
