/*
    *Длина корабля (1,2,3,4)
    *Координаты корабля

    -Определение длинны корабля
    -Внесение координат
    -Нанесение повреждений
*/
package ru;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class ShipManager {
    
   private int shipLength;
   private ArrayList<String> shipCoords = new ArrayList<String>();
   private String shipName;
   
   public ShipManager (int newShipLength, String sn) {
       setLength(newShipLength);
       shipName = sn;
   }
   
   public void setLength (int l) {
       if (l > 0 && l < 5) shipLength = l;
   }
   
   public int getLength () {
       return shipLength;
   }
   
   public String getShipName () {
       return shipName;
   }
   
   public void setShipCoords (ArrayList<String> newCoords) {
       
       if (newCoords.size() == shipLength)
           shipCoords = newCoords;
       
   }
   
   public ArrayList<String> getShipCoords () {
       return shipCoords;
   }
   
   public void getDamage(String coord) {
       shipCoords.remove(coord);
   }
    
}
