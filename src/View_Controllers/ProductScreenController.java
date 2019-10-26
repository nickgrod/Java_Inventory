/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controllers;

import View_Controllers.InventoryLandingController;
import Models.Product;
import Models.Part;
import static View_Controllers.InventoryLandingController.inventory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author nickgrod
 */
public class ProductScreenController implements Initializable {

    @FXML
    private Label productLabel;
    @FXML
    private TextField productIDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField invField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Part> allPartsTable;
    @FXML
    private TableView<Part> containedPartsTable;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    //The TableView for available parts.
    @FXML
    private TableColumn<Part, String> avPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> avPartIDColumn;
    @FXML
    private TableColumn<Part, Integer> avPartInvColumn;
    @FXML
    private TableColumn<Part, Double> avPartPriceColumn;

    //The TableView for parts contained in the product
    @FXML
    private TableColumn<Part, String> incPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> incPartIDColumn;
    @FXML
    private TableColumn<Part, Integer> incPartInvColumn;
    @FXML
    private TableColumn<Part, Double> incPartPriceColumn;

    Product product = new Product();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Part part : inventory.getParts()) {
            associatedParts.add(part);
        }

        avPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        avPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        avPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        avPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsTable.setItems(associatedParts);

        incPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        incPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        incPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        incPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        containedPartsTable.setItems(product.getParts());
    }

    //throws a confirmation before confirming cancel
    public void clickCancel(ActionEvent event) throws IOException {
        boolean check = confirmBox();

        if (check) {
            Parent partsWindow = FXMLLoader.load(getClass().getResource("/inventoryproject/InventoryLanding.fxml"));
            Scene scene = new Scene(partsWindow);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
            window.setTitle("Inventory System");
        }

    }

    //removes a part from the list and pushes it back into the available parts
    public void removePartFromList(ActionEvent event) throws IOException {
        Part selectedPart = containedPartsTable.getSelectionModel().getSelectedItem();
        product.removeAssociatedPart(selectedPart.getPartID());
        associatedParts.add(selectedPart);
    }

    
    //starts validation process on save click
    public void clickSave(ActionEvent event) throws IOException {
        double price;
        try {
            price = Double.parseDouble(priceField.getText());

        } catch (NumberFormatException e) {
            alertDouble();
            priceField.setText("");
            return;
        }

        boolean checker = validateAll();

        if (checker) {

            boolean finalCheck = checkPrices(price);
            if (finalCheck) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/inventoryproject/InventoryLanding.fxml"));
                Parent invLanding = loader.load();

                Scene scene = new Scene(invLanding);
                InventoryLandingController controller = loader.getController();

                String productName = nameField.getText();
                int inStock = Integer.parseInt(invField.getText());
                int min = Integer.parseInt(minField.getText());
                int max = Integer.parseInt(maxField.getText());
                product.setName(productName);
                product.setInStock(inStock);
                product.setMin(min);
                product.setMax(max);
                product.setPrice(price);
                String idIfExists = (productIDField.getText());
                if (idIfExists.equals("")) {
                    product.setProductID(0);
                    controller.addProduct(product);
                } else {
                    product.setProductID(Integer.parseInt(productIDField.getText()));
                    controller.modifyProduct(product);
                }

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
                window.setTitle("Inventory System");
            }
        }

    }

    //adds a part to the product's list and removes it from available parts
    public void addPart(ActionEvent event) throws IOException {
        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
        associatedParts.remove(selectedPart);
        product.addAssociatedPart(selectedPart);
    }

    //accepts information for the modify screen
    public void initData(Product product2) {
        productLabel.setText("Modify Product");
        nameField.setText(product2.getName());
        invField.setText(Integer.toString(product2.getInStock()));
        minField.setText(Integer.toString(product2.getMin()));
        maxField.setText(Integer.toString(product2.getMax()));
        priceField.setText(Double.toString(product2.getPrice()));
        productIDField.setText(Integer.toString(product2.getProductID()));
        for (Part part : product2.getParts()) {
            product.addAssociatedPart(part);
            associatedParts.remove(part);
        }
    }

    //searches for a part in the available list, throws dialog if not found
    public void partSearch() {

        if (associatedParts.isEmpty()) {
            System.out.println("No parts to search!");
        } else {

            int partsSearch = Integer.parseInt(searchField.getText());

            Part part = inventory.lookupPart(partsSearch);
            if (part != null) {
                allPartsTable.getSelectionModel().select(part);
            } else {
                noSuchPart();
            }
        }
    }

    //validates that all fields are complete and min/max/instock relationships are valid
    public boolean validateAll() {
        int min;
        int max;
        int inv;
        if (priceField.getText().isEmpty() || nameField.getText().isEmpty() || minField.getText().isEmpty() || maxField.getText().isEmpty() || invField.getText().isEmpty()) {
            alertFields();
            return false;
        } else {

            min = Integer.parseInt(minField.getText());
            max = Integer.parseInt(maxField.getText());
            inv = Integer.parseInt(invField.getText());
            if (min > max) {

                alertInventory("Minimum inventory cannot be higher than maximum.");
                return false;
            } else if (inv < min) {

                alertInventory("Cannot have fewer in stock than minimum.");
                return false;
            } else if (inv > max) {

                alertInventory("Cannot have more in stock than maximum.");
                return false;
            } else if (product.getParts().isEmpty() || product.getParts() == null) {
                alertParts();
                return false;
            }

        }

        return true;
    }

    
    //verifies that integer entries are non-negative and all integers
    public boolean onlyInts() {
        String minStr = minField.getText();
        String maxStr = maxField.getText();
        String currentInv = invField.getText();

        for (char c : minStr.toCharArray()) {
            if (c == '-') {
                minField.setText("");
                return false;
            } else if (c < '0' || c > '9') {
                minField.setText("");
                return false;
            }
        }
        for (char c : maxStr.toCharArray()) {
            if (c == '-') {
                maxField.setText("");
                return false;
            } else if (c < '0' || c > '9') {
                maxField.setText("");
                return false;
            }
        }
        for (char c : currentInv.toCharArray()) {
            if (c == '-') {
                invField.setText("");
                return false;
            } else if (c < '0' || c > '9') {

                invField.setText("");
                return false;
            }
        }
        return true;
    }
    //Checks to see if the current price of parts is greater than price of product
    
    public boolean checkPrices(Double price) {
        double currentCost = 0.0;
        for (Part part : product.getParts()) {
            currentCost += part.getPrice();
        }
        if (currentCost == 0.0) {
            return false;
        } else if (currentCost > price){
            alertInventory("Current cost to make product is higher than price of product");
          return false;  
        }else {
            return true;
        }
    }
    
    
    /*
    
    ALL DIALOG AND CONFIRMATION BOX FUNCTIONS
    */
    public void alertInventory(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Inventory Warning");
        alert.setHeaderText("Invalid Inventory");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void alertDouble() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Price Amount");
        alert.setContentText("Price must be a number.");
        alert.showAndWait();
    }

    public void alertFields() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Incomplete Information");
        alert.setHeaderText("Incomplete Field Information");
        alert.setContentText("Please fill out all fields.");
        alert.showAndWait();
    }

    public boolean confirmBox() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Confirm your selection.");
        alert.setContentText("Are you sure you want to cancel? You will lose any data that hasn't been saved.");
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }

    public void alertParts() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Parts Required");
        alert.setHeaderText("Product Missing Parts");
        alert.setContentText("At least one part required to save part.");
        alert.showAndWait();

    }

    
        public void noSuchPart() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Parts Search");
        alert.setHeaderText("Part Not Found");
        alert.setContentText("Could not find part with matching ID, please search again.");
        alert.showAndWait();

    }

}
