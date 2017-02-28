package com.rexxie.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionProvider {
    private Map<String, DataSource> dataSources = new HashMap<>();
    private Map<String, Connection> hashedConnections = new HashMap<>();
    private DbConfig config;

    @Inject
    public ConnectionProvider(DbConfig config) {
        this.config = config;

        loadConnectionsFromConfig(config);
    }

    public ConnectionProvider() {
    }

    /**
      * Load connection information from configuration class and generate DataSource objects
     */
    private void loadConnectionsFromConfig(DbConfig config) {
        for (Map.Entry<String, ConnectionReference> connection: config.getConnections().entrySet()) {
            addConnectionReference(connection.getKey(), connection.getValue());
        }
    }

    /**
     * That method add new connection to connection pool
     * @param name connection key or alias
     * @param reference Object with all connection properties
    */
    public void addConnectionReference(String name, ConnectionReference reference) {
        DataSource dataSource = createDataSource(reference);
        getDataSources().put(name, dataSource);
    }

    /**
     * Generate new DataSource object using data from ConnectionReference
    */
    private DataSource createDataSource(ConnectionReference connection) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + connection.getHost());
        dataSource.setUser(connection.getUsername());
        dataSource.setPassword(connection.getPassword());

        return dataSource;
    }

    /**
      * Return new connection to database by connection name
     */
    public Connection getConnection(String connectionName) throws SQLException {
        if (this.hashedConnections.containsKey(connectionName)) {
            Connection conn = this.hashedConnections.get(connectionName);
            if (!conn.isClosed()) {
                return conn;
            }
        }
        Connection conn = getDataSource(connectionName).getConnection();
        this.hashedConnections.put(connectionName, conn);

        return conn;
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
