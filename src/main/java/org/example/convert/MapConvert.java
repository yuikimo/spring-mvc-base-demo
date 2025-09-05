package org.example.convert;

import java.util.HashMap;
import java.util.Map;

public class MapConvert extends Convert<HashMap>{

    public MapConvert(Class<HashMap> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Map.class).newInstance(arg);
    }
}
