
package org.mongodb;

import com.mongodb.MongoOptions; //Not Thread Safe(?)
import java.lang.reflect.Method;
import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.Properties;

public class MongoOptionsHelper {
    
    //These are used as keys in properties.
    //It seemed redundant to store the string option name in a variable
    // with the same name.
    //Option names should match fields in com.mongodb.MongoOptions.
    private enum MongoOptionName
    {
        alwaysUseMBeans,
        autoConnectRetry,
        connectionsPerHost,
        connectTimeout,
        cursorFinalizerEnabled,
        dbDecoderFactory,
        dbEncoderFactory,
        description,
        fsync,
        j,
        maxAutoConnectRetryTime,
        maxWaitTime,
        readPreference,
        safe,
        slaveOk,
        socketFactory,
        socketKeepAlive,
        socketTimeout,
        threadsAllowedToBlockForConnectionMultiplier,
        w,
        wtimeout
    }
    
    //Just a couple of constructors to help out with DriverPropertyInfo.
    public static class MongoOptionDriverPropertyInfo extends DriverPropertyInfo
    {
        public MongoOptionDriverPropertyInfo(MongoOptionName mongoOptionName, String value, String description) {
            super(mongoOptionName.toString(), value);
            this.description = description;
        }
        
        public MongoOptionDriverPropertyInfo(MongoOptionName mongoOptionName, String value, String description, String[] choices) {
            this(mongoOptionName, value, description);
            this.choices = choices;
        }
    }
    
    public MongoOptions getMongoOptions(Properties properties)
    {
        MongoOptions result = new MongoOptions();
        Class<?> mongoOptionsClass = result.getClass();
        
        for(MongoOptionName eOptionName : MongoOptionName.values())
        {
            String name = eOptionName.toString();
            
            if(properties.containsKey(name))
            {
                //= set + C + onnectionsPerHost
                String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                
                //TODO
                // Get declared methods in MongoOptions.
                // Loop through. If method name matches setMethodName...
                // Check the parameter type. Should be only 1 param.
                // If string, great! call with value.
                // If int/bool, parse and call.
                // If enum, get type, get value, call. (this could get complicated)
            }
        }
        
        return result;
    }
    
    public static DriverPropertyInfo[] getDriverPropertyInfos()
    {
        ArrayList<DriverPropertyInfo> driverPropertyInfos = new ArrayList<DriverPropertyInfo>();
        DriverPropertyInfo propertyInfo;
        
        ////////////////////////////////////////////////////////////////////////
        //Property descriptions retrieved from MongoDB API, 21:25 GMT 28/2/18
        //https://api.mongodb.com/java/2.6/com/mongodb/MongoOptions.html
        
        propertyInfo = new MongoOptionDriverPropertyInfo(MongoOptionName.connectionsPerHost, "10",
                "The number of connections allowed per host (the pool size, per host)");
        driverPropertyInfos.add(propertyInfo);
        
        return (DriverPropertyInfo[]) driverPropertyInfos.toArray();
    }        
}
