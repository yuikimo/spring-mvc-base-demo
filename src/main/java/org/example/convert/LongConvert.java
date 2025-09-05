package org.example.convert;

public class LongConvert extends Convert<Long>{

    public LongConvert(Class<Long> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
