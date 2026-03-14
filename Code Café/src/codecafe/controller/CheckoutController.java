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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

    private static int orderCounter = 1; // Simple order # generator

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

    // Generate receipt text with updated info
    private String generateReceipt(HashMap<String, Node> orderedItemsMap, int totalItems, double totalPrice, String orderType) {
        StringBuilder receipt = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String orderNumber = String.format("%05d", orderCounter++);

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

        // --- inside generateReceipt() method ---

        for (Node itemCard : orderedItemsMap.values()) {
            Label nameLabel = (Label) itemCard.lookup("#ordered_name");
            Label qtyLabel = (Label) itemCard.lookup("#ordered_name1");
            Label priceLabel = (Label) itemCard.lookup("#ordered_prize");
            Text addonsText = (Text) itemCard.lookup("#ordered_addons");

            String name = nameLabel != null ? nameLabel.getText() : "";
            String qty = qtyLabel != null ? qtyLabel.getText() : "x1";
            String price = priceLabel != null ? priceLabel.getText().replace("₱", "") : "0.00";
            String addons = addonsText != null ? addonsText.getText() : "";

            receipt.append(String.format("%-15s %3s  ₱ %s\n", name, qty, price));

            if (!addons.isEmpty()) {
                // --- wrap add-ons text ---
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
        // --- Load logo ---
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe/view/logoprint.fxml"));
        ImageView logoView = loader.load();
        logoView.setFitWidth(150);
        logoView.setFitHeight(125);
        logoView.setPreserveRatio(true);

        // --- Put logo inside HBox for proper alignment ---
        HBox logoContainer = new HBox();
        logoContainer.setPrefWidth(450);               // same width as printContent
        logoContainer.setAlignment(Pos.TOP_LEFT);      // align logo to left
        logoContainer.setPadding(new Insets(0, 0, 0, 50)); // adjust horizontal offset (50 = move right)
        logoContainer.getChildren().add(logoView);

        // --- Generate receipt text ---
        String receiptText = generateReceipt(
                OrderData.getInstance().getOrderMap(),
                OrderData.getInstance().getTotalItems(),
                OrderData.getInstance().getTotalPrice(),
                OrderData.getInstance().getOrderType()
        );

        // --- Create receipt label with wrapping ---
        Label receiptLabel = new Label(receiptText);
        receiptLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12; -fx-text-fill: black;");
        receiptLabel.setWrapText(true);    // ensures long lines wrap
        receiptLabel.setMaxWidth(400);     // adjust width as needed

        // --- Create VBox to hold logo HBox and receipt text ---
        VBox printContent = new VBox();
        printContent.setSpacing(10);
        printContent.setStyle("-fx-padding: 20;");  // padding for margins
        printContent.setPrefWidth(450);
        printContent.setMaxWidth(450);
        printContent.setAlignment(Pos.TOP_CENTER);   // centers receipt text horizontally

        // Add logo container and receipt label
        printContent.getChildren().addAll(logoContainer, receiptLabel);

        // --- Printer job ---
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

        // --- Open PDF/Print options window ---
        FXMLLoader pdfLoader = new FXMLLoader(getClass().getResource("/codecafe/view/print_or_savePDF.fxml"));
        Parent pdfRoot = pdfLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Receipt PDF / Print Options");
        stage.setScene(new Scene(pdfRoot));
        stage.show();

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
}