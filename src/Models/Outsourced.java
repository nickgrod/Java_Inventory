/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Nick Rodriguez
 */
public class Outsourced extends Part{
    
    private String companyName;
    
    public Outsourced(int partID, String name, double price, int inStock, int min, int max, String companyName){
        this.partID = partID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
        this.companyName = companyName;
        
        
    }
    
    public String getCompanyName(){
        return companyName;
    }
    public void setCompanyName(String newName){
        companyName = newName;
    }
}
