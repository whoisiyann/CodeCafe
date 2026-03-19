package codecafe_KDS.controller;

import java.sql.Connection;
import java.sql.ResultSet;

import codecafe.db.DBConnection;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Insets;

public class OrderCardController {

    private int orderId;

    @FXML private Label order_NUMBER;
    @FXML private VBox itemsContainer;
    @FXML private StackPane orderCard;
    @FXML private Label dineIn_takeOut;

    // References from parent
    private GridPane ordersGrid;
    private Label totalOrdersLabel;

    // Method to set parent references
    public void setParentData(GridPane ordersGrid, Label totalOrdersLabel) {
        this.ordersGrid = ordersGrid;
        this.totalOrdersLabel = totalOrdersLabel;
    }

    public void setOrderData(int orderId) {
        this.orderId = orderId;

        try (Connection conn = DBConnection.connect()) {

            // Kunin yung order type
            String orderSql = "SELECT order_type FROM orders WHERE id = " + orderId;
            ResultSet orderRs = conn.createStatement().executeQuery(orderSql);
            if (orderRs.next()) {
                String orderType = orderRs.getString("order_type");
                dineIn_takeOut.setText(orderType.toUpperCase());
            }

            // Kunin yung items at addons
            String sql = "SELECT * FROM order_items WHERE order_id = " + orderId;
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                Label item = new Label("x" + rs.getInt("quantity") + " " + rs.getString("item_name"));
                item.setStyle("-fx-font-size: 18px;");
                itemsContainer.getChildren().add(item);

                String addons = rs.getString("addons");
                if (addons != null && !addons.isEmpty()) {

                    Text addonsText = new Text("    " + addons);
                    addonsText.setStyle("-fx-font-size: 15px;");

                    TextFlow flow = new TextFlow(addonsText);
                    flow.setPrefWidth(340); // adjust depende sa width ng order card

                    VBox.setMargin(flow, new Insets(0, 0, 10, 0));

                    itemsContainer.getChildren().add(flow);
                }
            }

            order_NUMBER.setText(String.format("#%05d", orderId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void completeOrder() {
        try (Connection conn = DBConnection.connect()) {

            // Update database
            String sql = "UPDATE orders SET status='DONE' WHERE id=?";
            var ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.executeUpdate();

            // Remove card from GridPane
            ordersGrid.getChildren().remove(orderCard);

            // Update total orders
            int remainingOrders = ordersGrid.getChildren().size();
            totalOrdersLabel.setText("Total Orders: " + remainingOrders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}