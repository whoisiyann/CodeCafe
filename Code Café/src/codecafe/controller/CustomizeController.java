package codecafe.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;

public class CustomizeController {

    // Main drink UI
    @FXML private ImageView coffeeImage;
    @FXML private Label coffeeNameLabel;
    @FXML private Label coffeePriceLabel;
    @FXML private Text coffeeDescriptionLabel;
    @FXML private Button BacktoMenu_btn;
    @FXML private Button minusBtn;
    @FXML private Button plusBtn;
    @FXML private Label quantityLabel;
    @FXML private Button addToOrderBtn;

    // Add-on panes
    @FXML private AnchorPane espresoShot;
    @FXML private AnchorPane sinker;
    @FXML private AnchorPane coldFoam;
    @FXML private AnchorPane syrup;
    @FXML private AnchorPane cheesecake;
    @FXML private AnchorPane whippedCream;
    @FXML private AnchorPane subOutMilk;

    private MenuController menuController;
    private AnchorPane contentAnchor;
    private AnchorPane previousMenu;

    // Price and quantity
    private double basePrice = 0;
    private int quantity = 1;


    //PesoFormat #,###.00
    private final DecimalFormat pesoFormat = new DecimalFormat("#,###.00");

    // Add-ons model
    private static class AddOn {
        String name;
        double price;
        int quantity = 0;
        Label quantityLabel;
    }

    private final List<AddOn> addOns = new ArrayList<>();

    //Initialize drink data
    public void setData(String name, String price, String description, String imagePath) {
        coffeeNameLabel.setText(name);

        basePrice = Double.parseDouble(price.replace("₱", "").trim());
        quantity = 1;
        quantityLabel.setText(String.valueOf(quantity));
        coffeeDescriptionLabel.setText(description);

        coffeeImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        coffeeImage.setUserData(imagePath);

        // Reset add-ons
        for (AddOn addOn : addOns) {
            addOn.quantity = 0;
            if (addOn.quantityLabel != null) addOn.quantityLabel.setText("0");
        }

        updatePrice();
    }

    //Set parent anchor panes and menu controller
    public void setContentAnchor(AnchorPane contentAnchor, AnchorPane previousMenu, MenuController menuController) {
        this.contentAnchor = contentAnchor;
        this.previousMenu = previousMenu;
        this.menuController = menuController;

        attachBackButton();
        attachQuantityHandlers();
        attachAddOnHandlers();
        attachAddToOrderHandler();
    }

    //Handle back button
    private void attachBackButton() {
        BacktoMenu_btn.setOnAction(e -> {
            if (contentAnchor != null && previousMenu != null) {
                contentAnchor.getChildren().setAll(previousMenu);
            }
        });
    }

    //Handle main drink quantity
    private void attachQuantityHandlers() {
        minusBtn.setOnAction(e -> {
            if (quantity > 1) {
                quantity--;
                quantityLabel.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        plusBtn.setOnAction(e -> {
            quantity++;
            quantityLabel.setText(String.valueOf(quantity));
            updatePrice();
        });
    }

    //Initialize add-ons
    private void attachAddOnHandlers() {
        setupAddOn(espresoShot);
        setupAddOn(sinker);
        setupAddOn(coldFoam);
        setupAddOn(syrup);
        setupAddOn(cheesecake);
        setupAddOn(whippedCream);
        setupAddOn(subOutMilk);
    }

    //Set up individual add-on pane
    private void setupAddOn(AnchorPane pane) {
        if (pane == null) return;

        Button minus = null;
        Button plus = null;
        Label quantityLbl = null;
        Label priceLbl = null;

        for (Node node : pane.getChildren()) {
            if (node instanceof Label l && l.getStyleClass().contains("add-ons-prize")) {
                priceLbl = l;
            }

            if (node instanceof HBox hbox) {
                for (Node child : hbox.getChildren()) {
                    if (child instanceof Button b) {
                        if (b.getText().equals("➖")) minus = b;
                        if (b.getText().equals("➕")) plus = b;
                    } else if (child instanceof Label l) {
                        quantityLbl = l;
                    }
                }
            }
        }

        if (minus == null || plus == null || quantityLbl == null || priceLbl == null) return;

        AddOn addOn = new AddOn();
        addOn.name = ((Button)pane.getChildren().get(0)).getText(); // assume first child is name
        addOn.price = Double.parseDouble(priceLbl.getText().replace("₱", "").trim());
        addOn.quantityLabel = quantityLbl;

        addOns.add(addOn);

        // Add add-ons quantity
        plus.setOnAction(e -> {
            addOn.quantity++;
            addOn.quantityLabel.setText(String.valueOf(addOn.quantity));
            if (addOn.quantity > 0) {
                pane.setStyle("-fx-background-color: #6B3F2A;");
            }
            updatePrice();
        });

        // Minus add-ons quantity
        minus.setOnAction(e -> {
            if (addOn.quantity > 0) {
                addOn.quantity--;
                addOn.quantityLabel.setText(String.valueOf(addOn.quantity));
                if (addOn.quantity == 0) {
                    pane.setStyle("-fx-background-color: #C69A6B;");
                }
                updatePrice();
            }
        });
    }

    //Add to order button handler
private void attachAddToOrderHandler() {

    addToOrderBtn.setOnAction(e -> {

        String name = coffeeNameLabel.getText();
        String imagePath = coffeeImage.getUserData() != null 
                ? coffeeImage.getUserData().toString() 
                : "";

        double addOnsTotal = 0;

        for (AddOn addOn : addOns) {
            addOnsTotal += addOn.price * addOn.quantity;
        }

        String priceStr = "₱ " + pesoFormat.format(basePrice);

        // Format add-ons text
        List<String> selectedAddOns = new ArrayList<>();

        for (AddOn addOn : addOns) {
            if (addOn.quantity > 0) {
                selectedAddOns.add(addOn.quantity + " " + addOn.name);
            }
        }

        String addonsStr = selectedAddOns.isEmpty() 
                ? "" 
                : "Add-ons: " + String.join(", ", selectedAddOns);

        if (menuController != null) {

            menuController.addOrderItem(
                    name,
                    priceStr,
                    imagePath,
                    addonsStr,
                    quantity,
                    addOnsTotal
            );
        }

        if (contentAnchor != null && previousMenu != null) {
            contentAnchor.getChildren().setAll(previousMenu);
        }
    });
}
    

    //Update total price
    private double getPerItemPrice() {

        double addOnsTotal = 0;

        for (AddOn addOn : addOns) {
            addOnsTotal += addOn.price * addOn.quantity;
        }

        return (basePrice * quantity) + addOnsTotal;
    }

    
    private void updatePrice() {

        double addOnsTotal = 0;

        for (AddOn addOn : addOns) {
            addOnsTotal += addOn.price * addOn.quantity;
        }

        double total = (basePrice * quantity) + addOnsTotal;

        coffeePriceLabel.setText("₱ " + pesoFormat.format(total));
    }
}