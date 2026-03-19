package codecafe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

import codecafe.model.OrderData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;


import java.text.DecimalFormat;


public class MenuController {

    @FXML private Button click_coffee_btn;
    @FXML private Button click_cafe_special_btn;
    @FXML private Button click_nonCoffee_btn;
    @FXML private Button click_matcha_series_btn;
    @FXML private Button click_fruit_soda_btn;
    @FXML private Button click_food_btn;
    @FXML private Button click_pastries_btn;
    @FXML private Button back_btn;
    @FXML private AnchorPane contentAnchor;
    @FXML private Label total_price_label;
    @FXML private Label total_items_label;
    @FXML private Button checkout_btn;
    @FXML private Label delete_all_items;

    @FXML private VBox ordered_items_VBox;
    @FXML private AnchorPane reminder_ordered_card;

    @FXML
    private Button noClearBtn;

    @FXML
    private Button yesClearBtn;

    private Button activeButton = null;
    private AnchorPane activeCard = null;

    private AnchorPane currentMenuPane;

    private HashMap<Button, String> menuFXMLMap = new HashMap<>();
    private HashMap<Button, AnchorPane> loadedMenus = new HashMap<>();
    private HashMap<String, Node> orderedItemsMap = new HashMap<>();

    private double totalPrice = 0;
    private int totalItems = 0;

    //PesoFormat #,###.00
    private final DecimalFormat pesoFormat = new DecimalFormat("#,##0.00");

    @FXML
    public void initialize() {

        checkout_btn.setDisable(true);

        menuFXMLMap.put(click_coffee_btn, "/codecafe/view/coffee.fxml");
        menuFXMLMap.put(click_cafe_special_btn, "/codecafe/view/cafe_specials.fxml");
        menuFXMLMap.put(click_nonCoffee_btn, "/codecafe/view/non_coffee.fxml");
        menuFXMLMap.put(click_matcha_series_btn, "/codecafe/view/matcha_series.fxml");
        menuFXMLMap.put(click_fruit_soda_btn, "/codecafe/view/fruit_soda.fxml");
        menuFXMLMap.put(click_food_btn, "/codecafe/view/food.fxml");
        menuFXMLMap.put(click_pastries_btn, "/codecafe/view/croffles.fxml");

        menuFXMLMap.keySet().forEach(btn -> btn.setOnAction(e -> setActiveMenu(btn)));

        Platform.runLater(() -> setActiveMenu(click_coffee_btn));

        delete_all_items.setOnMouseClicked(this::deleteAllItems);

    }


    // Delete all ordered items
    private void clearAllOrders() {

        ordered_items_VBox.getChildren().clear();
        orderedItemsMap.clear();

        totalPrice = 0;
        totalItems = 0;

        updateTotals();

        ordered_items_VBox.getChildren().add(reminder_ordered_card);
    }



