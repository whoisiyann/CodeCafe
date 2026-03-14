package codecafe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PDFreceiptController {

    @FXML private Button pdfReceiptBtn;

    @FXML
    private void initialize() {
        pdfReceiptBtn.setOnAction(e -> {
            Stage stage = (Stage) pdfReceiptBtn.getScene().getWindow();
            stage.close();
            System.out.println("PDF Receipt button clicked!");
        });
    }
}