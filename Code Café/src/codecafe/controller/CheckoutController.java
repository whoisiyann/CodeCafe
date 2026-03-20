package codecafe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import codecafe.db.DBConnection;
import codecafe.model.OrderData;
import javafx.print.PrinterJob;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintColor;
import javafx.print.PrintResolution;

public class CheckoutController {

    @FXML private Button checkout_btn2;
    @FXML private Label order_TYPE;
    @FXML private VBox ordered_items_VBox2;
    @FXML private Label total_items_label2;
    @FXML private Label total_price_label2;
    @FXML private Button back_to_menu_btn;


    public void loadCheckoutData(HashMap<String, Node> orderedItemsMap, int totalItems, double totalPrice) {
        ordered_items_VBox2.getChildren().clear();

        for (Node item : orderedItemsMap.values()) {
            ordered_items_VBox2.getChildren().add(item);
            ImageView deleteBtn = (ImageView) item.lookup("#delete_ordered_item");
            if (deleteBtn != null) {
                deleteBtn.setOnMouseClicked(e -> {
                    ordered_items_VBox2.getChildren().remove(item);
                    orderedItemsMap.entrySet().removeIf(entry -> entry.getValue() == item);
                });
            }
        }

        total_items_label2.setText("Total Item: " + totalItems);
        total_price_label2.setText("Total Price: ₱ " + String.format("%.2f", totalPrice));

        order_TYPE.setText(OrderData.getInstance().getOrderType());
    }

    @FXML
    private void goBackMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/menu.fxml"));
            Parent root = loader.load();
            MenuController controller = loader.getController();

            OrderData data = OrderData.getInstance();
            controller.restoreOrders(data.getOrderMap(), data.getTotalItems(), data.getTotalPrice());

