package org.example.convert;

public class IntegerConvert extends Convert<Integer>{

    public IntegerConvert(Class<Integer> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
