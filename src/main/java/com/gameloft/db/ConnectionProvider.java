package com.gameloft.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionProvider {
    private Map<String, DataSource> dataSources = new HashMap<>();
    private DbConfig config;

    public ConnectionProvider(DbConfig config) {
        this.config = config;

        for (Map.Entry<String, ConnectionReference> connection: config.getConnections().entrySet()) {
            getDataSources().put(connection.getKey(), createDataSource(connection.getValue()));
        }
    }

    public ConnectionProvider() {
    }

    public void addConnectionReference(String name, ConnectionReference reference) {
        DataSource dataSource = createDataSource(reference);
        getDataSources().put(name, dataSource);
    }

    private DataSource createDataSource(ConnectionReference connection) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + connection.getHost());
        dataSource.setUser(connection.getUsername());
        dataSource.setPassword(connection.getPassword());

        return dataSource;
    }

    public Connection getConnection(String connectionName) throws SQLException {
        return getDataSource(connectionName).getConnection();
    }

    private Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    private void setDataSources(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    private DataSource getDataSource(String connectionName) {
        return getDataSources().get(connectionName);
    }

    private DbConfig getConfig() {
        return config;
    }
}
