/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mongodb.jdbc;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.operation.OperationExecutor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;
import net.minidev.json.JSONObject;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

/**
 *
 * @author alex231
 */
public class MongoDatabase extends com.mongodb.MongoDatabase implements JSObject {

    protected MongoDatabase(final String name, final CodecRegistry codecRegistry, final ReadPreference readPreference, final WriteConcern writeConcern, final ReadConcern readConcern, final OperationExecutor executor) {
        super(name, codecRegistry, readPreference, writeConcern, readConcern, executor);
    }
    
    public Document runCommand(String json) {
        return runCommand( Document.parse(json) ) ;
    }

    public Document runCommand(Bson bson, ReadPreference readPreference) {
        return runCommand( bson, readPreference );
    }
    
    public Object call(Object o, Object... os) {
        throw new UnsupportedOperationException("call");
    }

    public Object newzObject(Object... os) {
        throw new UnsupportedOperationException("newzObject");
    }

    public Object eval(String string) {
        throw new UnsupportedOperationException("eval");
    }

    public Object getMember(final String name) 
    {
        throw new UnsupportedOperationException();
    }

    public Object getSlot(int i) {
       return null;
    }

    @Override
    public boolean hasMember(String name) {
        throw new UnsupportedOperationException();
    }

    public boolean hasSlot(int i) {
        return false;
    }

    public void removeMember(String name) {
        Objects.requireNonNull(name);
    }

    public void setMember(String name, Object value) {
        Objects.requireNonNull(name);
    }

    public void setSlot(int i, Object o) {

    }

    public Set<String> keySet() {
        return Collections.emptySet();
    }

    public Collection<Object> values() {
        return Collections.emptySet();
    }

    public boolean isInstance(Object o) {
        return false;
    }

    public boolean isInstanceOf(Object clazz) {
        if (clazz instanceof JSObject) {
            return ((JSObject)clazz).isInstance(this);
        }

        return false;
    }

    public String getClassName() {
        return getClass().getName();
    }

    public boolean isFunction() {
      return false;
    }

    public boolean isStrictFunction() {
      return false;
    }

    public boolean isArray() {
      return false;
    }

    public double toNumber() {
        return JSType.toNumber(JSType.toPrimitive(this, Number.class));
    }

    public Object newObject(Object... os) {
        throw new UnsupportedOperationException("newObject");
    }
    
}
