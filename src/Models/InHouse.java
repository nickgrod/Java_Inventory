/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Nick Rodriguez
 * @version 1.0
 * 
 */
public class InHouse extends Part{
    
    private int machineID;
    
    public InHouse(int partID, String name, double price, int inStock, int min, int max, int machineID){
        
        this.partID = partID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
        this.machineID = machineID;
        
    }
   
    
    public void setMachineID(int newID){
        machineID = newID;
    }
    public int getMachineID(){
        return machineID;
    }
}
