package Models;

import Models.Part;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Nick Rodriguez
 * @version 1.0
 *
 */
public class Product {

    private ObservableList<Part> associatedParts;
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

    public Product() {
        associatedParts = FXCollections.observableArrayList();

    }

    //initializes the observable list for the parts
    public Product(ObservableList<Part> partsList, int prodID, String prodName, double prodPrice, int stock, int mini, int maxi) {
        associatedParts = partsList;
        productID = prodID;
        name = prodName;
        price = prodPrice;
        inStock = stock;
        min = mini;
        max = maxi;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setInStock(int newStock) {
        inStock = newStock;
    }

    public int getInStock() {
        return inStock;
    }

    public void setMin(int newMin) {
        min = newMin;
    }

    public void setMax(int newMax) {
        max = newMax;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void addAssociatedPart(Part newPart) {
        associatedParts.add(newPart);
    }

    //finds a part and removes it (searches by ID) if it exists
    public boolean removeAssociatedPart(int oldPart) {
        for (Part part : associatedParts) {
            if (part.getPartID() == oldPart) {
                associatedParts.remove(part);
                return true;
            }
        }
        return false;
    }

    //finds a part and returns it if it exists
    public Part lookupAssociatedPart(int partNum) {
        for (Part part : associatedParts) {
            if (part.getPartID() == partNum) {
                return part;
            }
        }
        return null;
    }

    public void setProductID(int newID) {
        productID = newID;
    }

    public int getProductID() {
        return productID;
    }

    public ObservableList<Part> getParts() {
        return associatedParts;
    }
}
