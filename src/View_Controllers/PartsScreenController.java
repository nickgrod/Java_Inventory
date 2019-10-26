/*
This controller handles all of the functionality of the parts screens.
It reads data to change the parts screen depending on its current use so that
the parts screen can function as BOTH a modify AND a creation without needing 
two separate screens or controllers.
 */
package View_Controllers;

import Models.Part;
import Models.Outsourced;
import Models.InHouse;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author Nick Rodriguez
 * @version 1.0
 */
public class PartsScreenController implements Initializable {

    @FXML
    private Label screenTitleLabel;
    @FXML
    private TextField partIDField;
    @FXML
    private Label coNameLabel;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField inStockField;
    @FXML
    private TextField priceCostField;
    @FXML
    private TextField minNumField;
    @FXML
    private TextField maxNumField;
    @FXML
    private TextField coNameField;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourceRadio;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    private ToggleGroup partTypeToggleGroup;
    private int toggleVal = 0;

    @FXML

    //receives data for modification
    public void initData(Part part) {

        //if this part is of subclass InHouse, change labels
        if (part instanceof InHouse) {
            setAsInHouse();
            InHouse inHousePart = (InHouse) part;
            String machineID = Integer.toString(inHousePart.getMachineID());
            coNameField.setText(machineID);
        }//else if this part is of subclass Oursourced...
        else if (part instanceof Outsourced) {
            Outsourced outsourcedPart = (Outsourced) part;
            coNameField.setText(outsourcedPart.getCompanyName());
            setAsOutsourced();

        }
        String partID = Integer.toString(part.getPartID());
        partIDField.setText(partID);
        partNameField.setText(part.getName());
        String inStock = Integer.toString(part.getInStock());
        inStockField.setText(inStock);
        String price = Double.toString(part.getPrice());
        priceCostField.setText(price);
        String min = Integer.toString(part.getMin());
        String max = Integer.toString(part.getMax());
        minNumField.setText(min);
        maxNumField.setText(max);
        if (toggleVal == 0) {
            //casting the part as an inhouse part to utilize the getMachineID
            InHouse inHousePart = (InHouse) part;

        } else if (toggleVal == 1) {
            //casting part as outsourced to acquire the company name

        }

    }

    //Will initialize the window and combine the two parts types into one
    //toggle group.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAsInHouse();
        partTypeToggleGroup = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(partTypeToggleGroup);
        this.outSourceRadio.setToggleGroup(partTypeToggleGroup);
        inHouseRadio.setSelected(true);

    }

    //sets the screen to appear for an in-house addition
    public void setAsInHouse() {
        toggleVal = 0;
        screenTitleLabel.setText("In-House Part");
        coNameLabel.setText("Machine ID");
        coNameField.setPromptText("Name of Machine");
        inHouseRadio.setSelected(true);
    }

    //sets the screen to proper appearance for outsourced
    public void setAsOutsourced() {
        toggleVal = 1;
        screenTitleLabel.setText("Outsourced Part");
        coNameLabel.setText("Company Name");
        coNameField.setPromptText("Name of Company");
        outSourceRadio.setSelected(true);
    }


    //checks for the toggle of what type of part is being made
    public void partTypeSelected(ActionEvent e) {
        if (this.partTypeToggleGroup.getSelectedToggle().equals(inHouseRadio)) {
            toggleVal = 0;
            setAsInHouse();
        } else if (this.partTypeToggleGroup.getSelectedToggle().equals(outSourceRadio)) {
            toggleVal = 1;
            setAsOutsourced();

        }
    }


