package com.mongodb.jdbc;

import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 *
 * @author alex231
 */
public class Service {
    
    private final MongoClient client;
    private final String uri;
    private final String databaseName;
    
    public Service(final String uri, final Properties properties) throws UnknownHostException {
        
        //If the username & password have been passed as properties, add them to the uri :)
        if(properties.containsKey("username") && properties.containsKey("password"))
            this.uri = uri.replace("mongo://", "mongo://" + properties.getProperty("username") + ":" + properties.getProperty("password") + "@");
        else
            this.uri = uri;
        
        String uriWithoutProtocol = this.uri.replace("mongodb://", "");
        if(uriWithoutProtocol.contains("/") && uriWithoutProtocol.lastIndexOf("/") < uriWithoutProtocol.length())
            this.databaseName = uriWithoutProtocol.substring(uriWithoutProtocol.lastIndexOf('/'));
        else
            this.databaseName = "admin";
        
        client = new MongoClient( uri );
    }
    
    public MongoDatabase getDatabase(String dbName) {
        return (MongoDatabase) client.getDatabase(dbName);
    }
    
    //public 
    
}
