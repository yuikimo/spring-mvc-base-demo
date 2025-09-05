package org.example.convert;

public class FloatConvert extends Convert<Float>{

    public FloatConvert(Class<Float> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
