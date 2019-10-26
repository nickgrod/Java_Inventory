/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Nick Rodriguez
 */
public class Inventory {

    private static ObservableList<Product> products;
    private static ObservableList<Part> allParts;
    private static int currentPartID;
    private static int currentProductID;

    
    //initializes the collections on construct
    public Inventory() {
        products = FXCollections.observableArrayList();
        allParts = FXCollections.observableArrayList();
        currentPartID = 1;
        currentProductID = 1;

    }

    //adds a product to the products list
    public void addProduct(Product newProduct) {
        products.add(newProduct);
    }

    //removes a product if it exists
    public boolean removeProduct(int productID) {
        int index = -1;
        int count = 0;
        for (Product indProduct : products) {
            if (indProduct.getProductID() == productID) {
                index = count;
            }
            count++;
        }
        if (index == -1) {
            System.out.println("No such item found.");
            return false;
        } else {
            products.remove(index);
            return true;
        }
    }

    //finds a product by productID
    public Product lookupProduct(int productID) {
        for (Product product : products) {
            if (product.getProductID() == productID) {
                return product;
            }
        }
        return null;
    }

    //replaces a product with an updated version on modify
    public void updateProduct(int index, Product product) {
        products.set(index, product);
    }

    //adds a part to the parts list
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    //deletes a part if it exists
    public boolean deletePart(Part part) {
        int index = -1;
        int count = 0;
        for (Part indPart : allParts) {
            if (indPart.getName().equals(part.getName()) && indPart.getPartID() == part.getPartID()) {
                index = count;
            }
            count++;
        }
        if (index == -1) {
            System.out.println("No such item found.");
            return false;
        } else {
            allParts.remove(index);
            return true;
        }

    }

    //finds a part if it exists within the parts list
    public Part lookupPart(int partNumber) {

        for (Part part : allParts) {
            if (part.getPartID() == partNumber) {
                return part;
            }
        }
        return null;
    }

    public void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    public ObservableList<Product> getProducts() {

        return products;
    }

    public ObservableList<Part> getParts() {
        return allParts;
    }

    public int getCurrentPartID() {
        return currentPartID;
    }

    public int getCurrentProductID() {
        return currentProductID;
    }
    
    //tracks current ID to assign to new parts
    public void iterateCurrentPartID() {
        currentPartID++;
    }

    //tracks current ID to assign to new products
    public void iterateCurrentProductID() {
        currentProductID++;
    }

}
