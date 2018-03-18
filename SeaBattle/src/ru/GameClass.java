package ru;

import java.util.*;

public class GameClass {
    
    private final int gameBoardSize = 10;
    private int numOfGuesses = 0; //Количество ходов
    private InputController helper = new InputController(); //Объект ввода данных
    public ArrayList<ShipManager> dotComsList = new ArrayList<ShipManager>(); //Общий список кораблей
    
    private String[][] playerGameBoard;
    
    public GameClass () {
        playerGameBoard = new String[gameBoardSize][gameBoardSize];
        for (int i = 0; i < gameBoardSize; i ++) {
            for (int j = 0; j < gameBoardSize; j++) {
                playerGameBoard[i][j] = "O";
            }
        }
    }
    
    public static void main(String[] args) {
        
        DescManager dm = new DescManager();
        InputController ic = new InputController();
        GameClass gc = new GameClass();
        
//        gm.printGameBoard();
        
        dm.setupGameBoard();
        dm.addNewShips();
        dm.refreshGameBoard();
        
        

        while (dm.getShipsCount() != 0)
        {
            //gc.printShipsInfo(dm);
            gc.printGameBoard();
            gc.makeOneTurn (dm, ic);
            gc.numOfGuesses++;
            
        }
        
        System.out.println("Вы закончили игру за: " + gc.numOfGuesses + " выстрелов. Отлично!");
        
        System.out.println("exit");
    }
    
    private void printGameBoard () {
        System.out.print("  |");
        for (int i = 0; i < gameBoardSize; i++) {
            String alphabet = "abcdefghij";
            System.out.print(alphabet.charAt(i) + " ");
        }
        
        System.out.println("");
        
        for (int i = 0; i < 22; i++)
            System.out.print("-");
        
        System.out.println("");
        
        for (int i = 0; i < gameBoardSize; i ++) {
            if (i != 9)System.out.print(" " + (i + 1) + "|");
            else System.out.print("10|");
            for (int j = 0; j < gameBoardSize; j++) {
                
                System.out.print(playerGameBoard[j][i] + " ");
            }
            System.out.println("");
        }
    }
    
    private void changeCellStatus(int [] shoot) {
        if(shoot[0] == 0)
            playerGameBoard[shoot[1]][shoot[2]] = "*";
        if(shoot[0] == 1)
            playerGameBoard[shoot[1]][shoot[2]] = "X";
    }
    
    private void makeOneTurn (DescManager dm, InputController ic) {
        int[] shootCoord = dm.checkUserShoot(ic.userInputString());
        changeCellStatus(shootCoord);
    }
 
    private void printShipsInfo (DescManager dm) {
        
        String alphabet = "abcdefghij";
        
        ArrayList<ShipManager> ships = dm.getShipsOnBoard();
        
        for (ShipManager sh : ships) {
            
            ArrayList<String> coords = sh.getShipCoords();
            int shiplength = sh.getLength();
            
            System.out.print(sh.getShipName() + ": ");
            
            for (String coord : coords) {
                int numOfChar = Integer.parseInt("" + coord.charAt(0));
                int numOfRow = Integer.parseInt("" + coord.charAt(1));
                String strCoords = "" + alphabet.charAt(numOfChar) + (numOfRow + 1);
                System.out.print(strCoords + ", ");
            }
            
            System.out.println("");
            
        }
    
    }
}
