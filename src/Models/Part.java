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
 */

//class is abstract since it will never be initialized without InHouse or Outsourced
public abstract class Part {
    protected int partID;
    protected String name;
    protected double price;
    protected int inStock;
    protected int min;
    protected int max;
    
    public void setName(String newName){
        name = newName;
    }
    public String getName(){
        return name;
    }
    public void setPrice(double newPrice){
        price = newPrice;
    }
    public double getPrice(){
        return price;
    }
    public void setInStock(int newStock){
        inStock = newStock;
    }
    public int getInStock(){
        return inStock;
    }
    public void setMin(int newMin){
        min = newMin;
    }
    public void setMax(int newMax){
        max = newMax;
    }
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }
    public void setPartID(int newID){
        partID = newID;
    }
    public int getPartID(){
        return partID;
    }
}
