package org.example.convert;

import java.util.Date;

public class DateConvert extends Convert<Date> {

    public DateConvert(Class<Date> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
