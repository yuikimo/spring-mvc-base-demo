package org.example.convert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterRegistry {

    private List<Convert> convertList = new ArrayList<>();

    public void addConverter(Convert convert) {
        this.convertList.add(convert);
    }

    public Map<Class, ConvertHandler> getConverts() {
        final HashMap<Class, ConvertHandler> convertHandlerHashMap = new HashMap<>();
        for (Convert convert : this.convertList) {
            final Class type = convert.getType();
            try {
                final Method method = convert.getClass().getDeclaredMethod("convert", String.class);
                convertHandlerHashMap.put(type, new ConvertHandler(convert, method));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return convertHandlerHashMap;
    }
}
