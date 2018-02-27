# Mongo Java Driver JDBC (MJDJ)
MJDJ is a wrapper for mongo-java-driver that adds jdbc driver implementation.

Unlike some alternatives, this library does not parse SQL queries.

If you have any feature requests or bugs, please create [an issue](https://github.com/Alex-231/mongo-java-driver-jbdc/issues/new) or check out details below on how to get setup with developing MJDJ further.

## Using MJDJ

MJDJ automatically registers itself to the DriverManager.

A standard mongo URI can be used to connect to a database:
```java
"mongodb://username:password@host:port/database"
```

OR, the username and password can be passed as parameters to the connection.
```java
DriverManager.getConnection("mongodb://host:port/database", "username", "password"); 
```

OR as Properties.
```java
Properties properties = new Properties();
properties.put("username", username)
properties.put("password", password)
DriverManager.getConnection("mongodb://host:port/database", properties); 
```

In both cases, jdbc: can be included.

```java
"jdbc:mongodb://username:password@host:port/database"
```

But don't include the username & password in both the URI and the properties. Pick one :)