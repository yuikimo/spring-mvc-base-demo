package org.example.convert;

import java.util.ArrayList;
import java.util.Collection;

public class ListConvert extends Convert<ArrayList> {

    public ListConvert(Class<ArrayList> type) {
        super(type);
    }


    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Collection.class).newInstance(arg);
    }
}
