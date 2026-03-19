package codecafe_KDS.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import codecafe.db.DBConnection;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class KitchenDisplayController {

    @FXML
    private Label orders_page;

    @FXML
    private Button back_btn_orders;

    @FXML
    private Button next_btn_orders;


    @FXML private GridPane ordersGrid;

    @FXML
    private void previousPage() {

        if (currentPage > 0) {
            currentPage--;
            loadOrders();
        }
    }

    @FXML
    private void nextPage() {

        if (currentPage < totalPages - 1) {
            currentPage++;
            loadOrders();
        }
    }

    private int currentPage = 0;
    private final int ORDERS_PER_PAGE = 8;
    private int totalOrders = 0;
    private int totalPages = 0;

    public void initialize() {
        loadOrders();

            // AUTO REFRESH EVERY 3 SECONDS
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> refreshOrders())
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

        private void refreshOrders() {
        loadOrders();
    }

    private void loadOrders() {
        try (Connection conn = DBConnection.connect()) {

            calculatePages(conn);

            String sql = "SELECT * FROM orders WHERE status='PENDING' ORDER BY created_at ASC LIMIT ? OFFSET ?";
            var ps = conn.prepareStatement(sql);
            ps.setInt(1, ORDERS_PER_PAGE);
            ps.setInt(2, currentPage * ORDERS_PER_PAGE);

            ResultSet rs = ps.executeQuery();

            ordersGrid.getChildren().clear();

            int col = 0;
            int row = 0;

            while (rs.next()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecafe_KDS/view/order.fxml"));
                Node card = loader.load();

                OrderCardController controller = loader.getController();
                controller.setOrderData(rs.getInt("id"));

                ordersGrid.add(card, col, row);

                col++;
                if (col == 4) {
                    col = 0;
                    row++;
                }
            }

            updatePageUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePages(Connection conn) throws Exception {

        String countSql = "SELECT COUNT(*) FROM orders WHERE status='PENDING'";
        var ps = conn.prepareStatement(countSql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            totalOrders = rs.getInt(1);
        }

        totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);

        if (totalPages == 0) {
            totalPages = 1;
        }
    }

    private void updatePageUI() {

        orders_page.setText("Page " + (currentPage + 1) + "/" + totalPages);

        back_btn_orders.setDisable(currentPage == 0);

        next_btn_orders.setDisable(currentPage >= totalPages - 1);

    }

}