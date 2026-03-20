package codecafe.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.1.6:3306/codecafe_db"; //IP address ng laptop 2 for (KDS)
        String user = "kiosk";
        String pass = "kioskpass";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected successfully!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}