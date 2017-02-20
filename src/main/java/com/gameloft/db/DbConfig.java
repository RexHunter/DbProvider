package com.gameloft.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DbConfig {
    private static final Logger logger = LogManager.getLogger(DbConfig.class);

    private String configPath;
    private Map<String, ConnectionReference> connections = new HashMap<>();

    public DbConfig(String configPath) throws IOException {
        this.configPath = configPath;
        this.connections = loadConfigReferencesFromFile(this.configPath);
    }

    private DbConfig(Map<String, ConnectionReference> connections) {
        setConnections(connections);
    }

    public DbConfig() {
    }

    public void addConnection(String name, ConnectionReference reference) {
        this.connections.put(name, reference);
    }

    /**
     * load data from json configuration file
     *
     * @return HasMap with connections
     */
    private Map<String, ConnectionReference> loadConfigReferencesFromFile(String filePath) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        connections = objectMapper.readValue(new File(filePath), new TypeReference<Map<String, ConnectionReference>>() {
        });
        return connections;
    }

    private String getConfigPath() {
        return this.configPath;
    }

    private void setConnections(Map<String, ConnectionReference> connections) {
        this.connections = connections;
    }

    public ConnectionReference getConnection(String connectionName) {
        return this.connections.get(connectionName);
    }

    public Map<String, ConnectionReference> getConnections() {
        return this.connections;
    }
}
