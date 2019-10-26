package View_Controllers;

import Models.Product;
import Models.Part;
import Models.Inventory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Nick Rodriguez
 * @version 1.0
 * @C482 Project, Inventory System
 */
public class InventoryLandingController implements Initializable {

    //The buttons and fields for the Parts portion of the landing page.
    @FXML
    private TextField partsSearchField;
    @FXML
    private Button partsSearchButton;
    @FXML
    private Button partsAddButton;
    @FXML
    private Button partsModifyButton;
    @FXML
    private Button partsDeleteButton;

    //The buttons and fields for the Products portion of the landing page.
    @FXML
    private TextField productSearchField;
    @FXML
    private Button productsSearchButton;
    @FXML
    private Button productsAddButton;
    @FXML
    private Button productsModifyButton;
    @FXML
    private Button productsDeleteButton;

    //The TableView for the parts.
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, Integer> partInvColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    //The TableView for the products.
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productIDColumn;
    @FXML
    private TableColumn<Product, Integer> productInvColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private Button exitButton;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    public static Inventory inventory = new Inventory();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(inventory.getProducts());

        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(inventory.getParts());

    }

    //Opens the parts screen on "Add Part"
    public void openPartsScreen(ActionEvent event) throws IOException {

        Parent partsWindow = FXMLLoader.load(getClass().getResource("/inventoryproject/PartsScreen.fxml"));

        Scene scene = new Scene(partsWindow);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Inventory System");
    }

    //opens the products screen on "Add Product"
    public void openProductsScreen(ActionEvent event) throws IOException {

        Parent productWindow = FXMLLoader.load(getClass().getResource("/inventoryproject/ProductScreen.fxml"));

        Scene scene = new Scene(productWindow);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Inventory System");
    }

    //takes the selected part and pushes it to the modify Parts screen
    public void modifyPart(ActionEvent event) throws IOException {

        try{
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/inventoryproject/PartsScreen.fxml"));
        Parent partsWindow = loader.load();

        Scene scene = new Scene(partsWindow);

        PartsScreenController controller = loader.getController();
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        controller.initData(selectedPart);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Modify Part");          
        }catch (NullPointerException e){
            System.out.println("No item selected...");
        }

    }

    //Searches for a matching part based on Part ID
    public void partSearch() {

        if (inventory.getParts().isEmpty()) {
            System.out.println("No parts to search!");
        } else {

            int partsSearch = Integer.parseInt(partsSearchField.getText());

            Part part = inventory.lookupPart(partsSearch);
            if (part != null) {
                partsTable.getSelectionModel().select(part);
            } else {
                noSuchPart();
            }
        }
    }

    //Searches for a matching product based on product ID
    public void productSearch() {
        if (inventory.getProducts().isEmpty()) {
            System.out.println("No products to search!");
        } else {

            int prodSearch = Integer.parseInt(productSearchField.getText());

            Product prod = inventory.lookupProduct(prodSearch);
            if (prod != null) {
                productsTable.getSelectionModel().select(prod);
            } else {
                noSuchProduct();
            }
        }
    }

    //Pushes a product to the modify screen
    public void modifyProduct(ActionEvent event) throws IOException {
        try{
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/inventoryproject/ProductScreen.fxml"));
        Parent productsWindow = loader.load();
        Scene scene = new Scene(productsWindow);

        ProductScreenController controller = loader.getController();
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        controller.initData(selectedProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Modify Product");           
        }catch(NullPointerException e){
            System.out.println("No item selected...");
        }


    }

    //exits the program on click of the "exit" button
    public void leaveProgram() {
        Platform.exit();
        System.exit(0);
    }

    //receives a new part passed from the parts page
    public void addNewPart(Part part) {

        part.setPartID(inventory.getCurrentPartID());
        inventory.iterateCurrentPartID();
//           partsTable.getItems().add(part);
        inventory.addPart(part);
    }

    //Confirms a part being added after information is passed from Parts window
    public void addProduct(Product product) {

        product.setProductID(inventory.getCurrentProductID());
        inventory.iterateCurrentProductID();
        inventory.addProduct(product);
    }
    //recieves a part from the Parts screen and updates the correct index
    //of the observable list

    public void modifyPart(Part part) {
        int current = 0;
        int index = 0;
        for (Part tempPart : inventory.getParts()) {
            if (tempPart.getPartID() == part.getPartID()) {
                index = current;
            } else {
                current++;
            }
        }
        inventory.updatePart(index, part);
    }

    //Modifies a product after being returned from the modify product
    public void modifyProduct(Product product) {
        int current = 0;
        int index = 0;
        for (Product tempProd : inventory.getProducts()) {
            if (tempProd.getProductID() == tempProd.getProductID()) {
                index = current;
            } else {
                current++;
            }
        }
        inventory.updateProduct(index, product);
    }

    //deletes a part that has been selected, checks for null selection
    public void deletePart() {
        boolean check = confirmBox();
        if (check && partsTable.getSelectionModel().getSelectedItem() != null) {
            Part deletePart = partsTable.getSelectionModel().getSelectedItem();
            inventory.deletePart(deletePart);
        }

    }

    //deletes a product that has been selected, checks for null selection
    public void deleteProduct() {
        boolean check = confirmBox();
        if (check && productsTable.getSelectionModel().getSelectedItem() != null) {
            Product deleteProduct = productsTable.getSelectionModel().getSelectedItem();
            if (deleteProduct.getParts() != null) {
                alertProductHasParts();
            } else {
                int productID = deleteProduct.getProductID();
                inventory.removeProduct(productID);
            }

        }

    }

    /*
    THESE ARE THE ALERT BOXES THAT EITHER CONFIRM SELECTIONS OR THROW WARNINGS
     */
    public boolean confirmBox() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Confirm your selection.");
        alert.setContentText("Are you sure you want to cancel? You will lose any data that hasn't been saved.");
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }

    public void alertProductHasParts() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cannot Delete");
        alert.setHeaderText("Product Cannot Be Deleted");
        alert.setContentText("Unable to delete a product that has parts attached.");
        alert.showAndWait();

    }

    public void noSuchPart() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Parts Search");
        alert.setHeaderText("Part Not Found");
        alert.setContentText("Could not find part with matching ID, please search again.");
        alert.showAndWait();

    }

    
    public void noSuchProduct() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Product Search");
        alert.setHeaderText("Product Not Found");
        alert.setContentText("Could not find product with matching ID, please search again.");
        alert.showAndWait();

    }
}
