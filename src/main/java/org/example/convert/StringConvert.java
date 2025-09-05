package org.example.convert;

public class StringConvert extends Convert<String>{


    public StringConvert(Class<String> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
