/*

        *лист координат
        *список живых кораблей
        *список ходов

    -Инициализация игрового поля
    -Размещение новых кораблей на доске
    -Проверка попаданий
*/

package ru;

import java.util.ArrayList;
import java.util.HashMap;


public class DescManager {
    
    private int[][] cellsStatus; //Состояние ячеек на доске (Свободна, занята)
    
    private ArrayList<ShipManager> shipsOnBoard = new ArrayList<ShipManager>(); //Список живых кораблей на доске
    
    private final int gameBoardSize = 10;
    
    public void setupGameBoard () {
        
        cellsStatus = new int[gameBoardSize][gameBoardSize];
        
        int cell = 0;
        int row = 0;
        
        while (row < gameBoardSize) {
            
            if (cell < gameBoardSize) {
                
                cellsStatus[row][cell] = 0;
                
                //System.out.println("Добавляем координату: " + coord);
            
                cell++;
            } else {
                row++;
                cell = 0;
            }
        }
        
    }
    
    //Создаем все корабли и распологаем их на игровой доске
    public void addNewShips () {
        int counter = 4;
        while (counter != 0) {
            switch (counter) {
                case 1 : {
                    for (int j = 0; j < 4; j++) {
                        makeNewFirstCoordinate(counter);
                    }
                    counter--;
                    break;
                }
                case 2 : {
                    for (int j = 0; j < 3; j++) {
                        makeNewFirstCoordinate(counter);
                    }
                    counter--;
                    break;
                }
                case 3 : {
                    for (int j = 0; j < 2; j++) {
                        makeNewFirstCoordinate(counter);
                    }
                    counter--;
                    break;
                }
                case 4 : {
                    for (int j = 0; j < 1; j++) {
                        makeNewFirstCoordinate(counter);
                    }
                    counter--;
                    break;
                }
            }
        }
    }
    
    //Выбираем стартовую координату корабля
    private void makeNewFirstCoordinate (int length) {
        int cell, row;
        
        cell = (int) (Math.random() * gameBoardSize);
        row = (int) (Math.random() * gameBoardSize);
        
        if (cellsStatus[row][cell] == 0) {
            
            ArrayList<String> coords = new ArrayList<>();
            //Вверх
            if (row - length > 0) {
                boolean canPlace = true;
                
                for (int i = 0; i < length; i++) {
                    if (cellsStatus[row - i][cell] != 0)
                        canPlace = false;
                    
                    coords.add("" + (row-i) + cell);
                }
                
                if(canPlace) {
                    ShipManager newShip = new ShipManager (length);

                    newShip.setShipCoords(coords);
                    
                    coords.forEach((str) -> {
                        changeCellStatus(str,1);
                        changeCellStatus(str,2);
                    });
                
                    
                    shipsOnBoard.add(newShip);
                }
                
                return;
            }
            //В право
            if (cell + length < 10) {
                boolean canPlace = true;
                
                for (int i = 0; i < length; i++) {
                    if (cellsStatus[row][cell + i] != 0)
                        canPlace = false;
                    
                    coords.add("" + row + (cell + i));
                }
                
                if(canPlace) {
                    ShipManager newShip = new ShipManager (length);

                    newShip.setShipCoords(coords);
                    
                    coords.forEach((str) -> {
                        changeCellStatus(str,1);
                        changeCellStatus(str,2);
                    });
                    
                    shipsOnBoard.add(newShip);
                }
                
                return;
            }
            //Вниз
            if (row + length < 10) {
                boolean canPlace = true;
                
                for (int i = 0; i < length; i++) {
                    if (cellsStatus[row + i][cell] != 0)
                        canPlace = false;
                    
                    coords.add("" + (row+i) + cell);
                }
                
                if(canPlace) {
                    ShipManager newShip = new ShipManager (length);

                    newShip.setShipCoords(coords);
                    
                    coords.forEach((str) -> {
                        changeCellStatus(str,1);
                        changeCellStatus(str,2);
                    });
                    
                    shipsOnBoard.add(newShip);
                }
                
                return;
            }
            //В лево
            if (cell - length > 0) {
                boolean canPlace = true;
                
                for (int i = 0; i < length; i++) {
                    if (cellsStatus[row][cell - i] != 0)
                        canPlace = false;
                    
                    coords.add("" + row + (cell - i));
                }
                
                if(canPlace) {
                    ShipManager newShip = new ShipManager (length);

                    newShip.setShipCoords(coords);
                    
                    coords.forEach((str) -> {
                        changeCellStatus(str,1);
                        changeCellStatus(str,2);
                    });
                    
                    shipsOnBoard.add(newShip);
                }
                
                return;
            }
            
            
            
        } else {
            makeNewFirstCoordinate(length);
        }
        
        
    }    

    private void changeCellStatus (String coord, int status) {
        
        int bigCoord = Integer.parseInt(coord);
        
        int row = (int) (bigCoord/10);
        int cell = (int) (bigCoord%10);
        
        switch (status) {
            case 1 : {
                cellsStatus[row][cell] = status;
                break;
            }
            
            case 2 : {
                for (int i = -1 ; i <= 1 ; i++) {
                    if (row + i >= 0 && row + i < 10) {
                        for (int j = -1 ; j <= 1; j++) {
                            if (cell + j >= 0 && cell + j < 10) {
                                if (cellsStatus[row + i][cell + j] != 1)
                                    cellsStatus[row + i][cell + j] = 2;
                            }
                        }
                    }
                }
            }

        }
        
    }
    
    public void printGameBoard () {
        for (int i = 0; i < gameBoardSize; i++) {
            for (int j = 0; j < gameBoardSize; j ++)
                System.out.print(cellsStatus[i][j] + " ");
            System.out.print("\n");
        }
        
        System.out.println("Всего: " + shipsOnBoard.size() + " кораблей");
    }
}
