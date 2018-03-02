
package com.mongodb.util;

import com.mongodb.MongoClientOptions;
import java.lang.reflect.Method;
import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.Properties;

public class MongoClientOptionsHelper {
    
    //Here's how this works:
    //com.mongodb.MongoClientOptions.Builder contains methods to set option values.
    //These methods are all named the same name as the option.
    //Eg. connectionsPerHost is set with connectionsPerHost(int)
    //So, with reflection, this methods can be found, and if their name is found in property keys, invoked.
    //Could use refactoring.
    public MongoClientOptions getMongoClientOptions(Properties properties)
    {
        MongoClientOptions.Builder mongoClientOptionsBuilder = new MongoClientOptions.Builder();
        Class<?> mongoClientOptionsBuilderClass = mongoClientOptionsBuilder.getClass();
        
        for(Method mongoClientOptionBuilderMethod : mongoClientOptionsBuilderClass.getMethods())
        {
            String propertyName = mongoClientOptionBuilderMethod.getName();
            if(properties.containsKey(propertyName))
            {
                if(mongoClientOptionBuilderMethod.getParameterCount() != 1)
                {
                    System.out.println("Warning: Unsupported mongo client option in driver properties, " + mongoClientOptionBuilderMethod.getName());
                }
                
                Class<?> optionValueType = mongoClientOptionBuilderMethod.getParameterTypes()[0];
                String propertyValue = properties.getProperty(propertyName);
                
                if(optionValueType == int.class)
                {
                    try
                    {
                        int optionValue = Integer.parseInt(propertyValue);
                        mongoClientOptionBuilderMethod.invoke(mongoClientOptionsBuilder, optionValue);
                    }
                    catch(NumberFormatException numberFormatException)
                    {
                        System.out.println("Unable to set property " + propertyName + ", invalid value " + propertyValue);
                    }
                    catch(Exception exception)
                    {
                        System.out.println("Unexpected Exception: " + exception.getLocalizedMessage());
                    }
                }
                else if(optionValueType == boolean.class)
                {
                    try
                    {
                        boolean optionValue;
                        //Because Boolean.parseBoolean returns false if it can't parse,
                        //Custom parsing is required.
                        if("true".equals(propertyValue.toLowerCase()))
                        {
                            optionValue = true;
                        }
                        else if("false".equals(propertyValue.toLowerCase()))
                        {
                            optionValue = false;
                        }
                        else
                        {
                            System.out.println("Unable to set property " + propertyName + ", invalid value " + propertyValue);
                            continue;
                        }
                        
                        mongoClientOptionBuilderMethod.invoke(mongoClientOptionsBuilder, optionValue);
                    }
                    catch(Exception exception)
                    {
                        System.out.println("Unexpected Exception: " + exception.getLocalizedMessage());
                    }
                }
                else if(optionValueType == String.class)
                {
                    try
                    {
                        String optionValue = propertyValue;
                        mongoClientOptionBuilderMethod.invoke(mongoClientOptionsBuilder, optionValue);
                    }
                    catch(Exception exception)
                    {
                        System.out.println("Unexpected Exception: " + exception.getLocalizedMessage());
                    }
                }
                else if(optionValueType.isEnum())
                {
                    try
                    {
                        Object optionValue = Enum.valueOf((Class<Enum>) optionValueType, propertyValue);
                        mongoClientOptionBuilderMethod.invoke(mongoClientOptionsBuilder, optionValue);
                    }
                    catch(Exception exception)
                    {
                        System.out.println("Unexpected Exception: " + exception.getLocalizedMessage());
                    }
                }
            }
        }
        
        return mongoClientOptionsBuilder.build();
    }
    
    public static DriverPropertyInfo[] getDriverPropertyInfos()
    {
        ArrayList<DriverPropertyInfo> driverPropertyInfos = new ArrayList<DriverPropertyInfo>();
        
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder();
        Class<?> mongoClientOptionsBuilderClass = mongoClientOptionsBuilder.getClass();
        MongoClientOptions defaultMongoClientOptions = mongoClientOptionsBuilder.build();
        Class<?> mongoClientOptionsClass = defaultMongoClientOptions.getClass();
        
        for(Method mongoClientOptionBuilderMethod : mongoClientOptionsBuilderClass.getMethods())
        {
            if(mongoClientOptionBuilderMethod.getParameterCount() != 1)
                continue;
            
            String optionName = mongoClientOptionBuilderMethod.getName();
            
            Class<?> optionValueType = mongoClientOptionBuilderMethod.getParameterTypes()[0];
            if(optionValueType == String.class || optionValueType == int.class || optionValueType == boolean.class || optionValueType.isEnum())
            {
                Method getMongoClientOptionValueMethod;
                try
                {
                    if(optionValueType == boolean.class)
                        getMongoClientOptionValueMethod = mongoClientOptionsClass.getMethod(
                            "is" + optionName.substring(0,1).toUpperCase() + optionName.substring(1));
                    else
                        getMongoClientOptionValueMethod = mongoClientOptionsClass.getMethod(
                            "get" + optionName.substring(0,1).toUpperCase() + optionName.substring(1));
                } 
                catch (Exception exception) 
                {
                    System.out.println(exception.getLocalizedMessage());
                    System.out.println("Unable to find default value for option " + optionName + ", skipping.");
                    continue;
                }
                
                Object defaultOptionValue = null;
                
                try
                {
                    getMongoClientOptionValueMethod.invoke(defaultMongoClientOptions);
                }
                catch (Exception exception)
                {
                    System.out.println(exception.getLocalizedMessage());
                    System.out.println("Unable to find default value for option " + optionName + ", skipping.");
                    continue;
                }
                
                if(defaultOptionValue == null)
                {
                    System.out.println("Unable to find default value for option " + optionName + ", skipping.");
                }
                
                DriverPropertyInfo propertyInfo = new DriverPropertyInfo(optionName, defaultOptionValue.toString());
                if(optionValueType.isEnum())
                {
                    ArrayList<String> choices = new ArrayList<String>();
                    for(Object enumValue : optionValueType.getEnumConstants())
                    {
                        choices.add(enumValue.toString());
                    }
                    propertyInfo.choices = (String[])choices.toArray();
                }
            }
        }
        return (DriverPropertyInfo[]) driverPropertyInfos.toArray();
    }        
}
