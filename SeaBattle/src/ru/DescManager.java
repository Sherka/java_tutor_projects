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


public class DescManager {
    
    private int[][] cellsStatus; //Состояние ячеек на доске (Свободна, занята)
    
    private ArrayList<ShipManager> shipsOnBoard = new ArrayList<ShipManager>(); //Список живых кораблей на доске
    
    private final int gameBoardSize = 10;
    
    public ArrayList<ShipManager> getShipsOnBoard() {
        return shipsOnBoard;
    }
    
    public int getShipsCount () {
        return shipsOnBoard.size();
    }
    
    //Подготовка игровой доски
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
        String newShipName;
        
        while (counter != 0) {
            switch (counter) {
                case 1 : {
                    for (int j = 0; j < 4; j++) {
                        makeNewFirstCoordinate(counter, "Катер");
                    }
                    counter--;
                    break;
                }
                case 2 : {
                    for (int j = 0; j < 3; j++) {
                        makeNewFirstCoordinate(counter, "Фрегат");
                    }
                    counter--;
                    break;
                }
                case 3 : {
                    for (int j = 0; j < 2; j++) {
                        makeNewFirstCoordinate(counter, "Линкор");
                    }
                    counter--;
                    break;
                }
                case 4 : {
                    for (int j = 0; j < 1; j++) {
                        makeNewFirstCoordinate(counter, "Линейный");
                    }
                    counter--;
                    break;
                }
            }
        }
    }
    
    //Подбираем расположение корабля
    private void makeNewFirstCoordinate (int length, String shipName) {
        
        int cell, row, randomSide;
        boolean canPlace = false;
        ArrayList<String> coords = new ArrayList<>();
        
        while (!canPlace) {
            cell = (int) (Math.random() * gameBoardSize);
            row = (int) (Math.random() * gameBoardSize);
            randomSide = (int) (Math.random() * 4);

            if (cellsStatus[row][cell] == 0) {

                

                switch (randomSide) {
                    //Вверх
                    case 0 : {
                        
                        coords.clear();
                        if (row - length > 0) {

                            for (int i = 0; i < length; i++) {
                                if (cellsStatus[row - i][cell] == 0) {
                                    coords.add("" + (row-i) + cell);
                                }
                            }
                            
                            if (coords.size() == length) {
                                canPlace = true;
                                break;
                            }

                        }
                        break;
                    }

                //В право
                    case 1 : {
                        coords.clear();
                        if (cell + length < 10) {

                            for (int i = 0; i < length; i++) {
                                if (cellsStatus[row][cell + i] == 0) {
                                    coords.add("" + row + (cell + i));
                                    
                                }
                            }

                            if (coords.size() == length) {
                                canPlace = true;
                                break;
                            }

                        }
                        break;
                    }

                //Вниз
                    case 2 : {
                        coords.clear();
                        if (row + length < 10) {

                            for (int i = 0; i < length; i++) {
                                if (cellsStatus[row + i][cell] == 0) {
                                    coords.add("" + (row+i) + cell); 
                                    
                                }
                            }
                            if (coords.size() == length) {
                                canPlace = true;
                                break;
                            }

                        }
                        break;

                        
                    }

                //В лево
                    case 3 : {
                        coords.clear();
                        if (cell - length > 0) {

                            for (int i = 0; i < length; i++) {
                                if (cellsStatus[row][cell - i] == 0) {
                                    coords.add("" + row + (cell - i));
                                    
                                }
                            }
                            if (coords.size() == length) {
                                canPlace = true;
                                break;
                            }

                        }
                        break;
                        }
                    }
                }

        }
        
        constructNewShip(length, coords, shipName);
        
    }    
    
    //Создаем корабль по выбранным координатам
    public void constructNewShip (int length, ArrayList<String> coords, String shipName) {
        ShipManager newShip = new ShipManager (length, shipName);

        newShip.setShipCoords(coords);

        coords.forEach((str) -> {
            changeCellStatus(str,1);
            changeCellStatus(str,2);
        });

        shipsOnBoard.add(newShip);
    }
    
    //Обновление игрового поля перед стартом игры
    public void refreshGameBoard () {
        for (int i = 0; i < gameBoardSize; i ++) {
            for (int j = 0; j < gameBoardSize; j++) {
                if(cellsStatus[i][j] != 1) {
                    String coord = "" + i + j;
                    changeCellStatus(coord, 0);
                }
                
            }
        }
    }
    
    //Устанавливаем статус поля
    private void changeCellStatus (String coord, int status) {
        
        int bigCoord = Integer.parseInt(coord);
        
        int row = (int) (bigCoord/10);
        int cell = (int) (bigCoord%10);
        
        switch (status) {
            
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
                break;
            }
            
            default : {
                cellsStatus[row][cell] = status;
                break;
            }

        }
        
    }
    
    //Вывод игрового поля на экран
    public void printGameBoard () {
        for (int i = 0; i < gameBoardSize; i++) {
            for (int j = 0; j < gameBoardSize; j++)
                System.out.print(cellsStatus[i][j] + " ");
            System.out.print("\n");
        }
        
        System.out.println("Всего: " + shipsOnBoard.size() + " кораблей");
    }
    
    public int[] checkUserShoot (String userShoot) {
        String alphabet = "abcdefghij";
        boolean getShoot = false;
        int[] shoot = new int[3];
        
        int cell = 0;
        
        for (int i = 0; i < alphabet.length(); i ++) {
            if (userShoot.charAt(0) == alphabet.charAt(i))
                cell = i;
        }
        
        int row;
        if(userShoot.length() == 2)
            row = Integer.parseInt("" + userShoot.charAt(1)) - 1;
        else
            row = Integer.parseInt("" + userShoot.charAt(1) + userShoot.charAt(2)) - 1;
        
        String userCoord = "" + cell + row;
        
        for (int i = 0; i < shipsOnBoard.size(); i++)
        {
            ShipManager ship = shipsOnBoard.get(i);
            ArrayList<String> shipCoords = ship.getShipCoords();
            
            if(shipCoords.contains(userCoord)) {
                ship.getDamage(userCoord);
                System.out.println("Попал");
                getShoot = true;
            }
            
            if(ship.getShipCoords().size() == 0) {
                shipsOnBoard.remove(i);
                System.out.println("Убил");
            }
                
        }
        
        if (getShoot)
            shoot[0] = 1;
        else
            shoot[0] = 0;
        
        shoot[1] = cell;
        shoot[2] = row;
        System.out.println("" + shoot[1] + shoot[2]);
        return shoot;
    }
}
