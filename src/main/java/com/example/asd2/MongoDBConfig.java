package com.example.asd2;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConfig {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBConfig.class);
    private static final String CONNECTION_STRING =
            "mongodb+srv://mz123:UfNBnHJmIFfjWqE9@advancedsoftwaredevelop.66a2g.mongodb.net/?retryWrites=true&w=majority&appName=AdvancedSoftwareDevelopment";
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase(String databaseName) {
        if (mongoClient == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Initializing MongoDB client with connection string: {}", CONNECTION_STRING);
            }
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .serverApi(serverApi)
                    .build();
            mongoClient = MongoClients.create(settings);
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Connecting to MongoDB database: {}", databaseName);
            }
            return mongoClient.getDatabase(databaseName);
        } catch (MongoException e) {
            logger.error("Error while connecting to MongoDB: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to MongoDB");
        }
    }
}
