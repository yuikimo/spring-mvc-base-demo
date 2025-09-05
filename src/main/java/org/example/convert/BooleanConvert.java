package org.example.convert;

public class BooleanConvert extends Convert<Boolean>{

    public BooleanConvert(Class<Boolean> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {

        return defaultConvert(arg.toString());
    }
}