    private void setActiveMenu(Button clickedBtn) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("nav-button-active");
            activeButton.getStyleClass().add("nav-button");
        }
        if (activeCard != null) {
            activeCard.getStyleClass().remove("nav-menu-card-active");
            activeCard.getStyleClass().add("nav-menu-card");
        }

        clickedBtn.getStyleClass().remove("nav-button");
        clickedBtn.getStyleClass().add("nav-button-active");
        AnchorPane parentCard = (AnchorPane) clickedBtn.getParent();
        parentCard.getStyleClass().remove("nav-menu-card");
        parentCard.getStyleClass().add("nav-menu-card-active");
        activeButton = clickedBtn;
        activeCard = parentCard;

        AnchorPane content = loadedMenus.get(clickedBtn);
        if (content == null) {
            try {
                content = FXMLLoader.load(getClass().getResource(menuFXMLMap.get(clickedBtn)));
                loadedMenus.put(clickedBtn, content);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentAnchor.getChildren().setAll(content);
        currentMenuPane = content;

        final AnchorPane finalContent = content;


        if (clickedBtn == click_coffee_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "coffee",
                    new String[]{"coffee1","coffee2","coffee3","coffee4","coffee5","coffee6","coffee7","coffee8","coffee9","coffee10"},
                    new String[]{"Americano","Cafe Latte","Hazelnut","Spanish","Caramel Macchiato","Dark Mocha","Cinnamon","Toffee Nut","French Vanilla","Chocolate Mocha"},
                    new String[]{"₱ 99.00","₱ 109.00","₱ 109.00","₱ 119.00","₱ 119.00","₱ 119.00","₱ 119.00","₱ 119.00","₱ 119.00","₱ 129.00"},
                    new String[]{
                        "A smooth and bold coffee made by blending rich espresso with hot water. It has a clean taste with a light bitterness, perfect for those who enjoy classic black coffee.",
                        "A creamy and balanced drink made with espresso and steamed milk. It offers a smooth coffee flavor with a mild sweetness and velvety texture.",
                        "A comforting blend of espresso, milk, and sweet hazelnut flavor. This drink has a nutty aroma and smooth finish that makes it both rich and satisfying.",
                        "A sweet and creamy coffee made with espresso and condensed milk. It delivers a richer and slightly caramelized flavor compared to a regular latte.",
                        "A layered espresso drink with milk and sweet caramel drizzle. It combines smooth coffee with buttery caramel for a rich and indulgent taste.",
                        "A bold coffee drink made with espresso and deep chocolate flavor. Perfect for chocolate lovers who enjoy a stronger coffee kick.",
                        "A warm and aromatic coffee infused with a hint of cinnamon spice. It’s smooth, slightly sweet, and comforting with every sip.",
                        "A sweet and nutty coffee drink with the rich taste of toffee and roasted nuts. The creamy milk balances the espresso for a smooth and flavorful experience.",
                        "A creamy espresso drink with a sweet and fragrant vanilla flavor. Smooth and comforting, it’s perfect for those who prefer a lighter coffee taste.",
                        "A rich combination of espresso, milk, and sweet white chocolate. It has a creamy and indulgent flavor with a smooth chocolate finish."},
                        
                    new String[]{
                        "/codecafe/assets/images/Coffee_Based/americano.png",
                        "/codecafe/assets/images/Coffee_Based/cafe_latte.png",
                        "/codecafe/assets/images/Coffee_Based/hazelnut.png",
                        "/codecafe/assets/images/Coffee_Based/spanish.png",
                        "/codecafe/assets/images/Coffee_Based/caramel_macchiato.png",
                        "/codecafe/assets/images/Coffee_Based/dark_mocha.png",
                        "/codecafe/assets/images/Coffee_Based/cinnamon.png",
                        "/codecafe/assets/images/Coffee_Based/toffee_nut.png",
                        "/codecafe/assets/images/Coffee_Based/french_vanilla.png",
                        "/codecafe/assets/images/Coffee_Based/white_chocolate_mocha.png"
                    }));
        }
        else if (clickedBtn == click_cafe_special_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "special",
                    new String[]{"special1","special2","special3","special4","special5","special6"},
                    new String[]{"Caramel Sealalt","Oreo Cheesecake","House Special","Spanish Cereal","Hazelnut Mocha","Dirty Matcha"},
                    new String[]{"₱ 129.00","₱ 129.00","₱ 129.00","₱ 129.00","₱ 129.00","₱ 139.00"},
                    new String[]{
                        "A delicious blend of espresso, creamy milk, and caramel with a hint of sea salt. The sweet and salty balance creates a unique and satisfying flavor.",
                        "A decadent coffee drink inspired by classic desserts. It combines creamy cheesecake flavor, chocolate cookie notes, and espresso for a rich and indulgent treat.",
                        "A signature drink crafted with our special blend of coffee and sweet flavors. Smooth, creamy, and perfectly balanced for a unique Code Café experience.",
                        "A creamy Spanish latte enhanced with a sweet cereal-inspired flavor. It offers a nostalgic taste with smooth espresso and milk.",
                        "A rich combination of chocolate, espresso, and nutty hazelnut flavor. Smooth, creamy, and perfect for those who love both coffee and chocolate.",
                        "A unique fusion of earthy matcha and bold espresso. The mix of green tea and coffee creates a balanced drink with both energy and flavor."},

                    new String[]{
                        "/codecafe/assets/images/Coffee_Bases_Specials/caramel_seasalt.png",
                        "/codecafe/assets/images/Coffee_Bases_Specials/oreo_cheesecake_tiramisu.png",
                        "/codecafe/assets/images/Coffee_Bases_Specials/house_special.png",
                        "/codecafe/assets/images/Coffee_Bases_Specials/spanish_cereal.png",
                        "/codecafe/assets/images/Coffee_Bases_Specials/hazelnut_mocha.png",
                        "/codecafe/assets/images/Coffee_Bases_Specials/dirty_matcha.png"
                    }));
        }
        else if (clickedBtn == click_nonCoffee_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "nonCoffee",
                    new String[]{"nonCoffee1","nonCoffee2","nonCoffee3","nonCoffee4","nonCoffee5","nonCoffee6"},
                    new String[]{"Strawberry Milk","Blueberry Milk","Cookies & Cream","Hershy Choco","Strawberry Yogurt","Blueberry Yogurt"},
                    new String[]{"₱ 109.00","₱ 109.00","₱ 109.00","₱ 109.00","₱ 125.00","₱ 135.00"},
                    new String[]{
                        "A refreshing and creamy milk drink blended with sweet strawberry flavor. Smooth, fruity, and perfect for a light and enjoyable treat.",
                        "A delicious milk drink infused with sweet blueberry flavor. It’s creamy, fruity, and refreshing with a smooth finish.",
                        "A creamy drink blended with chocolate cookie crumbs and milk. Rich, sweet, and perfect for dessert lovers.",
                        "A rich chocolate drink made with smooth milk and Hershey’s chocolate flavor. It’s creamy, sweet, and perfect for chocolate lovers.",
                        "A refreshing drink made with creamy yogurt and sweet strawberry flavor. Slightly tangy and fruity, it’s both delicious and refreshing.",
                        "A smooth yogurt-based drink with a sweet blueberry taste. It has a refreshing balance of fruity sweetness and creamy tanginess."},

                    new String[]{
                        "/codecafe/assets/images/Non_Coffee/strawberry_milk.png",
                        "/codecafe/assets/images/Non_Coffee/blueberry_milk.png",
                        "/codecafe/assets/images/Non_Coffee/cookies_and_cream.png",
                        "/codecafe/assets/images/Non_Coffee/hershey_choco.png",
                        "/codecafe/assets/images/Non_Coffee/strawberry_yogurt.png",
                        "/codecafe/assets/images/Non_Coffee/blueberry_yogurt.png",

                    }));
        }
        else if (clickedBtn == click_matcha_series_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "matcha",
                    new String[]{"matcha1","matcha2","matcha3","matcha4","matcha5"},
                    new String[]{"Matcha Latte","Matcha Special","Strawberry Matcha","Oreo Matcha","Biscoff Matcha"},
                    new String[]{"₱ 109.00","₱ 119.00","₱ 129.00","₱ 129.00","₱ 139.00"},
                    new String[]{
                        "A smooth blend of premium matcha green tea and creamy milk. It has a rich, earthy flavor with a naturally refreshing taste.",
                        "A creamy matcha drink made with our special flavor twist. Smooth, slightly sweet, and perfect for matcha lovers.",
                        "A refreshing combination of earthy matcha and sweet strawberry flavor. The fruity sweetness perfectly balances the green tea.",
                        "A dessert-inspired matcha drink with cookies and cheesecake flavor. Creamy, rich, and packed with sweet matcha goodness.",
                        "A unique matcha drink topped with the caramelized flavor of Biscoff. Creamy, sweet, and perfectly balanced with earthy matcha."},


                    new String[]{
                        "/codecafe/assets/images/Matcha_Series/matcha_latte.png",
                        "/codecafe/assets/images/Matcha_Series/matcha_special.png",
                        "/codecafe/assets/images/Matcha_Series/strawberry_matcha.png",
                        "/codecafe/assets/images/Matcha_Series/oreo_matcha_cheesecake.png",
                        "/codecafe/assets/images/Matcha_Series/biscoff_matcha.png",
                    }));
        }
        else if (clickedBtn == click_fruit_soda_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "soda",
                    new String[]{"soda1","soda2","soda3","soda4","soda5","soda6"},
                    new String[]{"Blueberry Soda","Strawberry Soda","Green Apple Soda","Lychee Soda","Passionfruit Soda","Hibiscus Soda"},
                    new String[]{"₱ 69.00","₱ 69.00","₱ 69.00","₱ 69.00","₱ 69.00","₱ 69.00"},
                    new String[]{
                        "A fizzy drink infused with sweet blueberry flavor. Refreshing and lightly sweet with a sparkling finish.",
                        "A bright and refreshing soda with sweet strawberry flavor. Perfect for a cool and fruity beverage.",
                        "A crisp and tangy soda with the refreshing taste of green apples. Light, bubbly, and perfect for hot days.",
                        "A refreshing soda drink with the sweet and floral flavor of lychee. Smooth, fruity, and pleasantly fizzy.",
                        "A tropical soda with the bold and tangy taste of passionfruit. Sweet, refreshing, and full of vibrant flavor.",
                        "A floral and refreshing soda with a slightly tart hibiscus flavor. Light, unique, and pleasantly aromatic."},

                    new String[]{
                        "/codecafe/assets/images/Fruit_Soda/blueberry_soda.png",
                        "/codecafe/assets/images/Fruit_Soda/strawberry_soda.png",
                        "/codecafe/assets/images/Fruit_Soda/green_apple_soda.png",
                        "/codecafe/assets/images/Fruit_Soda/lychee_soda.png",
                        "/codecafe/assets/images/Fruit_Soda/passionfruit_soda.png",
                        "/codecafe/assets/images/Fruit_Soda/hibiscus_soda.png",

                    }));
        }
        else if (clickedBtn == click_food_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "food",
                    new String[]{"food1","food2","food3","food4","food5","food6","food7","food8"},
                    new String[]{"Chicharap","Regular Fries","Cheesy Fries","Cheesy Bacon Fries","Grilled Cheese","Classic Quesadilla","Beff Quesadilla","Beff Nachos"},
                    new String[]{"₱ 99.00","₱ 109.00","₱ 109.00","₱ 119.00","₱ 119.00","₱ 129.00","₱ 119.00","₱ 119.00"},
                    new String[]{
                        "Crispy golden fries topped with seasoned beef, melted cheese, and crunchy nacho bits. Every bite delivers a savory mix of crispy, cheesy, and meaty goodness. Perfect for sharing or enjoying as a satisfying snack.",
                        "A warm grilled tortilla filled with flavorful seasoned beef and melted cheese. Toasted to a golden crisp on the outside while remaining soft and cheesy inside. A hearty and satisfying savory treat.",
                        "Crispy fries smothered in rich melted cheese and topped with savory bacon bits. The combination of creamy cheese and smoky bacon makes every bite indulgent and flavorful. A perfect comfort snack.",
                        "Golden crispy fries topped with a generous layer of creamy melted cheese. Simple, rich, and irresistibly satisfying. Perfect for cheese lovers.",
                        "Light and crispy fried crackers served with a savory dipping sauce. Crunchy, airy, and perfect as a quick snack while enjoying your drink. A classic crunchy treat.",
                        "A toasted tortilla filled with melted cheese and savory fillings. Grilled until crispy outside and gooey inside for the perfect bite. A simple yet delicious comfort snack.",
                        "Buttery toasted bread filled with perfectly melted cheese. Crispy on the outside and soft and cheesy on the inside. A classic snack loved by all ages.",
                        "Freshly fried golden potato fries with a crispy outside and soft inside. Lightly salted and served with a dipping sauce. A simple and satisfying snack."},
                        
                    new String[]{
                        "/codecafe/assets/images/Food/chicharap.png",
                        "/codecafe/assets/images/Food/regular_fries.png",
                        "/codecafe/assets/images/Food/cheesy_fries.png",
                        "/codecafe/assets/images/Food/cheesy_becon_fries.png",
                        "/codecafe/assets/images/Food/grilled_cheese.png",
                        "/codecafe/assets/images/Food/classic_quesadilla.png",
                        "/codecafe/assets/images/Food/beef_quesadilla.png",
                        "/codecafe/assets/images/Food/beef_nachos.png",
                    }));
        }
        else if (clickedBtn == click_pastries_btn) {
            Platform.runLater(() -> attachHandlers(finalContent,
                "croffles",
                    new String[]{"croffles1","croffles2","croffles3","croffles4","croffles5"},
                    new String[]{"Biscoff Cream","Oreo Cream","Nutella Almond","Nutella Mallows","Strawberry Cream"},
                    new String[]{"₱ 135.00","₱ 135.00","₱ 135.00","₱ 135.00","₱ 135.00"},
                    new String[]{
                        "A warm waffle topped with smooth cream and rich Biscoff spread. Finished with a drizzle of caramelized cookie butter for a sweet and indulgent dessert.",
                        "A delicious waffle topped with whipped cream, crushed Oreo cookies, and chocolate drizzle. A perfect blend of creamy, crunchy, and chocolatey flavors.",
                        "A fluffy waffle layered with creamy Nutella and crunchy almond slices. The nutty crunch perfectly complements the rich chocolate flavor.",
                        "A warm waffle topped with creamy Nutella and soft marshmallows. The toasted mallows add a sweet, gooey texture for a delightful dessert treat.",
                        "A soft waffle topped with fresh strawberry topping and creamy whipped cream. Sweet, fruity, and refreshing for a light dessert option."},

                    new String[]{
                        "/codecafe/assets/images/Croffles/biscoff_cream.png",
                        "/codecafe/assets/images/Croffles/oreo_cream.png",
                        "/codecafe/assets/images/Croffles/nutella_almond.png",
                        "/codecafe/assets/images/Croffles/nutella_mallows.png",
                        "/codecafe/assets/images/Croffles/strawberry_cream.png",
                    }));
        }

    }

    @FXML
    private void goBack(javafx.event.ActionEvent event) {
        try {
            // Load back.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/back.fxml"));
            Parent backPane = loader.load();

            // Create a new stage for modal dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            Scene scene = new Scene(backPane);
            dialogStage.setScene(scene);

            // Make modal (cannot click main window)
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            dialogStage.initOwner(mainStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.centerOnScreen();

            // Get controller and set stages
            GoBackController controller = loader.getController();
            controller.setStages(dialogStage, mainStage);

            // Show dialog
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCustomize(String name, String price, String description, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/customize.fxml"));
            AnchorPane customizePane = loader.load();
            CustomizeController controller = loader.getController();
            controller.setData(name, price, description, imagePath);
            controller.setContentAnchor(contentAnchor, currentMenuPane, this);

            contentAnchor.getChildren().setAll(customizePane);
            customizePane.setLayoutX(0);
            customizePane.setLayoutY(0);
            customizePane.prefWidthProperty().bind(contentAnchor.widthProperty());
            customizePane.prefHeightProperty().bind(contentAnchor.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attachHandlers(
            AnchorPane pane,
            String plusPrefix,
            String[] cardIds,
            String[] names,
            String[] prices,
            String[] descriptions,
            String[] images) {

        for (int i = 0; i < cardIds.length; i++) {

            AnchorPane card = (AnchorPane) pane.lookup("#" + cardIds[i]);
            Label plusBtn = (Label) pane.lookup("#" + plusPrefix + "_" + (i + 1));

            final int index = i;

            // CARD CLICK -> OPEN CUSTOMIZE.FXML
            if (card != null) {
                card.setOnMouseClicked(e -> openCustomize(
                        names[index],
                        prices[index],
                        descriptions[index],
                        images[index]
                ));
            }

            // PLUS CLICK -> DIRECT ORDER
            if (plusBtn != null) {
                plusBtn.setOnMouseClicked(e -> {

                addOrderItem(
                        names[index],
                        prices[index],
                        images[index],
                        "",
                        1,
                        0
                );

                    e.consume();
                });
            }
        }
    }



    // ADD ORDER ITEM

    public void addOrderItem(String name, String price, String imagePath, String addons, int quantity, double addonsTotal) {

        try {

            String key = name + addons;

            if (orderedItemsMap.containsKey(key)) {

                Node existingCard = orderedItemsMap.get(key);

                Label qtyLabel = (Label) existingCard.lookup("#orderedQuantityLabel");
                Label priceLabel = (Label) existingCard.lookup("#ordered_price");

                int currentQty = Integer.parseInt(qtyLabel.getText());
                currentQty += quantity;

                qtyLabel.setText(String.valueOf(currentQty));

                double perItemPrice = (double) existingCard.getUserData();
                double newPrice = perItemPrice * currentQty;

                priceLabel.setText("₱ " + pesoFormat.format(newPrice));

                updateTotals();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/ordered_item.fxml"));
            StackPane itemCard = loader.load();

            // GET UI ELEMENTS
            ImageView orderedImage = (ImageView) itemCard.lookup("#ordered_image");
            Label orderedName = (Label) itemCard.lookup("#ordered_name");
            Label orderedPrice = (Label) itemCard.lookup("#ordered_price");
            Text orderedAddons = (Text) itemCard.lookup("#ordered_addons");
            Label orderedQuantityLabel = (Label) itemCard.lookup("#orderedQuantityLabel");
            Button minusBtn = (Button) itemCard.lookup("#ordered_minusBtn");
            Button plusBtn = (Button) itemCard.lookup("#ordered_plusBtn");
            ImageView deleteBtn = (ImageView) itemCard.lookup("#delete_ordered_item");

            // SET TEXT AND IMAGE
            orderedName.setText(name);
            orderedQuantityLabel.setText(String.valueOf(quantity)); 
            orderedAddons.setText(addons);

            double basePrice = Double.parseDouble(price.replace("₱","").trim());

            double totalPrice = (basePrice * quantity) + addonsTotal;

            itemCard.setUserData(basePrice + addonsTotal);

            orderedPrice.setText("₱ " + pesoFormat.format(totalPrice));

            if (!imagePath.isEmpty()) {
                orderedImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            }

            // DELETE BUTTON
            if (deleteBtn != null) {
                deleteBtn.setOnMouseClicked(e -> {
                    ordered_items_VBox.getChildren().remove(itemCard);
                    orderedItemsMap.entrySet().removeIf(entry -> entry.getValue() == itemCard);
                    updateTotals();
                    if (ordered_items_VBox.getChildren().isEmpty()) {
                        ordered_items_VBox.getChildren().add(reminder_ordered_card);
                    }
                });
            }

            // MINUS BUTTON
            if (minusBtn != null && orderedQuantityLabel != null) {
                minusBtn.setOnAction(e -> {
                    int currentQty = Integer.parseInt(orderedQuantityLabel.getText());
                    if (currentQty > 1) {  // minimum quantity = 1
                        currentQty--;
                        orderedQuantityLabel.setText(String.valueOf(currentQty));
                        double perItemPrice = (double) itemCard.getUserData();
                        double newPrice = perItemPrice * currentQty;
                        orderedPrice.setText("₱ " + pesoFormat.format(newPrice));
                        updateTotals();
                    }
                });
            }

            // PLUS BUTTON
            if (plusBtn != null && orderedQuantityLabel != null) {
                plusBtn.setOnAction(e -> {
                    int currentQty = Integer.parseInt(orderedQuantityLabel.getText());
                    currentQty++;
                    orderedQuantityLabel.setText(String.valueOf(currentQty));
                    double perItemPrice = (double) itemCard.getUserData();
                    double newPrice = perItemPrice * currentQty;
                    orderedPrice.setText("₱ " + pesoFormat.format(newPrice));
                    updateTotals();
                });
            }

            // ADD TO VBox
            ordered_items_VBox.getChildren().remove(reminder_ordered_card);
            ordered_items_VBox.getChildren().add(itemCard);

            // SAVE IN MAP
            orderedItemsMap.put(key, itemCard);

            updateTotals();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    


    // UPDATE TOTALS

    private void updateTotals() {

        double totalPrice = 0;
        int totalItems = 0;

        for (Node itemCard : orderedItemsMap.values()) {

            Label qtyLabel = (Label) itemCard.lookup("#orderedQuantityLabel");

            if (qtyLabel != null) {

                double perItemPrice = (double) itemCard.getUserData();
                int itemQty = Integer.parseInt(qtyLabel.getText().replace("x",""));

                totalPrice += perItemPrice * itemQty;
                totalItems += itemQty;
            }
        }

        this.totalPrice = totalPrice;
        this.totalItems = totalItems;

        total_price_label.setText("Total Price: ₱ " + pesoFormat.format(totalPrice));
        total_items_label.setText("Total Item: " + totalItems);

        // DISABLE CHECKOUT BUTTON IF ORDERED IS EMPTY
        checkout_btn.setDisable(totalItems == 0);
    }


    // DELETE ALL 

    @FXML
    private void deleteAllItems(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/delete_confirmation.fxml"));
            AnchorPane popup = loader.load();

            DeleteAllItemController controller = loader.getController();

            Stage popupStage = new Stage();
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.centerOnScreen();

            controller.yesClearBtn.setOnAction(e -> {
                clearAllOrders();
                popupStage.close();
            });

            controller.noClearBtn.setOnAction(e -> popupStage.close());

            popupStage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleCheckout() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/checkout.fxml"));
            Parent root = loader.load();

            CheckoutController controller = loader.getController();

            OrderData data = OrderData.getInstance();
            data.setOrderMap(new HashMap<>(orderedItemsMap));
            data.setTotalItems(totalItems);
            data.setTotalPrice(totalPrice);

            controller.loadCheckoutData(
                data.getOrderMap(),
                data.getTotalItems(),
                data.getTotalPrice()
            );
        

            Stage stage = (Stage) checkout_btn.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Node> getOrderedItemsMap(){
    return orderedItemsMap;
    }

    public int getTotalItems(){
        return totalItems;
    }

    public double getTotalPrice(){
        return totalPrice;
    }

    public void restoreOrders(HashMap<String, Node> map, int items, double price){

        orderedItemsMap = map;

        ordered_items_VBox.getChildren().clear();

        if(orderedItemsMap.isEmpty()){
            ordered_items_VBox.getChildren().add(reminder_ordered_card);
        } 
        else {

            for(Node item : orderedItemsMap.values()){

                ImageView deleteBtn = (ImageView) item.lookup("#delete_ordered_item");

                if(deleteBtn != null){

                    deleteBtn.setOnMouseClicked(e -> {

                        ordered_items_VBox.getChildren().remove(item);

                        orderedItemsMap.entrySet().removeIf(entry -> entry.getValue() == item);

                        updateTotals();

                        if (ordered_items_VBox.getChildren().isEmpty()) {
                            ordered_items_VBox.getChildren().add(reminder_ordered_card);
                        }

                    });

                }

                ordered_items_VBox.getChildren().add(item);
            }
        }

        totalItems = items;
        totalPrice = price;

        updateTotals();
    }


    
}