package com.example.asd2;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    private static final String CONNECTION_STRING = "mongodb+srv://mz123:aOTQ2FI6iSpoNNEz@advancedsoftwaredevelop.66a2g.mongodb.net/?retryWrites=true&w=majority&appName=AdvancedSoftwareDevelopment";
    private static MongoClient mongoClient;

    @Bean
    public MongoDatabase mongoDatabase() {
        if (mongoClient == null) {
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .serverApi(serverApi)
                    .build();
            mongoClient = MongoClients.create(settings);
        }
        return mongoClient.getDatabase("ASD");  // 使用你的数据库名称
    }
}
