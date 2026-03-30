package codecafe.model;

import javafx.scene.Node;
import java.util.HashMap;

public class OrderData {

    private static OrderData instance;

    private HashMap<String, Node> orderMap = new HashMap<>();
    private int totalItems = 0;
    public double totalPrice = 0;
    private String orderType = "";   
    
    private int orderCounter = 1;
    
    // Singleton access
    public static OrderData getInstance() {
        if(instance == null){
            instance = new OrderData();
        }
        return instance;
    }

    // --- Getters and Setters ---
    public HashMap<String, Node> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(HashMap<String, Node> map) {
        this.orderMap = map;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int items) {
        this.totalItems = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double price) {
        this.totalPrice = price;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String type) {
        this.orderType = type;
    }

    public void clear() {
        orderMap.clear();
        totalItems = 0;
        totalPrice = 0;
        orderType = "";
    }

    public void clearData() {
        orderMap.clear();
        totalItems = 0;
        totalPrice = 0;
        orderType = null;

    }
}