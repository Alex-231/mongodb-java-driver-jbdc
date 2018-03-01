
package org.mongodb.jdbc;

import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import com.mongodb.MongoOptions;
import java.util.ArrayList;

public class Driver implements java.sql.Driver {
    
    static Driver singleton;
    
    //Register the driver to the DriverManager :)
    static {
      try 
      {
        Driver registeredDriver = new Driver();
        DriverManager.registerDriver(registeredDriver);
        singleton = registeredDriver;
      } 
      catch (SQLException e) 
      {
        throw new ExceptionInInitializerError(e);
      }
    }
    
    public Connection connect(String url, Properties info) throws SQLException {
        if (acceptsURL( url )){
            try	{
                if ( url.startsWith("jdbc:")) {
                    url = url.substring(5);
                }
                final Service service = new Service(url, info);

                return new Connection(service);
            } catch (UnknownHostException exception) {
                throw new SQLException("Unknown Host: " 
                        + exception.getMessage(), exception);
            }
        }
        return null;
    }

    public boolean acceptsURL(String url) throws SQLException {
        return url != null && 
                url.startsWith("mongodb:") || url.startsWith("jdbc:mongodb:");
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return MongoClientOptionsHelper.getDriverPropertyInfos();
    }

    public int getMajorVersion() {
        return Integer.parseInt(getClass().getPackage()
                .getImplementationVersion().substring(0, 1));
    }

    public int getMinorVersion() {
        return Integer.parseInt(getClass().getPackage()
                .getImplementationVersion().substring(2, 3));
    }

    public boolean jdbcCompliant() {
        return true;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
    
}