//handles a cancel selection and throws dialog to confirm
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

    //checks for validation before allowing either new part or modification save
    public void clickSave(ActionEvent event) throws IOException {
        boolean check = validateAll();
        if(check){
           String idIfExists = (partIDField.getText());
        if (idIfExists.equals("")) {
            clickAdd(event);
        } else {
            clickModify(event);
        }         
        }

    }

    //handles a new part being created
    public void clickAdd(ActionEvent event) throws IOException {

        double price;
        try {
            price = Double.parseDouble(priceCostField.getText());

        } catch (NumberFormatException e) {
            alertDouble();
            priceCostField.setText("");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/inventoryproject/InventoryLanding.fxml"));
        Parent invLanding = loader.load();

        Scene scene = new Scene(invLanding);
        InventoryLandingController controller = loader.getController();
        String partName = partNameField.getText();
        int inStock = Integer.parseInt(inStockField.getText());
        int min = Integer.parseInt(minNumField.getText());
        int max = Integer.parseInt(maxNumField.getText());

        if (toggleVal == 0) {
            int machineID = Integer.parseInt(coNameField.getText());
            InHouse myInHouse = new InHouse(0, partName, price, inStock, min, max, machineID);
            controller.addNewPart(myInHouse);

        } else if (toggleVal == 1) {
            String companyName = coNameField.getText();
            Outsourced myOutPart = new Outsourced(0, partName, price, inStock, min, max, companyName);
            controller.addNewPart(myOutPart);
        }
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Inventory System");
    }

    //handles a part being updated via modify screen
    public void clickModify(ActionEvent event) throws IOException {
        double price;
        try {
            price = Double.parseDouble(priceCostField.getText());

        } catch (NumberFormatException e) {
            alertDouble();
            priceCostField.setText("");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/inventoryproject/InventoryLanding.fxml"));
        Parent invLanding = loader.load();

        Scene scene = new Scene(invLanding);
        InventoryLandingController controller = loader.getController();
        String partName = partNameField.getText();
        int inStock = Integer.parseInt(inStockField.getText());
        int min = Integer.parseInt(minNumField.getText());
        int max = Integer.parseInt(maxNumField.getText());
        int partID = Integer.parseInt(partIDField.getText());
        if (toggleVal == 0) {
            int machineID = Integer.parseInt(coNameField.getText());
            InHouse myInHouse = new InHouse(partID, partName, price, inStock, min, max, machineID);
            controller.modifyPart(myInHouse);

        } else if (toggleVal == 1) {
            String companyName = coNameField.getText();
            Outsourced myOutPart = new Outsourced(partID, partName, price, inStock, min, max, companyName);
            controller.modifyPart(myOutPart);
        }
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setTitle("Inventory System");

    }

    //verifies that entreies into min, max, and inStock fields are only integers and non-negative
    public boolean onlyInts() {
        String minStr = minNumField.getText();
        String maxStr = maxNumField.getText();
        String currentInv = inStockField.getText();

        for (char c : minStr.toCharArray()) {
            if (c == '-') {
                minNumField.setText("");
                return false;
            } else if (c < '0' || c > '9') {
                minNumField.setText("");
                return false;
            }
        }
        for (char c : maxStr.toCharArray()) {
            if (c == '-') {
                maxNumField.setText("");
                return false;
            } else if (c < '0' || c > '9') {
                maxNumField.setText("");
                return false;
            }
        }
        for (char c : currentInv.toCharArray()) {
            if (c == '-') {
                inStockField.setText("");
                return false;
            } else if (c < '0' || c > '9') {

                inStockField.setText("");
                return false;
            }
        }
        return true;
    }

    //validates all areas, does the appropriate checks for inventory levels and min/max relations
    public boolean validateAll() {
        int min;
        int max;
        int inv;
        if (priceCostField.getText().isEmpty() || coNameField.getText().isEmpty() || minNumField.getText().isEmpty() || maxNumField.getText().isEmpty() || inStockField.getText().isEmpty() || partNameField.getText().isEmpty()) {
            alertFields();
            return false;
        } else {

            min = Integer.parseInt(minNumField.getText());
            max = Integer.parseInt(maxNumField.getText());
            inv = Integer.parseInt(inStockField.getText());
            if (min > max) {

                alertInventory("Minimum inventory cannot be higher than maximum.");
                return false;
            } else if (inv < min) {

                alertInventory("Cannot have fewer in stock than minimum.");
                return false;
            } else if (inv > max) {

                alertInventory("Cannot have more in stock than maximum.");
                return false;
            }
        }

        return true;
    }

    /*
    
    ALL DIALOG BOXES AND CONFIRMATIONS
    */
    public void alertInventory(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Inventory Warning");
        alert.setHeaderText("Invalid Inventory");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void alertDouble(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Price Amount");
        alert.setContentText("Price must be a number.");
        alert.showAndWait();      
    }
    public void alertFields(){
         Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Incomplete Information");
        alert.setHeaderText("Incomplete Field Information");
        alert.setContentText("Please fill out all fields.");
        alert.showAndWait(); 
    }
    
        public boolean confirmBox() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Confirm your selection.");
        alert.setContentText("Are you sure you want to cancel? You will lose any data that hasn't been saved.");
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }
}
