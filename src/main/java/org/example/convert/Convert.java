package org.example.convert;

public abstract class Convert<T> {

    protected Class<T> type;

    public Class<T> getType() {
        return type;
    }

    public Convert(Class<T> type) {
        this.type = type;
    }

    protected abstract Object convert(Object arg) throws Exception;

    /**
     * 返回基本包装类型
     * @param arg
     * @return
     * @throws Exception
     */
    protected Object defaultConvert(String arg) throws Exception {
        return type.getConstructor(String.class).newInstance(arg);
    }
}
