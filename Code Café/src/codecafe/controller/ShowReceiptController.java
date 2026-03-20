package codecafe.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import codecafe.model.OrderData;

public class ShowReceiptController {

    @FXML private VBox itemsContainer;
    @FXML private Label orderNumberLabel;
    @FXML private Label orderTypeLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalItemsLabel;
    @FXML private Label totalPriceLabel;


    private int getLastOrderId() {

        int id = 1;

        try (java.sql.Connection conn = codecafe.db.DBConnection.connect()) {

            String sql = "SELECT id FROM orders ORDER BY id DESC LIMIT 1";

            var ps = conn.prepareStatement(sql);
            var rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    @FXML
    public void initialize() {

        OrderData data = OrderData.getInstance();

        var orderMap = data.getOrderMap();

        // SET HEADER INFO
        orderNumberLabel.setText("Order #: " + String.format("%05d", getLastOrderId()));
        orderTypeLabel.setText("Order Type: " + data.getOrderType());

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        dateLabel.setText("Date: " + date);

        // CLEAR SAMPLE ITEM
        itemsContainer.getChildren().clear();

        // LOOP ORDERS
        for (Node itemCard : orderMap.values()) {

            Label nameLabel = (Label) itemCard.lookup("#ordered_name");
            Label qtyLabel = (Label) itemCard.lookup("#orderedQuantityLabel");
            Label priceLabel = (Label) itemCard.lookup("#ordered_price");
            Text addonsText = (Text) itemCard.lookup("#ordered_addons");

            String name = nameLabel != null ? nameLabel.getText() : "";
            String qty = qtyLabel != null ? qtyLabel.getText() : "1";
            String price = priceLabel != null ? priceLabel.getText() : "₱0.00";
            String addons = addonsText != null ? addonsText.getText() : "";

            // CREATE RECEIPT ITEM ROW
            HBox row = new HBox(5);

            Label nameL = new Label(name);
            nameL.setPrefWidth(150);

            Label qtyL = new Label("x" + qty);
            qtyL.setPrefWidth(30);

            Label priceL = new Label(price);
            priceL.setPrefWidth(60);

            row.getChildren().addAll(nameL, qtyL, priceL);

            itemsContainer.getChildren().add(row);

            // ADDONS
            if (!addons.isEmpty()) {
                Label addonsLabel = new Label("Add-ons: " + addons);
                addonsLabel.setWrapText(true);
                itemsContainer.getChildren().add(addonsLabel);
            }
        }

        // TOTALS
        totalItemsLabel.setText("TOTAL ITEMS: " + data.getTotalItems());
        totalPriceLabel.setText("TOTAL PRICE: ₱ " + String.format("%.2f", data.getTotalPrice()));
    }
}