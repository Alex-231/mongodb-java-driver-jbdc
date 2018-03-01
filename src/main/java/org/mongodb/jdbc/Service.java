package org.mongodb.jdbc;

import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 *
 * @author alex231
 */
public class Service {
    
    private final MongoClient client;
    private String uri;
    
    public Service(final String uri, final Properties properties) throws UnknownHostException {
        this.uri = uri;
        
        if (uri.startsWith("mongo://")) {
            this.uri = this.uri.substring(8);
        }
        
        //If the username & password have been passed as properties, add them to the uri :)
        if(properties.containsKey("username") && properties.containsKey("password"))
        {
            this.uri = this.uri.replace("mongo://", "mongo://" + properties.getProperty("username") + ":" + properties.getProperty("password") + "@");
        }
        
        client = new MongoClient( uri );
    }
}
