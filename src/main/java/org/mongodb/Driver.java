
package org.mongodb;

import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

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
                throw new SQLException("Unknown Host: " + exception.getMessage(), exception);
            }
        }
        return null;
    }

    public boolean acceptsURL(String url) throws SQLException {
        return url != null && url.startsWith("mongodb:") || url.startsWith("jdbc:mongodb:");
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