            Stage stage = (Stage) back_to_menu_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        checkout_btn2.setOnAction(e -> printReceipt());
    }

    // Generate receipt text
    private String generateReceipt(HashMap<String, Node> orderedItemsMap, int totalItems, double totalPrice, String orderType) {
        StringBuilder receipt = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String orderNumber = getNextOrderNumber();

        receipt.append("================================\n");
        receipt.append("           Code Café           \n");
        receipt.append("   Arellano Street, Downtown   \n");
        receipt.append("    Dagupan City, 2400 Pang.  \n");
        receipt.append("   Email: codecafe.@gmail.com  \n");
        receipt.append("   Tel: (700) 574-2002         \n");
        receipt.append("================================\n");
        receipt.append("Order #: ").append(orderNumber).append("\n");
        receipt.append("Order Type: ").append(orderType).append("\n");
        receipt.append("Date: ").append(dateTime).append("\n");
        receipt.append("--------------------------------\n");


        for (Node itemCard : orderedItemsMap.values()) {
            Label nameLabel = (Label) itemCard.lookup("#ordered_name");
            Label qtyLabel = (Label) itemCard.lookup("#orderedQuantityLabel");
            Label priceLabel = (Label) itemCard.lookup("#ordered_price");
            Text addonsText = (Text) itemCard.lookup("#ordered_addons");

            String name = nameLabel != null ? nameLabel.getText() : "";
            String qty = qtyLabel != null ? qtyLabel.getText() : "x1";
            String price = priceLabel != null ? priceLabel.getText().replace("₱", "") : "0.00";
            String addons = addonsText != null ? addonsText.getText() : "";

            receipt.append(String.format("%-15s %3s  ₱ %s\n", name, qty, price));

            if (!addons.isEmpty()) {
                // wrap add-ons text 
                int wrapLength = 30; // max chars per line
                StringBuilder wrapped = new StringBuilder("  Add-ons: ");
                int count = 0;
                for (String word : addons.split(", ")) {
                    if (count + word.length() > wrapLength) {
                        wrapped.append("\n           "); // indent for next line
                        count = 0;
                    }
                    if (count != 0) wrapped.append(", ");
                    wrapped.append(word);
                    count += word.length() + 2; // +2 for comma and space
                }
                receipt.append(wrapped.toString()).append("\n");
            }
        }

        receipt.append("--------------------------------\n");
        receipt.append(String.format("TOTAL ITEMS: %d\n", totalItems));
        receipt.append(String.format("TOTAL PRICE: ₱ %.2f\n", totalPrice));
        receipt.append("================================\n");
        receipt.append("   Thank you for visiting Code Café!\n");
        receipt.append("       Enjoy your day!            \n");
        receipt.append("================================\n");

        return receipt.toString();
    }

    @FXML
    private void printReceipt() {
        try {

            saveOrderToDatabase();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/logoprint.fxml"));
            ImageView logoView = loader.load();
            logoView.setFitWidth(150);
            logoView.setFitHeight(125);
            logoView.setPreserveRatio(true);

            // Put logo inside HBox for printing
            HBox logoContainer = new HBox();
            logoContainer.setPrefWidth(450);               
            logoContainer.setAlignment(Pos.TOP_LEFT);      
            logoContainer.setPadding(new Insets(0, 0, 0, 50)); 

            // Generate receipt text 
            String receiptText = generateReceipt(
                    OrderData.getInstance().getOrderMap(),
                    OrderData.getInstance().getTotalItems(),
                    OrderData.getInstance().getTotalPrice(),
                    OrderData.getInstance().getOrderType()
            );

            //  Create receipt label with wrapping 
            Label receiptLabel = new Label(receiptText);
            receiptLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12; -fx-text-fill: black;");
            receiptLabel.setWrapText(true);    
            receiptLabel.setMaxWidth(400);     

            // Create VBox to hold logo HBox and receipt text 
            VBox printContent = new VBox();
            printContent.setSpacing(10);
            printContent.setStyle("-fx-padding: 20;");  
            printContent.setPrefWidth(450);
            printContent.setMaxWidth(450);
            printContent.setAlignment(Pos.TOP_CENTER);   

            // Add logo container and receipt label
            printContent.getChildren().addAll(logoContainer, receiptLabel);

            //  Printer job 
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                job.getJobSettings().setPrintColor(PrintColor.MONOCHROME);
                job.getJobSettings().setPageLayout(
                        job.getPrinter().createPageLayout(
                                Paper.NA_LETTER,
                                PageOrientation.PORTRAIT,
                                36, 36, 36, 36 // margins: top, right, bottom, left in points
                        )
                );

                if (job.printPage(printContent)) {
                    job.endJob();
                }
            }

            //  Open showReceipt.fxml window 
            FXMLLoader pdfLoader = new FXMLLoader(getClass().getResource("/codecafe/view/print_or_showReceipt.fxml"));
            Parent pdfRoot = pdfLoader.load();
            Stage stage = (Stage) checkout_btn2.getScene().getWindow();
            stage.setScene(new Scene(pdfRoot, 1920, 1080));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ///Save order to Database
    private void saveOrderToDatabase() {

        System.out.println("Saving order to database...");

        try (Connection conn = DBConnection.connect()) {

            System.out.println("Database connected!");

            OrderData data = OrderData.getInstance();

            String orderNumber = getNextOrderNumber();

            String orderSQL =
            "INSERT INTO orders (order_number, order_type, total_items, total_price, status) VALUES (?, ?, ?, ?, ?)";

            var ps = conn.prepareStatement(orderSQL, java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, orderNumber);
            ps.setString(2, data.getOrderType());
            ps.setInt(3, data.getTotalItems());
            ps.setDouble(4, data.getTotalPrice());
            ps.setString(5, "PENDING");

            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();

            int orderId = 0;

            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            String itemSQL =
            "INSERT INTO order_items (order_id, item_name, quantity, price, addons) VALUES (?, ?, ?, ?, ?)";

            var itemPS = conn.prepareStatement(itemSQL);

            for (Node itemCard : data.getOrderMap().values()) {

                Label name = (Label) itemCard.lookup("#ordered_name");
                Label qty = (Label) itemCard.lookup("#orderedQuantityLabel");
                Label price = (Label) itemCard.lookup("#ordered_price");
                Text addons = (Text) itemCard.lookup("#ordered_addons");

                itemPS.setInt(1, orderId);
                itemPS.setString(2, name.getText());
                itemPS.setInt(3, Integer.parseInt(qty.getText()));
                itemPS.setDouble(4, Double.parseDouble(price.getText().replace("₱","").trim()));
                itemPS.setString(5, addons.getText());

                itemPS.addBatch();
            }

            itemPS.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNextOrderNumber() {

        String nextOrder = "00001";

        try (Connection conn = DBConnection.connect()) {

            String sql = "SELECT order_number FROM orders ORDER BY id DESC LIMIT 1";

            var ps = conn.prepareStatement(sql);
            var rs = ps.executeQuery();

            if (rs.next()) {

                String lastOrder = rs.getString("order_number");
                int num = Integer.parseInt(lastOrder);
                num++;

                nextOrder = String.format("%05d", num);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nextOrder;
    }
}