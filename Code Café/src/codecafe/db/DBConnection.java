package codecafe.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    //                                                                                      for laptop 1 - Kios
    private static final String URL = "jdbc:mysql://localhost:3306/codecafe_db"; // "jdbc:mysql://192.168.1._:3306/codecafe_db"
    private static final String USER = "root";      //  "kiosk"          -laptop1 kiosk
    private static final String PASSWORD = "admin"; //  "kioskpass"      -laptop1 kiosk

    public static Connection connect() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// PARA GUMANA DOWNLOAD MYSQL, WORKBENCH, MYSQL CONNECTOR


/* 

------------------------------------------------------------------------------------
//for MySQL Workbench type...



CREATE DATABASE codecafe_db;
USE codecafe_db;

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(10),
    order_type VARCHAR(20),
    total_items INT,
    total_price DOUBLE,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    item_name VARCHAR(100),
    quantity INT,
    price DOUBLE,
    addons TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);


------------------------------------------------------------------------------------

Laptop 1 (mag rurun ng KIOS)
private static final String URL = "jdbc:mysql://192.168.x.x:3306/codecafe_db";

------------------------------------------------------------------------------------

Laptop 2 (mag rurun ng KDS and sya yung may hawak/merong Mysql Database)
private static final String URL = "jdbc:mysql://localhost:3306/codecafe_db";

------------------------------------------------------------------------------------

REQUIREMENT PARA MAG CONNECT ANG ISANG LAPTOP 1 (KIOS) SA DATABASE

# no.1

Sa laptop 2, open cmd: type...
ipconfig

hanapin si:
IPv4 Address . . . . . . . . . . : 192.168.x.x

example:
192.168.1.5

yun ang ilalagay sa Kiosk Laptop 1... sa DBConnection.java nasa taas
private static final String URL = "jdbc:mysql://192.168.1.5:3306/codecafe_db";

------------------------------------------------------------------------------------

# no.2

Open MySQL Workbench
I-configure para tumanggap ng external connection

type...
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'admin';
FLUSH PRIVILEGES;

------------------------------------------------------------------------------------

# no.3

CHECK MySQL config file
pumunta sa filepath ng MySQL kung saan mo nilagay hanapin si my.ini

saakin ganito
C:\ProgramData\MySQL\MySQL Server 9.6\my.ini

hanapin:
bind-address=127.0.0.1

palitan ng:
bind-address=0.0.0.0
    -para tumanggap sya ng connection galing sa ibang device


pag hindi mahanap...
Option A:

open cmd type...
mysql --help | find "my.ini"

Option B:
Open Notepad... run as administrator, type..........

[mysqld]
bind-address=0.0.0.0



save mo kung saan yung path ng MySQL server 9.6 mo...  example  C:\ProgramData\MySQL\MySQL Server 9.6\



LAST is irun mo si TestDB.java

check mo kung 

Connected successfully! or Connection failed!







*/