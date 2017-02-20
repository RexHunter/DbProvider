package com.gameloft.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws IOException {
        DbConfig dbConfig = new DbConfig("./config/db.config.json");
//        dbConfig.addConnection("test-connection", new ConnectionReference("127.0.0.1", "root", "root"));

        System.out.println(dbConfig.getConnections());

        ConnectionProvider provider = new ConnectionProvider(dbConfig);

//        provider.addConnectionReference("test-connection", new ConnectionReference("127.0.0.1", "root", "root"));

        try {
            Connection conn = provider.getConnection("secretary");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
